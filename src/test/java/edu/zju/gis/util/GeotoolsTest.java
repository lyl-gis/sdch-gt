package edu.zju.gis.util;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.linemerge.LineMerger;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.Collection;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/12/05
 */
public class GeotoolsTest {

    @Test
    public void testGeometryCollection() throws ParseException {
        GeometryFactory factory = new GeometryFactory();
        Geometry points = factory.createMultiPointFromCoords(new Coordinate[]{new Coordinate(100, 35), new Coordinate(115, 36)});
        WKTReader reader = new WKTReader();
        Geometry polygons = reader.read("MULTIPOLYGON (((118.309873581 35.2283535, 118.30994606 35.228170395, 118.310091019 35.227842331, 118.310125351 35.227773666, 118.310136795 35.227743149, 118.310777664 35.227151871, 118.310857773 35.22709465, 118.3117733 35.226507187, 118.311918259 35.226488113, 118.313180923 35.226415634, 118.313241959 35.226465225, 118.313268661 35.226518631, 118.313318253 35.226629257, 118.313432693 35.227014542, 118.31347084 35.227151871, 118.313493729 35.227254868, 118.313497543 35.227304459, 118.313550949 35.227319717, 118.315412521 35.227247238, 118.315561295 35.227239609, 118.315660477 35.227231979, 118.317827225 35.226251602, 118.321977615 35.219163895, 118.321889877 35.212060928, 118.324430466 35.208158493, 118.334363937 35.206232071, 118.342775345 35.204809189, 118.347215652 35.204553604, 118.348093033 35.204515457, 118.347990036 35.204435349, 118.34768486 35.204030991, 118.347475052 35.203435898, 118.347402573 35.202207565, 118.347394943 35.202058792, 118.347356796 35.200609207, 118.347337723 35.197431564, 118.349977493 35.196878433, 118.355085373 35.189260483, 118.356283188 35.186662674, 118.35556221 35.18053627, 118.355550766 35.18047142, 118.355524063 35.180280685, 118.355081558 35.1802845, 118.345762253 35.18180275, 118.345468521 35.181833267, 118.345125198 35.181295395, 118.344552994 35.180330276, 118.344415665 35.179971695, 118.344324112 35.17940712, 118.344358444 35.179067612, 118.344438553 35.1788311, 118.344797134 35.178358078, 118.345140457 35.177942276, 118.345258713 35.177778244, 118.345315933 35.177530289, 118.345342636 35.177125931, 118.345331192 35.176721573, 118.345071793 35.17568779, 118.344770432 35.174726486, 118.344060898 35.172811508, 118.343839645 35.172475815, 118.343618393 35.172262192, 118.341489792 35.170537949, 118.337911606 35.168153763, 118.333108902 35.16850853, 118.328447342 35.167272568, 118.327444077 35.166109085, 118.319898605 35.153287888, 118.319555283 35.151453018, 118.315389633 35.148752213, 118.307401657 35.14581871, 118.300256729 35.148191452, 118.299142838 35.150224686, 118.293996811 35.151063919, 118.293699265 35.152803421, 118.291292191 35.158502579, 118.285554886 35.170225143, 118.284887314 35.171243668, 118.284013748 35.17253685, 118.283262253 35.173631668, 118.28272438 35.174310684, 118.282190323 35.174955368, 118.281064987 35.17606926, 118.279737473 35.177301407, 118.277456284 35.179136276, 118.276750565 35.17952919, 118.275163651 35.180418015, 118.272825241 35.181318283, 118.272199631 35.181467056, 118.269521713 35.182104111, 118.267621994 35.182523727, 118.265829086 35.182462692, 118.264699936 35.182279587, 118.263746262 35.182123184, 118.259504318 35.181001663, 118.258405685 35.180707932, 118.257001877 35.180330276, 118.255857468 35.179643631, 118.25512886 35.17883873, 118.255002975 35.178300858, 118.254594803 35.176599503, 118.253900528 35.175428391, 118.253389359 35.175237656, 118.253084183 35.175214767, 118.251394272 35.175096512, 118.248632431 35.175340652, 118.247488022 35.175802231, 118.244874954 35.177236557, 118.24164772 35.178342819, 118.239622116 35.178964615, 118.22999382 35.180421829, 118.227235794 35.180570602, 118.22627449 35.182142258, 118.227743149 35.190340042, 118.228391647 35.190145493, 118.228391647 35.191019058, 118.227991104 35.19226265, 118.224046707 35.197950363, 118.222810745 35.198213577, 118.222513199 35.198274612, 118.221769333 35.198400497, 118.220701218 35.198575974, 118.22029686 35.198629379, 118.219545364 35.198728561, 118.219110489 35.198785782, 118.218812943 35.198820114, 118.217927933 35.200368881, 118.217512131 35.201452255, 118.217039108 35.202684402, 118.216848373 35.208620071, 118.217092514 35.210325241, 118.217927933 35.211877823, 118.218858719 35.212156296, 118.222810745 35.212705612, 118.224157333 35.212659836, 118.230951309 35.216821671, 118.231172562 35.217508316, 118.23118782 35.217557907, 118.233453751 35.219423294, 118.237615585 35.220788956, 118.239305496 35.221017838, 118.240465164 35.221078873, 118.240541458 35.221063614, 118.241083145 35.220956802, 118.241773605 35.220819473, 118.242883682 35.220602036, 118.243558884 35.220449448, 118.244787216 35.221071243, 118.246171951 35.224386215, 118.24706459 35.226556778, 118.249765396 35.222875595, 118.256628036 35.22129631, 118.25860405 35.22161293, 118.264368057 35.222764969, 118.264406204 35.222818375, 118.267263412 35.226324081, 118.267381668 35.226320267, 118.267827988 35.226163864, 118.269105911 35.225606918, 118.269140244 35.225580215, 118.269556046 35.225046158, 118.270792007 35.22469902, 118.27089119 35.224657059, 118.275327682 35.222055435, 118.275335312 35.221998215, 118.275312424 35.221944809, 118.274785995 35.220838547, 118.273786545 35.218568802, 118.273794174 35.218500137, 118.274267197 35.218103409, 118.277132034 35.216550827, 118.277296066 35.216493607, 118.277734756 35.216459274, 118.279722214 35.216398239, 118.280267715 35.216405869, 118.286024094 35.216676712, 118.286649704 35.216836929, 118.289129257 35.219129562, 118.296510696 35.221025467, 118.300989151 35.220991135, 118.302679062 35.221921921, 118.306818008 35.224287033, 118.306985855 35.228006363, 118.308168411 35.228330612, 118.309873581 35.2283535)))");
        GeometryCollection collection = new GeometryCollection(new Geometry[]{points, polygons}, factory);
        System.out.println(collection.toText());
    }

    @Test
    public void testBuffer() throws FactoryException, TransformException {
        GeometryFactory factory = new GeometryFactory();
        Geometry point = factory.createPoint(new Coordinate(115.48838, 35.218884));
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4490");
        CoordinateReferenceSystem crs1 = CRS.decode("EPSG:4548");
        MathTransform transform = CRS.findMathTransform(crs, crs1);
        point = JTS.transform(point, transform);
        Geometry polygon = point.buffer(20, 8);
        System.out.println(point.toText());
        System.out.println(polygon.toText());
        System.out.println(JTS.transform(point, transform.inverse()));
        System.out.println(JTS.transform(polygon, transform.inverse()));
    }

    @Test
    public void testBuffer2() throws FactoryException, TransformException {
        GeometryFactory factory = new GeometryFactory();
        Geometry point = factory.createPoint(new Coordinate(115.48838, 35.218884));
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4490");//4490
        CoordinateReferenceSystem crs1 = CRS.decode("EPSG:4548");//4548
        MathTransform transform = CRS.findMathTransform(crs, crs1);
        point = JTS.transform(point, transform);
        Geometry polygon = point.buffer(800);
        System.out.println(point.toText());
        System.out.println(polygon.toText());
        System.out.println(JTS.transform(point, transform.inverse()));
        System.out.println(JTS.transform(polygon, transform.inverse()));
    }

    @Test
    public void testProj2() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        System.out.println(crs.toWKT());
    }

    @Test
    public void testPrecisionModel() {
        double scale = Math.pow(10, 3);//精度为小数3位+1位近似位
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(scale));
        Geometry geom = geometryFactory.createPoint(new Coordinate(100.123456789, 36.123456789));
        System.out.println(geom.toText());
    }

    @Test
    public void testUnion() {
        double scale = Math.pow(10, 4);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(scale));
        Geometry geom = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(115.12345, 34.12345),
                new Coordinate(116.23456, 35.12345)
        });
        Geometry geom2 = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(116.23456, 35.12345),
                new Coordinate(117.15627, 34.12345)
        });

        System.out.println(geom.touches(geom2));
        LineMerger lineMerger = new LineMerger();
        lineMerger.add(geom);
        lineMerger.add(geom2);
        Collection collec = lineMerger.getMergedLineStrings();
        System.out.println(geom.toText());
        System.out.println(geom2.toText());
        for (Object o : collec) {
            Geometry geom3 = (Geometry) o;
            System.out.println(geom3.toText());
        }
    }

    @Test
    public void testMergeBySelf() throws ParseException {
        double EPS = 0.0000002;
        WKTReader wktReader = new WKTReader();
//        String wkt1 = "LINESTRING (116.23456 35.12345, 115.12345 34.12345)".replace(", ", ",");
        String wkt1 = "LINESTRING (115.12345 34.12345, 116.23456 35.12345)".replace(", ", ",");
        String ss1 = wkt1.substring(wkt1.indexOf("(") + 1, wkt1.indexOf(")"));
        String[] sp11 = ss1.substring(0, ss1.indexOf(",")).split(" ");
        String[] sp12 = ss1.substring(ss1.lastIndexOf(",") + 1).split(" ");
        double[] p11 = new double[2];
        p11[0] = Double.parseDouble(sp11[0]);
        p11[1] = Double.parseDouble(sp11[1]);
        double[] p12 = new double[2];
        p12[0] = Double.parseDouble(sp12[0]);
        p12[1] = Double.parseDouble(sp12[1]);
        String wkt2 = "LINESTRING (117.15627 34.12345, 116.23456 35.12345)".replace(", ", ",");
//        String wkt2 = "LINESTRING (116.23456 35.12345, 117.15627 34.12345)".replace(", ", ",");
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
            for (int i = pairs.length - 1; i >= 0; i--) {
                wkt.append(pairs[i]).append(",");
            }
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
        } else {
        }
        System.out.println(wkt);
    }
}
