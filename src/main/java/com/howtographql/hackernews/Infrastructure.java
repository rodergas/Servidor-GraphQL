package com.howtographql.hackernews;

import java.util.ArrayList;

public interface Infrastructure {
  GeographicalCoordinate getLocatedIn();

  ArrayList<Infrastructure> getNearByInfrastructure();

  String getInfrastructureType();
}
