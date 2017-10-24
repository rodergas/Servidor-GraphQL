package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

public interface Infrastructure {


	ArrayList<Infrastructure> getNearByInfrastructure();
	GeographicalCoordinate getLocatedIn();
	String getInfrastructureType();
	
	
}
