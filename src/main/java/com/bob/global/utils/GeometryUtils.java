package com.bob.global.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {

  private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

  public static Point createPoint(double latitude, double longitude) {
    return geometryFactory.createPoint(new Coordinate(longitude, latitude));
  }
}
