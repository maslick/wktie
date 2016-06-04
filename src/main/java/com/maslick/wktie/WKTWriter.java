package com.maslick.wktie;

import com.sinergise.geometry.*;


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
				ret = parsePoint(geom);
				break;

			case "LineString":
				ret = parseLineString(geom);
				break;

			case "Polygon":
				ret = parsePolygon(geom);
				break;

			case "MultiPoint":

				break;

			case "MultiLineString":
				break;

			case "MultiPolygon":
				break;

			case "GeometryCollection":
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

	public String getTupleString(Geometry geom) {
		String ret = "";
		switch (geom.getClass().getSimpleName()) {
			case "Point":
				Point p = (Point) geom;
				ret = fmt(p.getX()) + " " + fmt(p.getY());
				break;
			case "LineString":
				LineString ln = (LineString) geom;
				int len = ln.getNumCoords();
				for (int i=0; i<len; i++ ) {
					ret += fmt(ln.getX(i)) + " " + fmt(ln.getY(i)) + (i!=len-1?", ":"");
				}
				break;
		}
		return ret;
	}

	public String parsePoint(Geometry geom) {
		Point p = (Point) geom;
		if (p.isEmpty()) {
			return "POINT EMPTY";
		}
		return "POINT (" + getTupleString(p) + ")";
	}

	public String parseLineString(Geometry geom) {
		LineString ln = (LineString) geom;
		if (ln.isEmpty()) {
			return "LINESTRING EMPTY";
		}
		return "LINESTRING (" + getTupleString(ln) + ")";
	}

	public String parsePolygon(Geometry geom) {
		Polygon pg = (Polygon) geom;
		String ret = "";
		if (pg.isEmpty()) {
			return "POLYGON EMPTY";
		}

		ret = "POLYGON ((";
		ret += getTupleString(pg.getOuter());
		ret += ")";

		if (pg.getNumHoles() > 0) {
			ret += ",";
			for (int i=0; i<pg.getNumHoles(); i++) {
				ret += "(";
				ret += getTupleString(pg.getHole(i));
				ret += ")" + (i!=pg.getNumHoles()-1?",":"");
			}
		}

		ret += ")";
		return ret;
	}

	public String parseMultiPoint(Geometry geom) {
		MultiPoint mp = (MultiPoint) geom;
		String ret = "";
		if (mp.isEmpty()) {
			return "MULTIPOINT EMPTY";
		}
		return ret;
	}
}
