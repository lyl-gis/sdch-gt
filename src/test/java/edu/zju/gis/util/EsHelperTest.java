package edu.zju.gis.util;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.geotools.data.FeatureReader;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONObject;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/26
 */
public class EsHelperTest {
    @Test
    public void testESHealth() {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        System.out.println(helper.getClusterHealth().toString());
    }

    @Test
    public void testGetGeom() {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        GetResponse response = helper.getClient().prepareGet("sdmap", "fe_zq", "XZQH7").get();
        System.out.println(response.getSourceAsString());
    }

    @Test
    public void testSearchGeom() throws IOException {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        SearchResponse response = helper.getClient().prepareSearch("sdmap").setTypes("fe_zq").setQuery(QueryBuilders.idsQuery().addIds("XZQH7")).get();
        Map<String, Object> doc = response.getHits().getAt(0).getSourceAsMap();
        JSONObject json = new JSONObject(doc);
        JSONObject geojson = json.getJSONObject("the_shape");
        GeometryJSON geometryJSON = new GeometryJSON();
        Geometry geom = geometryJSON.read(geojson.toString());
        System.out.println(geom.toText());
    }

    @Test
    public void deleteByType() {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        long count = helper.delete("sdmap", "fe_zq");
        System.out.println(count);
    }

    @Test
    public void deleteBySearch() {
        Client client = ElasticSearchHelper.getInstance().getClient();
//        SearchResponse response = client.prepareSearch("sdmap").setTypes("_doc")
//                .setQuery(QueryBuilders.matchQuery("dtype","f_poi"))
//                .setSize(20).get();
//        System.out.println(response.getHits().getTotalHits());

        long size = DeleteByQueryAction.INSTANCE.newRequestBuilder(client).source("sdmap2")
                .filter(QueryBuilders.matchQuery("dtype", "f_poi")).get().getDeleted();
        System.out.println(size);
    }

    @Test
    public void putMap() throws IOException {
        ElasticSearchHelper helper = ElasticSearchHelper.getInstance();
        helper.createIfNotExist("sdmap", 4, 0);
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("dynamic", false)
                .startObject("_all").field("enabled", false).endObject()
                .startObject("properties")
                .startObject("name32").field("type", "text").field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").endObject()
                .endObject()
                .endObject();

        AcknowledgedResponse response = helper.getClient().admin().indices()
                .preparePutMapping("sdmap").setType("_doc").setSource(builder).get();
        System.out.println(response);
    }

    @Test
    public void readshp() throws IOException {
        String shp = "F:\\Project\\山东国土测绘院\\shiti\\BOUA6乡镇面.shp";
        ShapefileDataStore dataStore = new ShapefileDataStore(new File(shp).toURI().toURL());
        dataStore.setCharset(Charset.forName("GBK"));
        SimpleFeatureIterator iterator = dataStore.getFeatureSource().getFeatures().features();
        if (iterator.hasNext())
            System.out.println(iterator.next().getAttributes());
    }

    @Test
    public void readshp2() throws IOException {
        String shp = "F:\\Project\\山东国土测绘院\\shiti\\BOUA6乡镇面.shp";
        ShapefileDataStore dataStore = new ShapefileDataStore(new File(shp).toURI().toURL());
        dataStore.setCharset(Charset.forName("UTF-8"));
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader();
        if (reader.hasNext())
            System.out.println(reader.next().getAttributes());
    }
}
