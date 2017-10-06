package com.howtographql.hackernews;

import java.util.ArrayList;

public class Infrastructure {
	private final ArrayList<Infrastructure> nearByInfrastructure;
	private final GeographicalCoordinate locatedIn;
	private final String infrastructureType;
	
	public Infrastructure(ArrayList<Infrastructure> nearByInfrastructure, GeographicalCoordinate locatedIn, String infrastructureType){
		this.nearByInfrastructure = nearByInfrastructure;
		this.locatedIn = locatedIn;
		this.infrastructureType = infrastructureType;
	}

	public ArrayList<Infrastructure> getNearByInfrastructure() {
		return nearByInfrastructure;
	}

	public GeographicalCoordinate getLocatedIn() {
		return locatedIn;
	}

	public String getInfrastructureType() {
		return infrastructureType;
	}
	
	
}
