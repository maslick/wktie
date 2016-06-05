package com.maslick.wktie;


import com.sinergise.geometry.*;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;


public class WKTieTest {
    @Test
    public void testingPoint() throws IOException, ParseException {
        Point input = new Point(1, 1.123);
        String expected = "POINT (1 1.123)";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingEmptyPoint() throws IOException, ParseException {
        Point input = new Point();
        String expected = "POINT EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingWriterLine() throws IOException, ParseException {
        LineString input = new LineString(new double[]{30.123, 10, 10, 30, 40, 40});
        String expected = "LINESTRING (30.123 10, 10 30, 40 40)";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingWriterEmptyLine() throws IOException, ParseException {
        LineString input = new LineString();
        String expected = "LINESTRING EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingWriterEmptyPolygon() throws IOException, ParseException {
        Polygon input = new Polygon(null, null);
        String expected = "POLYGON EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingWriterPolygon() throws IOException, ParseException {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Polygon input = new Polygon(outer, null);
        String expected = "POLYGON ((11 10, 30 20, 11 12, 11 10))";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingPolygonWithHoles() throws IOException, ParseException {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2}), new LineString(new double[] {1,2,3,4,5,6,1,2})};
        Polygon input = new Polygon(outer, holes);
        String expected = "POLYGON ((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2),(1 2, 3 4, 5 6, 1 2))";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingEmptyMultiPoint() throws IOException, ParseException {
        MultiPoint input = new MultiPoint();
        String expected = "MULTIPOINT EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingMultiPoint() throws IOException, ParseException {
        MultiPoint input = new MultiPoint(new Point[] {new Point(1,2), new Point(3,4)});
        String expected = "MULTIPOINT (1 2, 3 4)";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingEmptyMultiLineString() throws IOException, ParseException {
        MultiLineString input = new MultiLineString();
        String expected = "MULTILINESTRING EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingMultiLineString() throws IOException, ParseException {
        MultiLineString input = new MultiLineString(new LineString[] {new LineString(new double[]{1,2}), new LineString(new double[]{3,4})});
        String expected = "MULTILINESTRING ((1 2),(3 4))";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingEmptyMultiPolygon() throws IOException, ParseException {
        MultiPolygon input = new MultiPolygon();
        String expected = "MULTIPOLYGON EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingMultiPolygon() throws IOException, ParseException {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Polygon input1 = new Polygon(outer, null);
        Polygon input2 = new Polygon(outer, null);
        String expected = "MULTIPOLYGON (((11 10, 30 20, 11 12, 11 10)),((11 10, 30 20, 11 12, 11 10)))";
        MultiPolygon input = new MultiPolygon(new Polygon[] { input1, input2 });
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingMultiPolygonWithHoles() throws IOException, ParseException {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Polygon input1 = new Polygon(outer, holes);
        Polygon input2 = new Polygon(outer, holes);
        String expected = "MULTIPOLYGON (((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2)),((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2)))";
        MultiPolygon input = new MultiPolygon(new Polygon[] { input1, input2 });
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test
    public void testingGeometryCollection() throws IOException, ParseException {
        Point p = new Point(4, 6);
        LineString ls = new LineString(new double[]{4, 6, 7, 10});
        GeometryCollection input = new GeometryCollection(new Geometry[] {p,ls});
        String expected = "GEOMETRYCOLLECTION (POINT (4 6),LINESTRING (4 6, 7 10))";
        assertEquals(expected, new WKTWriter().write(input));
        assertEquals(input, new WKTReader().read(expected));
    }

    @Test(expected=ParseException.class)
    public void testingParseException()  throws IOException, ParseException {
        String erroneousWktString = "POINT (1, 6)";
        Point p = (Point) new WKTReader().read(erroneousWktString);
    }
}