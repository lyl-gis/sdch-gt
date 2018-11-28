package edu.zju.gis.util;

import edu.zju.gis.config.CommonSetting;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.json.JSONObject;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/13
 */
public class ShapefileReader implements Closeable {
    private static ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
    private ShapefileDataStore dataStore;
    private SimpleFeatureSource featureSource;
    private CoordinateReferenceSystem crs;

    public ShapefileReader(File path, Charset charset) throws IOException {
        dataStore = (ShapefileDataStore) dataStoreFactory.createDataStore(path.toURI().toURL());
        dataStore.setCharset(charset);//设置编码，防止中文读取乱码
        featureSource = dataStore.getFeatureSource();
        crs = dataStore.getSchema().getCoordinateReferenceSystem();
    }

    @Override
    public void close() {
        dataStore.dispose();
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public SimpleFeatureType getSchema() {
        return featureSource.getSchema();
    }

    public List<String> getFields() {
        return featureSource.getSchema().getAttributeDescriptors().stream()
                .map(AttributeDescriptor::getLocalName).collect(Collectors.toList());
    }

    public Map<String, String> getFieldTypes() {
        Map<String, String> fieldTypes = new HashMap<>();
        featureSource.getSchema().getAttributeDescriptors()
                .forEach(o -> fieldTypes.put(o.getLocalName(), o.getType().getBinding().getName()));
        return fieldTypes;
    }

    public String getGeometryType() {
        return featureSource.getSchema().getGeometryDescriptor().getType().getName().getLocalPart();
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        return featureSource.getFeatures();
    }

    public String getMappingSource(List<String> fields) {//todo 可索引的列
        JSONObject source = new JSONObject();
        source.put("dynamic", false).put("_all", new JSONObject()
                .put("analyzer", "ik_max_word")
                .put("search_analyzer", "ik_smart")
                .put("term_vector", "no").put("store", "yes"));
        JSONObject properties = new JSONObject();
        source.put("properties", properties);
        Map<String, String> fieldTypes = getFieldTypes();
        String dateFormat = CommonSetting.getInstance().get("es.dateformat");
        for (Map.Entry<String, String> entry : fieldTypes.entrySet()) {
            for (String field : fields)
                if (field.equalsIgnoreCase(entry.getKey())) {
                    JSONObject property = new JSONObject();
                    properties.put(field, property);
                    switch (entry.getValue()) {
                        case "short":
                        case "java.lang.Short":
                            property.put("type", "short").put("store", true);
                            break;
                        case "int":
                        case "java.lang.Integer":
                            property.put("type", "integer").put("store", true);
                            break;
                        case "long":
                        case "java.lang.Long":
                            property.put("type", "long").put("store", true);
                            break;
                        case "float":
                        case "java.lang.Float":
                            property.put("type", "float").put("store", true);
                            break;
                        case "double":
                        case "java.lang.Double":
                        case "java.math.BigDecimal":
                            property.put("type", "double").put("store", true);
                            break;
                        case "byte":
                        case "java.lang.Byte":
                            property.put("type", "byte").put("store", true);
                            break;
                        case "char":
                        case "java.lang.Character":
                            property.put("type", "text").put("store", true);
                            break;
                        case "boolean":
                        case "java.lang.Boolean":
                            property.put("type", "boolean").put("store", true);
                            break;
                        case "java.lang.String":
                            property.put("type", "text").put("store", true).put("analyzer", "ik_max_word")
                                    .put("search_analyzer", "ik_smart");
                            break;
                        case "java.util.Date":
                            property.put("type", "date").put("store", true).put("format", dateFormat);
                            break;
                    }
                }

        }
        properties.put("keywords", new JSONObject().put("type", "text").put("store", true).put("analyzer", "ik_max_word")
                .put("search_analyzer", "ik_smart"));
        properties.put("lng", new JSONObject().put("type", "float").put("store", true));
        properties.put("lat", new JSONObject().put("type", "float").put("store", true));

        properties.put("the_geom", new JSONObject().put("type", getGeometryType().contains("Point") ? "geo_point" : "geo_shape"));
        return source.toString();
    }
}
