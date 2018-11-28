package edu.zju.gis.util;

import com.vividsolutions.jts.geom.*;
import edu.zju.gis.tool.Importer;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.json.JSONObject;
import org.junit.Test;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/13
 */
public class ShapefileReaderTest {

    @Test
    public void testInsertXzm() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\xzm.properties", "F:\\Project\\山东国土测绘院\\实体查询\\BOUA6乡镇面.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertDaolu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\daolu.properties", "F:\\Project\\山东国土测绘院\\实体查询\\daolu.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertPOI() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\poi.properties", "F:\\Project\\山东国土测绘院\\实体查询\\poi.shp"
                , "UTF-8", ""});
    }

    @Test
    public void testInsertShuixi() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\shuixi.properties", "F:\\Project\\山东国土测绘院\\实体查询\\shuixi.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertZhengqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\zq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\zhengqu.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertBaoshuiqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\bsq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\保税区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertDizhigongyuan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\dzgy.properties", "F:\\Project\\山东国土测绘院\\实体查询\\地质公园.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertFengjingmingshengqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\fjmsq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\风景名胜区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojiajigaoxinqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\gjjgxq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\国家级高新区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojiajikaifaqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\gjjkfq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\国家级开发区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojiasenlingongyuan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\gjjslgy.properties", "F:\\Project\\山东国土测绘院\\实体查询\\国家级森林公园.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojialvyoudujiaqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\gjlydjq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\国家旅游度假区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShengjigaoxinqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\sjgxq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\省级高新区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShengjikaifaqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\sjkfq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\省级开发区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShengjisenlingongyuan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\sjslgy.properties", "F:\\Project\\山东国土测绘院\\实体查询\\省级森林公园.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShijieziranwenhuayichan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\sjzrwhyc.properties", "F:\\Project\\山东国土测绘院\\实体查询\\世界自然文化遗产.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertWenhuayichan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\whyc.properties", "F:\\Project\\山东国土测绘院\\实体查询\\文化遗产.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertZiranbaohuqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\zrbhq.properties", "F:\\Project\\山东国土测绘院\\实体查询\\自然保护区.shp"
                , "GBK", ""});
    }

    @Test
    public void testRangeBox() throws IOException {
        GeoShapeQueryBuilder queryBuilder = QueryBuilders.geoDisjointQuery("the_geom", ShapeBuilders.newEnvelope(
                new Coordinate(117, 40),
                new Coordinate(125, 30)
        ));
        SearchResponse searchResponse = ElasticSearchHelper.getInstance().getClient().prepareSearch("sdmap_framework")
                .setQuery(queryBuilder).get();
        System.out.println(searchResponse);
        System.err.println(searchResponse.getHits().getTotalHits());
    }

    @Test
    public void testReadXzm() throws IOException, FactoryException, TransformException {
        String path = "F:\\Project\\山东国土测绘院\\实体查询\\BOUA6乡镇面.shp";
        String configFile = "C:\\Users\\yanlo\\Desktop\\xzfwm.properties";
        List<Map<String, Object>> records = getRecords(path, configFile, "GBK");
//        System.out.println(records);
        Files.write(Paths.get("C:\\Users\\yanlo\\Desktop\\xiangzhenmian.txt"), records.stream().map(Object::toString).collect(Collectors.toList()));
    }

    private List<Map<String, Object>> getRecords(String path, String configFile, String charset) throws TransformException, IOException, FactoryException {
        List<Map<String, Object>> records = new ArrayList<>();
        try (ShapefileReader reader = new ShapefileReader(new File(path), Charset.forName(charset))) {
            SimpleFeatureIterator iterator = reader.getFeatures().features();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            GeometryJSON geometryJSON = new GeometryJSON(9);
            CoordinateReferenceSystem crs = reader.getCrs();
            MathTransform transform = null;
            if (crs instanceof ProjectedCRS)
                transform = CRS.findMathTransform(crs, ((ProjectedCRS) crs).getBaseCRS());//投影坐标转换为大地坐标系，cgcs2000和wgs84的微小误差可忽略
            Properties configs = new Properties();
            InputStream is = Files.newInputStream(Paths.get(configFile));
            configs.load(is);
            List<String> fields = Arrays.stream(configs.getProperty("fields.all").split(",")).map(String::trim).collect(Collectors.toList());
            String fieldUuid = configs.getProperty("fields.uuid");
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
                    continue;

                if (geom instanceof Point) {
                    Point point = (Point) geom;
                    GeometryFactory geometryFactory = new GeometryFactory();
                    point = geometryFactory.createPoint(new Coordinate(point.getX(), point.getY()));
                } else if (geom instanceof LineString) {

                } else if (geom instanceof Polygon) {

                } else /*GeometryCollection*/ {

                }


                if (transform != null)
                    geom = JTS.transform(geom, transform);
                String geojson = geometryJSON.toString(geom);
                JSONObject json;
                try {
                    json = new JSONObject(geojson);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                Map<String, Object> geo = new HashMap<>();
                geo.put("type", json.get("type"));
                geo.put("coordinates", json.get("coordinates"));
                map.put("the_geom", geo);
                map.put("keywords", "");
                records.add(map);
            }
        }
        return records;
    }

    @Test
    public void testCreatePoint2D() {
        GeometryFactory geometryFactory = new GeometryFactory();
        GeometryJSON geometryJSON = new GeometryJSON();
        Point point = geometryFactory.createPoint(new Coordinate(120, 49, Double.NaN));
        System.out.println(geometryJSON.toString(point));
    }

    @Test
    public void tt() {
        String geojson = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[122.042356544,37.402319783],[122.042401955,37.40229683],[122.042384023,37.402301255],[122.042412783,37.402291365],[122.042401955,37.40229683],[122.04249756,37.40227328],[122.042551151,37.402267686],[122.042526901,37.402216159],[122.042454333,37.402164986],[122.04235792,37.402136513],[122.042283388,37.402128061],[122.042212446,37.402129398],[122.042107065,37.402106836],[122.042020292,37.402100165],[122.041928139,37.402107329],[122.041837018,37.402147107],[122.041788135,37.402186449],[122.041676771,37.402227192],[122.041505022,37.402253591],[122.041390412,37.40227162],[122.041359655,37.402276473],[122.04125645,37.40226854],[122.041135175,37.402239856],[122.04104475,37.402222304],[122.040961475,37.402196937],[122.040914822,37.402173729],[122.040923653,37.402142306],[122.040971508,37.402108939],[122.041137609,37.40204948],[122.041208894,37.402015569],[122.041233663,37.401991004],[122.0413335,37.401969672],[122.041394357,37.4019619],[122.041493867,37.401949182],[122.041642349,37.401906849],[122.041803091,37.401896085],[122.041939539,37.401914018],[122.042024945,37.401918803],[122.0420672,37.401916878],[122.042072754,37.401888432],[122.042049443,37.401838416],[122.041978333,37.40177938],[122.041901538,37.401761937],[122.041818441,37.401759416],[122.041689484,37.401747542],[122.041598091,37.401769715],[122.041541091,37.401782015],[122.041472606,37.401777355],[122.041417033,37.401750312],[122.041407664,37.401714635],[122.041431494,37.401684848],[122.041556707,37.401623586],[122.04161393,37.401592169],[122.041656677,37.401554649],[122.041683441,37.401517381],[122.041670168,37.401491408],[122.041620361,37.401495157],[122.041499359,37.401518939],[122.041399969,37.401521508],[122.041387546,37.401521823],[122.04130844,37.40150247],[122.041178691,37.401439263],[122.041125202,37.401363147],[122.041113296,37.401298583],[122.04111711,37.401265542],[122.041121769,37.401224441],[122.041166241,37.401162186],[122.041296299,37.401090108],[122.041406202,37.401026785],[122.041485037,37.400981371],[122.041676532,37.400879803],[122.041805574,37.400813321],[122.041825636,37.400785758],[122.041844624,37.400770897],[122.041915477,37.400775575],[122.041992348,37.40078405],[122.042164768,37.400779365],[122.042269697,37.400763316],[122.042332014,37.400736824],[122.042371644,37.400649039],[122.042430768,37.400506689],[122.042419984,37.400466883],[122.042383493,37.400460369],[122.04237408,37.400458663],[122.042321711,37.400476247],[122.042296139,37.400530412],[122.042257118,37.400571703],[122.042204527,37.400608025],[122.042107134,37.400622253],[122.041966693,37.400624171],[122.041896421,37.400611631],[122.041849278,37.400588403],[122.041810881,37.400582111],[122.041724805,37.400590449],[122.041548501,37.400605948],[122.041411248,37.400631491],[122.041405439,37.400632582],[122.041232726,37.400696839],[122.041190068,37.400729118],[122.041123296,37.400734581],[122.041048599,37.400738875],[122.041006077,37.400758407],[122.040970467,37.400823768],[122.040949033,37.400842447],[122.040895014,37.400889519],[122.040781206,37.400938098],[122.040702595,37.400989221],[122.040655934,37.401039463],[122.040630958,37.401121338],[122.040625006,37.401218748],[122.04061853,37.401321776],[122.040602818,37.40137598],[122.040551864,37.40139545],[122.040472447,37.401398966],[122.040397779,37.401397263],[122.04033786,37.401419668],[122.040291746,37.401428299],[122.040249787,37.401403981],[122.04023833,37.401342436],[122.040250716,37.401220711],[122.040271266,37.401082194],[122.04029904,37.40096324],[122.040292915,37.400929448],[122.040273656,37.400892577],[122.040239427,37.400893455],[122.040217771,37.400894018],[122.040206093,37.400901993],[122.040198812,37.400906994],[122.040178527,37.4009479],[122.040169426,37.400966354],[122.040130016,37.401033513],[122.040068896,37.401150726],[122.040048567,37.401196297],[122.039989885,37.401304896],[122.03993813,37.401388822],[122.039912529,37.401448227],[122.039903073,37.401524611],[122.040005361,37.401974398],[122.040054553,37.402204921],[122.040087641,37.402337108],[122.040135484,37.402376081],[122.04016523,37.402402912],[122.040210868,37.402393527],[122.040296841,37.402392317],[122.040370048,37.402395886],[122.040423268,37.402421777],[122.040549962,37.402431009],[122.04069742,37.402465879],[122.04077754,37.40251785],[122.040875295,37.402550451],[122.040965587,37.402574775],[122.040984981,37.402601879],[122.040990451,37.40265328],[122.040983261,37.402739431],[122.041002744,37.402758695],[122.041129258,37.402779543],[122.041242442,37.402776277],[122.041297847,37.402741864],[122.041333536,37.40274214],[122.041361983,37.402756582],[122.041384224,37.402760615],[122.041480994,37.402778117],[122.041719188,37.402808846],[122.041881748,37.402837824],[122.041945081,37.402861913],[122.042001412,37.402883341],[122.042091972,37.402925649],[122.042169173,37.402989587],[122.042240685,37.403052752],[122.04231477,37.403062293],[122.042406785,37.403063013],[122.042437141,37.403039264],[122.042431286,37.402983002],[122.042371585,37.402910953],[122.042263446,37.402808168],[122.042129333,37.402718673],[122.042015254,37.40268106],[122.041898599,37.4026603],[122.041821891,37.402631976],[122.041766498,37.402594807],[122.041723927,37.40254502],[122.041750244,37.402508483],[122.041804129,37.402481174],[122.041997665,37.402401344],[122.04210611,37.402366575],[122.042216985,37.402364425],[122.042292633,37.402360132],[122.042356544,37.402319783]]]]}";
        int s = geojson.indexOf("\"type\":\"");
        int e = geojson.indexOf("\",\"", s);
        String type = geojson.substring(s + "\"type\":\"".length(), e);
        s = geojson.indexOf("\"coordinates\":");
        e = geojson.indexOf(",\"", s);
        if (e < 0)
            e = geojson.indexOf("}");
        String coor = geojson.substring(s + "\"coordinates\":".length(), e);
        System.out.println(type);
        System.out.println(coor);
    }
}
