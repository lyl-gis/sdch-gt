package edu.zju.gis;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/22
 */
public class PrintEpsg {
    public static void main(String[] args) throws FactoryException {
        CoordinateReferenceSystem crs1 = CRS.decode("EPSG:3857");
        CoordinateReferenceSystem crs2 = CRS.decode("EPSG:4326");
        MathTransform transform = CRS.findMathTransform(crs1, crs2);
        System.out.println(crs1);
        System.out.println(crs2);
        System.out.println(transform);
    }
}
