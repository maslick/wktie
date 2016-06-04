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
				ret = checkIfEmpty(parsePoint(geom), "POINT");
				break;

			case "LineString":
				ret = checkIfEmpty(parseLineString(geom), "LINESTRING");
				break;

			case "Polygon":
				ret = checkIfEmpty(parsePolygon(geom), "POLYGON");
				break;

			case "MultiPoint":
				ret = checkIfEmpty(parseMultiPoint(geom), "MULTIPOINT");
				break;

			case "MultiLineString":
				ret = checkIfEmpty(parseMultiLineString(geom), "MULTILINESTRING");
				break;

			case "MultiPolygon":
				ret = checkIfEmpty(parseMultiPolygon(geom), "MULTIPOLYGON");
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

	// POINT (30 10)
	public String parsePoint(Geometry geom) {
		Point p = (Point) geom;
		if (p.isEmpty()) {
			return "EMPTY";
		}
		return getTupleString(p);
	}

	// LINESTRING (30 10, 10 30, 40 40)
	public String parseLineString(Geometry geom) {
		LineString ln = (LineString) geom;
		if (ln.isEmpty()) {
			return "EMPTY";
		}
		return getTupleString(ln);
	}

	// POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))
	public String parsePolygon(Geometry geom) {
		Polygon pg = (Polygon) geom;
		String ret = "";
		if (pg.isEmpty()) {
			return "EMPTY";
		}

		ret = "(";
		ret += getTupleString(pg.getOuter());
		ret += ")";
		if (pg.getNumHoles() > 0) {
			ret += ",";
			for (int i=0; i<pg.getNumHoles(); i++) {
				ret += "(";
				ret += getTupleString(pg.getHole(i));
				ret += ")" + ( i != pg.getNumHoles()-1 ? "," : "");
			}
		}

		return ret;
	}

	// MULTIPOINT (10 40, 40 30, 20 20, 30 10)
	public String parseMultiPoint(Geometry geom) {
		MultiPoint mp = (MultiPoint) geom;
		String ret = "";
		if (mp.isEmpty()) {
			return "EMPTY";
		}
		for (int i = 0; i<mp.size(); i++) {
			ret += parsePoint(mp.get(i)) + ( i != mp.size()-1 ? ", " : "" );
		}
		return ret;
	}

	// MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))
	public String parseMultiLineString(Geometry geom) {
		MultiLineString mls = (MultiLineString) geom;
		String ret = "";
		if (mls.isEmpty()) {
			return "EMPTY";
		}
		for (int i = 0; i<mls.size(); i++) {
			ret += "(" + parseLineString(mls.get(i)) + ( i != mls.size()-1 ? ")," : ")" );
		}
		return ret;
	}

	// MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))
	public String parseMultiPolygon(Geometry geom) {
		MultiPolygon mlp = (MultiPolygon) geom;
		String ret = "";
		if (mlp.isEmpty()) {
			return "EMPTY";
		}
		for (int i = 0; i<mlp.size(); i++) {
			ret += "(" + parsePolygon(mlp.get(i)) + ( i != mlp.size()-1 ? ")," : ")" );
		}
		return ret;
	}

	public String checkIfEmpty(String strToCheck, String type) {
		if (strToCheck.equals("EMPTY")) {
			return type + " " + strToCheck;
		}
		else
			return type + " (" + strToCheck + ")";
	}
}
