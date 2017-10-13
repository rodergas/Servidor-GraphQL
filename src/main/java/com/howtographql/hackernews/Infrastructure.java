package com.howtographql.hackernews;

import java.util.ArrayList;

public interface Infrastructure {


	ArrayList<Infrastructure> getNearByInfrastructure();
	GeographicalCoordinate getLocatedIn();
	String getInfrastructureType();
	
	
}
