package com.TFG.servidorGraphQL2;

import java.util.ArrayList;

public interface Infrastructure {
  String getInfrastructureType();

  GeographicalCoordinate getLocatedIn();

  ArrayList<Infrastructure> getNearByInfrastructure();
}
