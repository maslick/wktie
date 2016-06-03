package com.maslick.wktie;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.GeometryCollection;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.Point;

import java.util.Arrays;

public class WKTWriter {
	
	/**
	 * Transforms the input Geometry object into WKT-formatted String. e.g.
	 * <pre><code>
	 * new WKTWriter().write(new LineString(new double[]{30, 10, 10, 30, 40, 40}));
	 * //returns "LINESTRING (30 10, 10 30, 40 40)"
	 * </code></pre>
	 */
	public String write(Geometry geom) {
		String ret = "";
		String gtype = geom.getClass().getSimpleName();
		switch (gtype) {
			case "Point":
				Point p = (Point) geom;
				if (p.isEmpty()) {
					ret = "POINT EMPTY";
				}
				else {
					ret = "POINT (" + fmt(p.getX()) + " " + fmt(p.getY()) + ")";
				}
				break;

			case "LineString":
				LineString ln = (LineString) geom;
				if (ln.isEmpty()) {
					ret = "LINESTRING EMPTY";
				}
				else {
					ret = "LINESTRING (";
					for(int i=0; i<ln.getNumCoords(); i++) {
						ret += fmt(ln.getX(i)) + " " + fmt(ln.getY(i)) + (i!=ln.getNumCoords()-1?", ":"");
					}
					ret += ")";
				}
				break;
		}
		return ret;
		//TODO: Implement this
		//return write(new GeometryCollection<Geometry>(new Geometry[]{new Point(4,6), new LineString(new double[] {4,6,7,10})}));
	}


	public static String fmt(double d) {
		if(d == (long) d)
			return String.format("%d",(long)d);
		else
			return String.format("%s",d);
	}
}
