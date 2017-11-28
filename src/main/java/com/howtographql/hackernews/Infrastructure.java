package com.howtographql.hackernews;

import java.util.ArrayList;

public interface Infrastructure {
  String getInfrastructureType();

  GeographicalCoordinate getLocatedIn();

  ArrayList<Infrastructure> getNearByInfrastructure();
}
