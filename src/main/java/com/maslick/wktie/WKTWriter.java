package com.maslick.wktie;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.GeometryCollection;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.Point;

public class WKTWriter {
	
	/**
	 * Transforms the input Geometry object into WKT-formatted String. e.g.
	 * <pre><code>
	 * new WKTWriter().write(new LineString(new double[]{30, 10, 10, 30, 40, 40}));
	 * //returns "LINESTRING (30 10, 10 30, 40 40)"
	 * </code></pre>
	 */
	public static void write(Geometry geom) {
		String gtype = geom.getClass().getName();
		System.out.print(geom.getClass().getName());
		switch (gtype) {
			case "Point": System.out.print("The square root of ");

		}
		//TODO: Implement this
		//return write(new GeometryCollection<Geometry>(new Geometry[]{new Point(4,6), new LineString(new double[] {4,6,7,10})}));
	}
}
