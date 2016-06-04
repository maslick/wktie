package com.maslick.wktie;

import com.sinergise.geometry.Geometry;
import com.sinergise.geometry.LineString;
import com.sinergise.geometry.Point;
import lombok.*;

import java.util.ArrayList;
import java.util.*;

public class WKTReader {

	/**
	 * Transforms the input WKT-formatted String into Geometry object
	 * POINT (30 10)
	 * LINESTRING EMPTY
	 */
	public Geometry read(String wktString) {
		//TODO: Implement this
		Geometry ret=null;
		String args = "";

		String objType = getObjectType(wktString);
		String rest = wktString.substring(wktString.indexOf(' ') + 1);

		if (!rest.equals("EMPTY")) {
			args = getObjectArgs(wktString);
		}
		switch (objType) {
			case "POINT":
				if (rest.equals("EMPTY"))
					ret = new Point();
				else {
					Tuple t = parseArgs(args).get(0);
					ret = new Point(t.getX(),t.getY());
				}
				break;
			case "LINESTRING":
				if (rest.equals("EMPTY"))
					ret = new LineString();
				else {
					ret = new LineString(parseArgsDouble(args));
				}
				break;
			case "POLYGON":
				break;

			case "MULTIPOINT":
				break;

			case "MULTILINESTRING":
				break;

			case "MULTIPOLYGON":
				break;

			case "GeometryCollection":
				break;

			default:
				ret=null;
				break;
		}
		return ret;
	}

	public String getObjectType(String str) {
		return str.split(" ")[0];
	}

	public String getObjectArgs(String str) {
		return str.substring(str.indexOf(' ') + 1).split("[\\(\\)]")[1];
	}

	public ArrayList<Tuple> parseArgs(String str) {
		String[] splitByComma = str.split(", ");
		String[] splitBySpace;
		ArrayList<Tuple> list = new ArrayList<>();
		for (int i=0; i<splitByComma.length; i++) {
			splitByComma[i] = splitByComma[i].trim();
			splitBySpace = splitByComma[i].split(" ");
			list.add(new Tuple(Double.parseDouble(splitBySpace[0]),Double.parseDouble(splitBySpace[1])));
		}
		return list;
	}

	public double[] parseArgsDouble(String str) {
		String[] splitByComma = str.split(", ");
		String[] splitBySpace;
		ArrayList<Double> list = new ArrayList<>();
		for (int i=0; i<splitByComma.length; i++) {
			splitByComma[i] = splitByComma[i].trim();
			splitBySpace = splitByComma[i].split(" ");
			list.add(Double.parseDouble(splitBySpace[0]));
			list.add(Double.parseDouble(splitBySpace[1]));
		}
		double[] ret = new double[list.size()];
		Iterator<Double> iterator = list.iterator();
		int i = 0;
		while(iterator.hasNext()) {
			ret[i] = iterator.next();
			i++;
		}
		return ret;
	}



	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class Tuple {
		double x;
		double y;
	}

}
