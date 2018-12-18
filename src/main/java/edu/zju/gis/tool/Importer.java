package edu.zju.gis.tool;

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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
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
    private static List<String> fields = Arrays.asList("id", "lsid", "name", "address", "telephone", "admincode", "zipcode", "kind", "type"
            , "clasid", "addcode", "abbreviation", "priority", "grade", "name1", "name2", "name3", "name4", "name5", "name6", "name7", "name8", "name9");

    static String[] nameFields = {"name", "name1", "name2", "name3", "name4", "name5", "name6", "name7", "name8", "name9"};

    public static void main(String[] args) throws IOException, FactoryException, TransformException {
        if (args.length < 3) {
            System.out.println("Usage:" +
                    "\n\t 0 - config file(.properties)" +
                    "\n\t 1 - shapefile(.shp)" +
                    "\n\t 2 - 默认中文编码类型（默认GBK，带有.cpg的数据会使用其原有编码）");
            return;
        }
        String pConfig = args[0];
        String pShps = args[1];
        String pCharset = args[2];
        Charset charset = Charset.forName(pCharset.isEmpty() ? "GBK" : pCharset);
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
        helper.createIfNotExist(indexName, shards, replicas);
        if (!helper.exists(indexName, "_doc")) {
            String mappingSource = getMappingSource();
            helper.putMapping(indexName, "_doc", mappingSource);
        }
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
            int error = 0;
            try (ShapefileReader reader = new ShapefileReader(rootFile, mCharset)) {
                SimpleFeatureIterator iterator = reader.getFeatures().features();
                GeometryJSON geometryJSON = new GeometryJSON(9);
                CoordinateReferenceSystem crs = reader.getCrs();
                MathTransform transform = null;
                if (crs instanceof ProjectedCRS)
                    transform = CRS.findMathTransform(crs, ((ProjectedCRS) crs).getBaseCRS());//投影坐标转换为大地坐标系，cgcs2000和wgs84的微小误差可忽略
                List<String> attrNames = reader.getFields();
                List<Map<String, Object>> records = new ArrayList<>();
                int count = 0;
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    Map<String, Object> map = new HashMap<>();
                    map.put("doc_id", fieldUuid == null ? UUID.randomUUID() : feature.getAttribute(fieldUuid).toString());
                    for (String key : attrNames) {
                        for (String field : fields) {
                            if (field.equalsIgnoreCase(key)) {
                                Object value = feature.getAttribute(key);
                                map.put(field, value);
                                break;
                            }
                        }
                        if (key.equalsIgnoreCase("adcode")) {
                            map.put("admincode", feature.getAttribute(key));
                        }
                        if (key.equalsIgnoreCase("abbreviati"))
                            map.put("abbreviation", feature.getAttribute(key));
                    }
                    map.put("lsid", feature.getAttribute(fieldUuid));
                    Geometry geom = (Geometry) feature.getDefaultGeometry();
                    if (geom == null || geom.isEmpty()) {
                        log.warn("跳过空间范围为空的记录：" + feature.getAttributes());
                        continue;
                    }
                    if (transform != null)
                        geom = JTS.transform(geom, transform);

                    String geojson = geometryJSON.toString(geom);
                    JSONObject json = new JSONObject(geojson);
                    Map<String, Object> geo;
                    if (json.getString("type").equalsIgnoreCase("point")) {
                        geo = new HashMap<>();
                        geo.put("lon", json.getJSONArray("coordinates").get(0));
                        geo.put("lat", json.getJSONArray("coordinates").get(1));
                        map.put("the_point", geo);
                    } else {
                        geo = new HashMap<>();
                        geo.put("type", json.get("type"));
                        geo.put("coordinates", json.get("coordinates"));
                        map.put("the_shape", geo);
                    }
                    map.put("dtype", indexType);
                    records.add(map);
                    if (records.size() >= 1000) {
                        error += helper.publish(indexName, "_doc", records);
                        records.clear();
                        count += 1000;
                        System.out.println("已入库" + (count - error) + "条记录...");
                    }
                }
                if (records.size() > 0) {
                    error += helper.publish(indexName, "_doc", records);
                    count += records.size();
                }
                System.out.println("共入库" + (count - error) + "条记录");
            }
//            List<Map<String, Object>> records = getRecords(indexName, indexType, rootFile, mCharset, fields, fieldUuid, pKeywords);
//            if (cluster)
//                records = execCluster(records);
//            int error = helper.publish(indexName, "_doc", records);
            System.out.println("入库失败" + error + "条");
        } else {
            System.out.println("输入数据必须是一个shapefile文件（.shp）");
        }
    }

    private static String getMappingSource() {
        JSONObject source = new JSONObject();
        source.put("dynamic", false).put("_all", new JSONObject().put("enabled", false));
        JSONObject properties = new JSONObject();
        source.put("properties", properties);
        for (String field : fields) {
            if (field.startsWith("name") || field.equals("address") || field.equals("abbreviation"))
                properties.put(field, new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word").put("search_analyzer", "ik_smart"));
            else if (field.equals("priority"))
                properties.put(field, new JSONObject().put("type", "integer").put("store", true));
            else
                properties.put(field, new JSONObject().put("type", "text").put("store", true));
        }
        properties.put("the_point", new JSONObject().put("type", "geo_point"));//cannot set property `store`
        properties.put("the_shape", new JSONObject().put("type", "geo_shape"));
        properties.put("dtype", new JSONObject().put("type", "keyword").put("store", true));
        return source.toString();
    }

    private static List<Map<String, Object>> execCluster(List<Map<String, Object>> entities) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(Math.pow(10, 7)));

        Map<List<String>, Map<String, Object>> namesToEntity = new HashMap<>();
        for (Map<String, Object> m : entities) {
            namesToEntity.put(getNames(m), m);
        }
        for (List<String> n1 : namesToEntity.keySet()) {
            for (List<String> n2 : namesToEntity.keySet()) {
                if (n1 != n2) {
                    List<String> intersects = new ArrayList<>(n1);
                    intersects.retainAll(n2);
                    if (!intersects.isEmpty()) {
                        Map<String, Object> m1 = namesToEntity.get(n1);
                        Map<String, Object> m2 = namesToEntity.get(n2);
                        String wkt = mergeIfAdjacent(m1, m2);
                        if (!wkt.isEmpty()) {
                            n2.retainAll(n1);
                            n1.addAll(n2);
                            m1.put("the_shape", wkt);
                        }
//                        if (isSame(m1, m2)) {
//                            Geometry geom1 = (Geometry) m1.get("geom");//不可以直接union，会存在精度问题
//                            Geometry geom2 = (Geometry) m2.get("geom");
//
//                        }
                    }
                }
            }
        }
        return entities;
    }

    private static List<String> getNames(Map<String, Object> map) {
        List<String> names = new ArrayList<>();
        for (String name : nameFields) {
            if (map.containsKey(name))
                names.add(map.get(name).toString());
        }
        return names;
    }

    static final double EPS = 0.0000002;

    private static String mergeIfAdjacent(Map<String, Object> m1, Map<String, Object> m2) {
        String wkt1 = m1.get("the_shape").toString();
        String ss1 = wkt1.substring(wkt1.indexOf("(") + 1, wkt1.indexOf(")"));
        String[] sp11 = ss1.substring(0, ss1.indexOf(",")).split(" ");
        String[] sp12 = ss1.substring(ss1.lastIndexOf(",") + 1).split(" ");
        double[] p11 = new double[2];
        p11[0] = Double.parseDouble(sp11[0]);
        p11[1] = Double.parseDouble(sp11[1]);
        double[] p12 = new double[2];
        p12[0] = Double.parseDouble(sp12[0]);
        p12[1] = Double.parseDouble(sp12[1]);
        String wkt2 = m2.get("the_shape").toString();
        String ss2 = wkt2.substring(wkt2.indexOf("(") + 1, wkt2.indexOf(")"));
        String[] sp21 = ss2.substring(0, ss2.indexOf(",")).split(" ");
        String[] sp22 = ss2.substring(ss2.lastIndexOf(",") + 1).split(" ");
        double[] p21 = new double[2];
        p21[0] = Double.parseDouble(sp21[0]);
        p21[1] = Double.parseDouble(sp21[1]);
        double[] p22 = new double[2];
        p22[0] = Double.parseDouble(sp22[0]);
        p22[1] = Double.parseDouble(sp22[1]);
        StringBuilder wkt = new StringBuilder();
        if ((Math.abs(p11[0] - p21[0]) < EPS && Math.abs(p11[1] - p21[1]) < EPS)) {
            String[] pairs = ss1.split(",");
            wkt.append("LINESTRING(");
            for (int i = pairs.length - 1; i >= 0; i--)
                wkt.append(pairs[i]).append(",");
            wkt.append(ss2.substring(ss2.indexOf(",") + 1)).append(")");
        } else if ((Math.abs(p11[0] - p22[0]) < EPS && Math.abs(p11[1] - p22[1]) < EPS)) {
            wkt.append(wkt2).delete(wkt.length() - 1, wkt.length());
            wkt.append(wkt1.substring(wkt1.indexOf(",")));
        } else if ((Math.abs(p12[0] - p21[0]) < EPS && Math.abs(p12[1] - p21[1]) < EPS)) {
            wkt.append(wkt1).delete(wkt.length() - 1, wkt.length());
            wkt.append(wkt2.substring(wkt2.indexOf(",")));
        } else if ((Math.abs(p12[0] - p22[0]) < EPS && Math.abs(p12[1] - p22[1]) < EPS)) {
            String[] pairs = ss2.split(",");
            wkt.append("LINESTRING(").append(ss1);
            for (int i = pairs.length - 1; i > 0; i--) {
                wkt.append(",").append(pairs[i]);
            }
            wkt.append(")");
        }
        return wkt.toString();
    }
}
