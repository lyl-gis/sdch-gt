package edu.zju.gis.tool;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;
import edu.zju.gis.config.CommonSetting;
import edu.zju.gis.util.ElasticSearchHelper;
import edu.zju.gis.util.ShapefileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.json.JSONObject;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/13
 */
public class Importer {
    private static final Logger log = LogManager.getLogger(Importer.class);
    private static List<String> fields = new ArrayList<>();

    public static void main(String[] args) throws IOException, FactoryException, TransformException {
        if (args.length != 4) {
            System.out.println("Usage:" +
                    "\n\t 0 - config file(.properties)" +
                    "\n\t 1 - shapefile(.shp)" +
                    "\n\t 2 - 默认中文编码类型（默认GBK，带有.cpg的数据会使用其原有编码）" +
                    "\n\t 3 - 关键词（半角分号分隔）");
            return;
        }
        fields.addAll(Arrays.asList("id", "lsid", "name", "address", "telephone"
                , "AdCode", "zipcode", "kind", "type", "CLASID", "ENTIID6", "navid", "city", "grade", "level", "lng", "lat", "geometry"));
        for (int i = 1; i < 10; i++) {
            fields.add("NAME" + i);
        }
        String pConfig = args[0];
        String pShps = args[1];
        String pCharset = args[2];
        Charset charset = Charset.forName(pCharset.isEmpty() ? "GBK" : pCharset);
        String pKeywords = args[3];
        Properties configs = new Properties();
        InputStream is = Files.newInputStream(Paths.get(pConfig));
        configs.load(is);
        CommonSetting setting = CommonSetting.getInstance();
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        String indexName = configs.getProperty("index.name", setting.getEsIndex());
        String indexType = configs.getProperty("index.type");
        String fieldUuid = configs.getProperty("fields.uuid");
        int shards = Integer.parseInt(configs.getProperty("es.num_of_shards", "" + setting.getEsShards()));
        int replicas = Integer.parseInt(configs.getProperty("es.num_of_replicas", "" + setting.getEsReplicas()));
        helper.createIfNotExist(setting.getEsIndex(), shards, replicas);
        File rootFile = new File(pShps);
        if (pShps.toLowerCase().endsWith(".shp") && rootFile.isFile()) {
            File cpg = new File(pShps.replace(".shp", ".cpg"));
            Charset mCharset = charset;
            if (cpg.exists()) {
                List<String> lines = Files.readAllLines(cpg.toPath());
                if (!lines.isEmpty()) {
                    mCharset = Charset.forName(lines.get(0));
                }
            }
            List<Map<String, Object>> records = getRecords(indexName, indexType, rootFile, mCharset, fields, fieldUuid, pKeywords);
            int error = helper.publish(indexName, indexType, records);
            System.out.println(error);
        } else {
            System.out.println("输入数据必须是一个shapefile文件（.shp）");
        }
    }

    private static List<Map<String, Object>> getRecords(String indexName, String indexType, File shpFile, Charset charset, List<String> fields
            , String fieldUuid, String keywords) throws IOException, FactoryException, TransformException {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        List<Map<String, Object>> records = new ArrayList<>();
        try (ShapefileReader reader = new ShapefileReader(shpFile, charset)) {
            if (!helper.exists(indexName, indexType)) {
                String mappingSource = getMappingSource();
                helper.putMapping(indexName, indexType, mappingSource);
            }
            SimpleFeatureIterator iterator = reader.getFeatures().features();
            GeometryJSON geometryJSON = new GeometryJSON(9);
            CoordinateReferenceSystem crs = reader.getCrs();
            MathTransform transform = null;
            if (crs instanceof ProjectedCRS)
                transform = CRS.findMathTransform(crs, ((ProjectedCRS) crs).getBaseCRS());//投影坐标转换为大地坐标系，cgcs2000和wgs84的微小误差可忽略
            WKTWriter wktWriter = new WKTWriter();
            List<String> attrNames = reader.getFields();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Map<String, Object> map = new HashMap<>();
                map.put("doc_id", fieldUuid == null ? UUID.randomUUID() : feature.getAttribute(fieldUuid).toString());
                for (String key : attrNames) {
                    for (String field : fields) {
                        if (field.equalsIgnoreCase(key)) {
                            Object value = feature.getAttribute(key);
                            map.put(field, value);
                        }
                    }
                    if (key.equalsIgnoreCase("admincode")) {
                        map.put("AdCode", feature.getAttribute(key));
                    }
                }
                map.put("lsid", feature.getAttribute(fieldUuid));
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                if (geom == null || geom.isEmpty()) {
                    log.warn("跳过空间范围为空的记录：" + feature.getAttributes());
                    continue;
                }
                if (transform != null)
                    geom = JTS.transform(geom, transform);

                Point center = geom.getCentroid();
                map.put("lng", center.getX());
                map.put("lat", center.getY());
                String geojson = geometryJSON.toString(geom);
                Map<String, Object> geo = new HashMap<>();
                JSONObject json = new JSONObject(geojson);
                if (json.getString("type").equalsIgnoreCase("point")) {
                    geo.put("lon", json.getJSONArray("coordinates").get(0));
                    geo.put("lat", json.getJSONArray("coordinates").get(1));
                    map.put("the_point", geo);
                } else {
                    geo.put("type", json.get("type"));
                    geo.put("coordinates", json.get("coordinates"));
                    map.put("geometry", wktWriter.write(geom));
                    map.put("the_shape", geo);
                }
                map.put("keywords", keywords);
                records.add(map);
            }
        }
        return records;
    }

    private static String getMappingSource() {
        JSONObject source = new JSONObject();
        source.put("dynamic", false).put("_all", new JSONObject()
                .put("analyzer", "ik_max_word")
                .put("search_analyzer", "ik_smart")
                .put("term_vector", "no").put("store", "yes"));
        JSONObject properties = new JSONObject();
        source.put("properties", properties);
        for (String field : fields) {
            if (field.toLowerCase().startsWith("name") || field.equalsIgnoreCase("address"))
                properties.put(field, new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word").put("search_analyzer", "ik_smart"));
            else
                properties.put(field, new JSONObject().put("type", "text").put("store", true));
        }
        properties.put("keywords", new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word").put("search_analyzer", "ik_smart"));
        properties.put("lng", new JSONObject().put("type", "float").put("store", true));
        properties.put("lat", new JSONObject().put("type", "float").put("store", true));
        properties.put("geometry", new JSONObject().put("type", "text").put("store", true));
        properties.put("the_point", new JSONObject().put("type", "geo_point"));
        properties.put("the_shape", new JSONObject().put("type", "geo_shape"));
        return source.toString();
    }
}
