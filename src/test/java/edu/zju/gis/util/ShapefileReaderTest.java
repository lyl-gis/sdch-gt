package edu.zju.gis.util;

import com.vividsolutions.jts.geom.Coordinate;
import edu.zju.gis.tool.Importer;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
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
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\xzm.properties", "F:\\Project\\山东国土测绘院\\shiti\\BOUA6乡镇面.shp"
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
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\shuixi.properties", "F:\\Project\\山东国土测绘院\\shiti\\shuixi.shp"
                , "GBK", ""});
    }

    @Test
    public void testInsertZhengqu() throws IOException, FactoryException, TransformException {
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\zq.properties", "F:\\Project\\山东国土测绘院\\shiti\\zhengqu.shp"
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
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\fjmsq.properties", "F:\\Project\\山东国土测绘院\\shiti\\风景名胜区.shp"
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
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\gjjslgy.properties", "F:\\Project\\山东国土测绘院\\shiti\\国家级森林公园.shp"
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
        Importer.main(new String[]{"F:\\Project\\山东国土测绘院\\conf\\sjkfq.properties", "F:\\Project\\山东国土测绘院\\shiti\\省级开发区.shp"
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

    //    @Test
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

}
