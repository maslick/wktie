package com.maslick.wktie;


import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.Point;
import com.sinergise.geometry.Polygon;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by maslick on 03/06/16.
 */
public class WKTieTest {
    @Test
    public void testAssertTrue() {
        assertTrue("failure - should be true", true);
    }

    @Test
    public void testingPoint() {
        Point p = new Point(1, 1);
        assertEquals(p.toString(), "PT(1.0 1.0)");
    }


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
        ArrayList<WKTReader.Tuple> list = r.parseArgs(expected);
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
    public void testingWriterPolygon() {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2})};

        Geometry input = new Polygon(outer, null);
        String expected = "POLYGON ((11 10, 30 20, 11 12, 11 10))";
        assertEquals(expected, new WKTWriter().write(input));
    }

    @Test
    public void testingWriterPolygonWithHoles() {
        LineString outer = new LineString(new double[] {11,10,30,20,11,12,11,10});
        LineString[] holes = new LineString[] {new LineString(new double[] {1,2,3,4,5,6,1,2}), new LineString(new double[] {1,2,3,4,5,6,1,2})};
        Geometry input = new Polygon(outer, holes);
        String expected = "POLYGON ((11 10, 30 20, 11 12, 11 10),(1 2, 3 4, 5 6, 1 2),(1 2, 3 4, 5 6, 1 2))";
        assertEquals(expected, new WKTWriter().write(input));
    }
}