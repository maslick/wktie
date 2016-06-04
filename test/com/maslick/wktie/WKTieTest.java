package com.maslick.wktie;


import com.sinergise.geometry.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by maslick on 03/06/16.
 */
public class WKTieTest {

    @Test
    public void testingReaderPoint() {
        String input = "POINT (2 1)";
        Geometry expected = new Point(2,1);
        assertEquals(expected, (new WKTReader().read(input)));
    }

    @Test
    public void testingReaderEmptyPoint() {
        String input = "POINT EMPTY";
        Geometry expected = new Point();
        assertEquals(expected, (new WKTReader().read(input)));
    }

    @Test
    public void testingWriterPoint() {
        Geometry input = new Point(1, 1.123);
        String expected = "POINT (1 1.123)";
        assertEquals(expected, (new WKTWriter()).write(input));
    }

    @Test
    public void testingWriterEmptyPoint() {
        Geometry input = new Point();
        String expected = "POINT EMPTY";
        assertEquals(expected, (new WKTWriter()).write(input));
    }

    @Test
    public void testingWriterLine() {
        Geometry input = new LineString(new double[]{30.123, 10, 10, 30, 40, 40});
        String expected = "LINESTRING (30.123 10, 10 30, 40 40)";
        assertEquals(expected, (new WKTWriter()).write(input));
    }

    @Test
    public void testingWriterEmptyLine() {
        Geometry input = new LineString();
        String expected = "LINESTRING EMPTY";
        assertEquals(expected, (new WKTWriter()).write(input));
    }

    @Test
    public void testingTuple() {
        WKTReader r = new WKTReader();
        String expected = "30.123 10, 10 30, 40 40";
        ArrayList<Coordinate> list = r.parseArgs(expected);
        for (int i=0; i<list.size(); i ++) {
            System.out.println(list.get(i).getX() + " " + list.get(i).getY());
        }
    }

    @Test
    public void testingReaderLine() {
        String input = "LINESTRING (11 10, 30 20, 11 12)";
        Geometry expected = new LineString(new double[] {11,10,30,20,11,12});
        assertEquals(expected, (new WKTReader().read(input)));
    }

    @Test
    public void testingWriterEmptyPolygon() {
        Geometry input = new Polygon(null, null);
        String expected = "POLYGON EMPTY";
        assertEquals(expected, new WKTWriter().write(input));
    }

    @Test
    public void testingWriterPolygon() {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Polygon input = new Polygon(outer, null);
        String expected = "POLYGON ((11 10, 30 20, 11 12, 11 10))";
        assertEquals(expected, new WKTWriter().write(input));
    }

    @Test
    public void testingWriterPolygonWithHoles() {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2}), new LineString(new double[] {1,2,3,4,5,6,1,2})};
        Polygon input = new Polygon(outer, holes);
        String expected = "POLYGON ((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2),(1 2, 3 4, 5 6, 1 2))";
        assertEquals(expected, new WKTWriter().write(input));
    }

    @Test
    public void testingWriterEmptyMultiPoint() {
        MultiPoint mp = new MultiPoint();
        String expected = "MULTIPOINT EMPTY";
        assertEquals(expected, new WKTWriter().write(mp));
    }

    @Test
    public void testingWriterMultiPoint() {
        MultiPoint mp = new MultiPoint(new Point[] {new Point(1,2), new Point(3,4)});
        String expected = "MULTIPOINT (1 2, 3 4)";
        assertEquals(expected, new WKTWriter().write(mp));
    }

    @Test
    public void testingWriterEmptyMultiLineString() {
        MultiLineString mp = new MultiLineString();
        String expected = "MULTILINESTRING EMPTY";
        assertEquals(expected, new WKTWriter().write(mp));
    }

    @Test
    public void testingWriterMultiLineString() {
        MultiLineString mp = new MultiLineString(new LineString[] {new LineString(new double[]{1,2}), new LineString(new double[]{3,4})});
        String expected = "MULTILINESTRING ((1 2),(3 4))";
        assertEquals(expected, new WKTWriter().write(mp));
    }

    @Test
    public void testingWriterEmptyMultiPolygon() {
        MultiPolygon mlp = new MultiPolygon();
        String expected = "MULTIPOLYGON EMPTY";
        assertEquals(expected, new WKTWriter().write(mlp));
    }

    @Test
    public void testingWriterMultiPolygon() {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Polygon input1 = new Polygon(outer, null);
        Polygon input2 = new Polygon(outer, null);
        String expected = "MULTIPOLYGON (((11 10, 30 20, 11 12, 11 10)),((11 10, 30 20, 11 12, 11 10)))";
        MultiPolygon mlp = new MultiPolygon(new Polygon[] { input1, input2 });
        assertEquals(expected, new WKTWriter().write(mlp));
    }

    @Test
    public void testingWriterMultiPolygonWithHoles() {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Polygon input1 = new Polygon(outer, holes);
        Polygon input2 = new Polygon(outer, holes);
        String expected = "MULTIPOLYGON (((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2)),((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2)))";
        MultiPolygon mlp = new MultiPolygon(new Polygon[] { input1, input2 });
        assertEquals(expected, new WKTWriter().write(mlp));
    }


}