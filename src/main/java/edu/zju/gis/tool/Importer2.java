package edu.zju.gis.tool;

import com.vividsolutions.jts.geom.Geometry;
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
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/13
 */
public class Importer2 {
    private static final Logger log = LogManager.getLogger(Importer.class);

    public static void main(String[] args) throws IOException, FactoryException, TransformException {
        if (args.length != 4) {
            System.out.println("Usage:" +
                    "\n\t 0 - config file(.properties)" +
                    "\n\t 1 - shapefile或包含shapefile的文件夹" +
                    "\n\t 2 - 默认中文编码类型（默认GBK，带有.cpg的数据会使用其原有编码）");
            return;
        }
        String pConfig = args[0];
        String pShps = args[1];
        String pCharset = args[2];
        Charset charset = Charset.forName(pCharset.isEmpty() ? "GBK" : pCharset);
        String pKeywords = args[3];
        Properties configs = new Properties();
        InputStream is = Files.newInputStream(Paths.get(pConfig));
        configs.load(is);
        List<String> fields = Arrays.stream(configs.getProperty("fields.all").split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        String indexName = configs.getProperty("index.name");
        String indexType = configs.getProperty("index.type");
        String fieldUuid = configs.getProperty("fields.uuid");
        helper.createIndexIfNotExist(indexName);
        File rootFile = new File(pShps);
        if (rootFile.isFile()) {
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
            Files.walkFileTree(rootFile.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!file.getFileName().toString().toLowerCase().endsWith(".shp")) {
                        try {
                            File cpg = new File(pShps.replace(".shp", ".cpg"));
                            Charset mCharset = charset;
                            if (cpg.exists()) {
                                List<String> lines = Files.readAllLines(cpg.toPath());
                                if (!lines.isEmpty()) {
                                    mCharset = Charset.forName(lines.get(0));
                                }
                            }
                            List<Map<String, Object>> records = getRecords(indexName, indexType, file.toFile(), mCharset, fields, fieldUuid, pKeywords);
                            int error = helper.publish(indexName, indexType, records);
                            System.out.println(error);
                        } catch (IOException | FactoryException | TransformException e) {
                            log.error("文件`" + file.toString() + "`读取异常", e);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static List<Map<String, Object>> getRecords(String indexName, String indexType, File shpFile, Charset charset, List<String> fields
            , String fieldUuid, String keywords) throws IOException, FactoryException, TransformException {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        List<Map<String, Object>> records = new ArrayList<>();
        try (ShapefileReader reader = new ShapefileReader(shpFile, charset)) {
            if (!helper.exists(new String[]{indexName}, indexType)) {
                String mappingSource = getMappingSource(reader.getGeometryType());
                helper.mappingBuilder(indexName).mappingfromString(indexType, mappingSource);
            }
            SimpleFeatureIterator iterator = reader.getFeatures().features();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GeometryJSON geometryJSON = new GeometryJSON(9);
            CoordinateReferenceSystem crs = reader.getCrs();
            MathTransform transform = null;
            if (crs instanceof ProjectedCRS)
                transform = CRS.findMathTransform(crs, ((ProjectedCRS) crs).getBaseCRS());//投影坐标转换为大地坐标系，cgcs2000和wgs84的微小误差可忽略
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Map<String, Object> map = new HashMap<>();
                map.put("doc_id", fieldUuid == null ? UUID.randomUUID() : feature.getAttribute(fieldUuid).toString());
                for (String key : fields) {
                    Object value = feature.getAttribute(key);
                    if (value instanceof Date)
                        map.put(key, sdf.format((Date) value));
                    else
                        map.put(key, value);
                }
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                if (geom == null || geom.isEmpty())
                    continue;//todo 跳过空记录
                if (transform != null)
                    geom = JTS.transform(geom, transform);
                String geojson = geometryJSON.toString(geom);
                Map<String, Object> geo = new HashMap<>();
                JSONObject json = new JSONObject(geojson);
                if (json.getString("type").equalsIgnoreCase("point")) {
                    geo.put("lon", json.getJSONArray("coordinates").get(0));
                    geo.put("lat", json.getJSONArray("coordinates").get(1));
                } else {
                    geo.put("type", json.get("type"));
                    geo.put("coordinates", json.get("coordinates"));
                }
                map.put("the_geom", geo);
                map.put("keywords", keywords);
                records.add(map);
            }
        }
        return records;
    }

    private static String getMappingSource(String ogcGeometryType) {
        JSONObject source = new JSONObject();
        source.put("dynamic", false).put("_all", new JSONObject()
                .put("analyzer", "ik_max_word")
                .put("search_analyzer", "ik_smart")
                .put("term_vector", "no").put("store", "yes"));
        JSONObject properties = new JSONObject();
        source.put("properties", properties);
        String dateFormat = CommonSetting.getInstance().get("es.dateformat");
        List<String> fields = Arrays.asList("id", "lsid", "name", "address", "telephone"
                , "AdCode", "PostCode", "kind", "type", "CLASID", "ENTITY6", "navid", "city", "grade", "level", "lng", "lat", "geometry");
        for (String field : fields) {
            if (field.toLowerCase().startsWith("name") || field.equalsIgnoreCase("address"))
                properties.put(field, new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word").put("search_analyzer", "ik_smart"));
            else
                properties.put(field, new JSONObject().put("type", "text").put("store", true).put("search_analyzer", "ik_smart"));
        }
        for (int i = 1; i < 10; i++) {
            properties.put("NAME" + i, new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word").put("search_analyzer", "ik_smart"));
        }
        properties.put("keywords", new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word").put("search_analyzer", "ik_smart"));
        properties.put("lng", new JSONObject().put("type", "float").put("store", true));
        properties.put("lat", new JSONObject().put("type", "float").put("store", true));
        properties.put("geometry", new JSONObject().put("type", "text").put("store", true));
        properties.put("the_geom", new JSONObject().put("type", ogcGeometryType.contains("Point") ? "geo_point" : "geo_shape"));
        return source.toString();
    }
}
