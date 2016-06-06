# wktie
[![Build Status](https://travis-ci.org/maslick/wktie.svg?branch=master)](https://travis-ci.org/maslick/wktie)

Local testing:
```
$ ./gradlew clean test
```

Testing with docker (imitates a clean system):
```
  $ docker run -ti jboss/base-jdk:8 bash
  
  $ curl -O curl -O https://codeload.github.com/maslick/wktie/zip/master
  $ unzip master
  $ cd wktie-master
  $ ./gradlew clean test
```

Usage:
- Writer (parses a Geometry object into a WKT string)
```
String wktString = new WKTWriter().write(new Point(1,1));
```

- Reader (parses a WKT string into a Geometry object)
```
Point p = new WKTReader().read("POINT (1 1)");
```
