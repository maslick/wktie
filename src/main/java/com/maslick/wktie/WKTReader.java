package com.maslick.wktie;

import com.sinergise.geometry.*;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;

public class WKTReader {

    private static final String EMPTY = "EMPTY";
    private static final String COMMA = ",";
    private static final String L_PAREN = "(";
    private static final String R_PAREN = ")";
    private StreamTokenizer tokenizer;

    /**
     * Transforms the input WKT-formatted String into Geometry object
     */
    public Geometry read(String wktString) throws IOException, ParseException  {
        StringReader reader = new StringReader(wktString);
        tokenizer = new StreamTokenizer(reader);
        tokenizer.resetSyntax();
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars(128 + 32, 255);
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars('+', '+');
        tokenizer.wordChars('.', '.');
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.commentChar('#');

        try {
            Geometry ret = readGeometry();
            return ret;
        }
        catch (IOException e) {
            throw new ParseException(e.toString(),0);
        }
    }

    private Geometry readGeometry() throws IOException, ParseException {
        String type = null;

        try{
            type = getNextWord();
        }catch(IOException e){
            return null;
        }catch(ParseException e){
            return null;
        }

        if (type.equals("POINT")) {
            return readPointText();
        }
        else if (type.equalsIgnoreCase("LINESTRING")) {
            return readLineStringText();
        }
        else if (type.equalsIgnoreCase("POLYGON")) {
            return readPolygonText();
        }
        else if (type.equalsIgnoreCase("MULTIPOINT")) {
            return readMultiPointText();
        }
        else if (type.equalsIgnoreCase("MULTILINESTRING")) {
            return readMultiLineStringText();
        }
        else if (type.equalsIgnoreCase("MULTIPOLYGON")) {
            return readMultiPolygonText();
        }
        else if (type.equalsIgnoreCase("GEOMETRYCOLLECTION")) {
            return readGeometryCollectionText();
        }
        throw new ParseException("Unknown geometry type: " + type,0);
    }

    private Coordinate getPreciseCoordinate()
            throws IOException, ParseException
    {
        Coordinate coord = new Coordinate();
        coord.x = getNextNumber();
        coord.y = getNextNumber();
        return coord;
    }

    private Coordinate[] getCoordinates() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new Coordinate[]{};
        }
        ArrayList coordinates = new ArrayList();
        coordinates.add(getPreciseCoordinate());
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            coordinates.add(getPreciseCoordinate());
            nextToken = getNextCloserOrComma();
        }
        Coordinate[] array = new Coordinate[coordinates.size()];
        return (Coordinate[]) coordinates.toArray(array);
    }

    private Point readPointText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new Point();
        }
        Coordinate c = new Coordinate(getNextNumber(),getNextNumber());
        Point point = new Point(c.x,c.y);
        getNextCloser();
        return point;
    }

    private MultiPoint readMultiPointText() throws IOException, ParseException {
        Coordinate [] cs = getCoordinates();
        if (cs.length == 0) {
            return new MultiPoint();
        }

        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i<cs.length; i++) {
            list.add(new Point(cs[i].getX(),cs[i].getY()));
        }
        Point[] arr = new Point[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }

        return new MultiPoint(arr);
    }

    private LineString readLineStringText() throws IOException, ParseException {
        Coordinate [] cs = getCoordinates();
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i<cs.length; i++) {
            list.add(cs[i].getX());
            list.add(cs[i].getY());
        }
        double[] arr = new double[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return new LineString(arr);
    }

    private MultiLineString readMultiLineStringText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new MultiLineString();
        }
        ArrayList<LineString> lineStrings = new ArrayList<>();
        LineString lineString = readLineStringText();
        lineStrings.add(lineString);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            lineString = readLineStringText();
            lineStrings.add(lineString);
            nextToken = getNextCloserOrComma();
        }
        LineString[] array = new LineString[lineStrings.size()];
        for (int i = 0; i < lineStrings.size(); i++) {
            array[i] = lineStrings.get(i);
        }
        return new MultiLineString(array);
    }

    private Polygon readPolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new Polygon();
        }
        ArrayList<LineString> holes = new ArrayList<>();
        LineString shell = readLineStringText();
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            LineString hole = readLineStringText();
            holes.add(hole);
            nextToken = getNextCloserOrComma();
        }

        LineString[] array = new LineString[holes.size()];
        for (int i = 0; i < holes.size(); i++) {
            array[i] = holes.get(i);
        }

        return new Polygon(shell, array);
    }

    private MultiPolygon readMultiPolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new MultiPolygon();
        }
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon polygon = readPolygonText();
        polygons.add(polygon);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            polygon = readPolygonText();
            polygons.add(polygon);
            nextToken = getNextCloserOrComma();
        }
        Polygon[] array = new Polygon[polygons.size()];
        for (int i = 0; i < polygons.size(); i++) {
            array[i] = polygons.get(i);
        }
        return new MultiPolygon(array);
    }

    private GeometryCollection readGeometryCollectionText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new GeometryCollection();
        }
        ArrayList<Geometry> geometries = new ArrayList<>();
        Geometry geometry = readGeometry();
        geometries.add(geometry);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            geometry = readGeometry();
            geometries.add(geometry);
            nextToken = getNextCloserOrComma();
        }
        Geometry[] array = new Geometry[geometries.size()];
        for (int i = 0; i < geometries.size(); i++) {
            array[i] = geometries.get(i);
        }
        return new GeometryCollection(array);
    }

    private String getNextWord() throws IOException, ParseException {
        int type = tokenizer.nextToken();
        String value;
        switch (type) {
            case StreamTokenizer.TT_WORD:
                String word = tokenizer.sval;
                if (word.equalsIgnoreCase(EMPTY)) {
                    value = EMPTY;
                }
                value = word;
                break;
            case'(':
                value = L_PAREN;
                break;
            case')':
                value = R_PAREN;
                break;
            case',':
                value = COMMA;
                break;
            default:
                parseError("word");
                value = null;
                break;
        }
        return value;
    }

    private double getNextNumber() throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch (type) {
            case StreamTokenizer.TT_WORD:
            {
                try {
                    return Double.parseDouble(tokenizer.sval);
                }
                catch (NumberFormatException ex) {
                    throw new ParseException("Invalid number: " + tokenizer.sval,0);
                }
            }
        }
        parseError("number");
        return 0.0;
    }

    private String getNextCloser() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(R_PAREN);
        return null;
    }

    private String getNextCloserOrComma() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(COMMA) || nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(COMMA + " or " + R_PAREN);
        return null;
    }

    private void parseError(String expected)
            throws ParseException {
        String tokenStr = tokenString();
        throw new ParseException("Expected " + expected + " but found " + tokenStr, 0);
    }

    private String tokenString() {
        switch (tokenizer.ttype) {
            case StreamTokenizer.TT_NUMBER:
                return "<NUMBER>";
            case StreamTokenizer.TT_EOL:
                return "End-of-Line";
            case StreamTokenizer.TT_EOF:
                return "End-of-Stream";
            case StreamTokenizer.TT_WORD:
                return "'" + tokenizer.sval + "'";
            default:
        }
        return "'" + (char) tokenizer.ttype + "'";
    }

    private String getNextEmptyOrOpener() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(EMPTY) || nextWord.equals(L_PAREN)) {
            return nextWord;
        }
        parseError(EMPTY + " or " + L_PAREN);
        return null;
    }
}
