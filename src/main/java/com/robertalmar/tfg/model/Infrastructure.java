package com.robertalmar.tfg.model;

import java.util.ArrayList;

public interface Infrastructure {
	
	ArrayList<Infrastructure> getNearByInfrastructure();
	
	GeographicalCoordinate getLocatedIn();
	
	String getInfrastructureType();
	
}
