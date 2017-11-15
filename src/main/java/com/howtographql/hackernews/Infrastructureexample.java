package com.howtographql.hackernews;

import java.util.ArrayList;

public interface Infrastructureexample {
  GeographicalCoordinate getLocatedIn();

  ArrayList<Infrastructure> getNearByInfrastructure();

  String getInfrastructureType();
}
