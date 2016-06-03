package com.maslick.wktie;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.GeometryCollection;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.Point;

public class WKTReader {
	
	/**
	 * Transforms the input WKT-formatted String into Geometry object
	 * POINT (30 10)
	 * LINESTRING EMPTY
	 */
	public Geometry read(String wktString) {
		//TODO: Implement this
		Geometry ret=null;

		String geoType = wktString.split(" ")[0];
		String rest = wktString.substring(wktString.indexOf(' ')+1);
		String args = rest.split("[\\(\\)]")[1];
		switch (geoType) {
			case "POINT":
				double x = Double.parseDouble(args.split(" ")[0]);
				double y = Double.parseDouble(args.split(" ")[1]);
				ret = new Point(x,y);
				break;
			case "LINESTRING":
				break;
			default:
				ret=null;
				break;
		}
		return ret;
	}

}
