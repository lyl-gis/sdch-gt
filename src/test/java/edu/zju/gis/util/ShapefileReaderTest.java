package edu.zju.gis.util;

import edu.zju.gis.tool.Importer;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.builders.EnvelopeBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/13
 */
public class ShapefileReaderTest {

    @Test
    public void testInsertXzm() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\xzm.properties", "E:\\data\\shiti\\BOUA6乡镇面.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertDaolu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\daolu.properties", "E:\\data\\实体查询\\daolu.shp"
                , "GBK", ""});
    }

    //    @Test
    public void testInsertPOI() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\poi.properties", "E:\\data\\实体查询\\poi.shp"
                , "UTF-8", ""});
    }

    @Test
    public void testInsertShuixi() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\shuixi.properties", "E:\\data\\shiti\\shuixi.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShuixixian() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\shuixijiegouxian.properties", "E:\\data\\实体查询_水系线_public\\京杭.shp"
                , "GBK", ""});
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\shuixijiegouxian.properties", "E:\\data\\实体查询_水系线_public\\shuixi_除京杭.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertZhengqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\zq.properties", "E:\\data\\shiti\\zhengqu.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertBaoshuiqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\bsq.properties", "E:\\data\\实体查询\\保税区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertDizhigongyuan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\dzgy.properties", "E:\\data\\实体查询\\地质公园.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertFengjingmingshengqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\fjmsq.properties", "E:\\data\\shiti\\风景名胜区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojiajigaoxinqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\gjjgxq.properties", "E:\\data\\实体查询\\国家级高新区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojiajikaifaqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\gjjkfq.properties", "E:\\data\\实体查询\\国家级开发区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojiasenlingongyuan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\gjjslgy.properties", "E:\\data\\shiti\\国家级森林公园.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertGuojialvyoudujiaqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\gjlydjq.properties", "E:\\data\\实体查询\\国家旅游度假区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShengjigaoxinqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\sjgxq.properties", "E:\\data\\实体查询\\省级高新区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShengjikaifaqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\sjkfq.properties", "E:\\data\\shiti\\省级开发区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShengjisenlingongyuan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\sjslgy.properties", "E:\\data\\实体查询\\省级森林公园.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertShijieziranwenhuayichan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\sjzrwhyc.properties", "E:\\data\\实体查询\\世界自然文化遗产.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertWenhuayichan() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\whyc.properties", "E:\\data\\实体查询\\文化遗产.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertZiranbaohuqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\zrbhq.properties", "E:\\data\\实体查询\\自然保护区.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertDmdzcj() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"C:\\Users\\SDGT\\Desktop\\sdch-gt\\src\\main\\resources\\conf\\dmdzcj.properties", "E:\\data\\zzl\\POI_20181126_public\\merge.shp"
                , "GBK", ""});
    }

    //    @Test
    public void testRangeBox() throws IOException {
        GeoShapeQueryBuilder queryBuilder = QueryBuilders.geoDisjointQuery("the_geom", new EnvelopeBuilder(
                new Coordinate(117, 40),
                new Coordinate(125, 30)
        ));
        SearchResponse searchResponse = ElasticSearchHelper.getInstance().getClient().prepareSearch("sdmap_framework")
                .setQuery(queryBuilder).get();
        System.out.println(searchResponse);
        System.err.println(searchResponse.getHits().getTotalHits());
    }

}
