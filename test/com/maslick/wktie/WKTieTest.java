package com.maslick.wktie;


import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.Point;
import org.junit.Test;
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
        String input = "POINT (2.0 1.0)";
        Geometry output = new Point(2,1.0);
        assertEquals(output, (new WKTReader().read(input)));
    }

    @Test
    public void testingWriterPoint() {
        Geometry input = new Point(2,1.0);
        String output = "PT(2.0 1.0)";
        assertEquals(output, (new WKTWriter()).write(input));
    }
}