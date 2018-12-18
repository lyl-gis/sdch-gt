package edu.zju.gis.util;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
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
    private ShapefileDataStore dataStore;
    private SimpleFeatureSource featureSource;
    private CoordinateReferenceSystem crs;

    public ShapefileReader(File path, Charset charset) throws IOException {
        dataStore = new ShapefileDataStore(path.toURI().toURL());
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
}
