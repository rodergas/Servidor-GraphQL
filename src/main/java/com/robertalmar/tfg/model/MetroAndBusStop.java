package com.robertalmar.tfg.model;

import java.util.ArrayList;

public class MetroAndBusStop implements Infrastructure {
	
	private final ArrayList<Infrastructure> nearByInfrastructure;

	private final GeographicalCoordinate locatedIn;
	
	private final String infrastructureType;
	
	private final String stopAddress;
	
	private final Integer stopPhone;
	
	private final String stopName;

	
	public MetroAndBusStop(ArrayList<Infrastructure> nearByInfrastructure, GeographicalCoordinate locatedIn, String infrastructureType, String stopAddress, Integer stopPhone, String stopName){
		this.nearByInfrastructure = nearByInfrastructure;
		this.locatedIn = locatedIn;
		this.infrastructureType = infrastructureType;
		this.stopAddress = stopAddress;
		this.stopPhone = stopPhone;
		this.stopName = stopName;

	}
	
	public ArrayList<Infrastructure> getNearByInfrastructure(){
		return nearByInfrastructure;
	}
	
	public GeographicalCoordinate getLocatedIn(){
		return locatedIn;
	}
	
	public String getInfrastructureType(){
		return infrastructureType;
	}

	public String getStopAddress() {
		return stopAddress;
	}

	public Integer getStopPhone() {
		return stopPhone;
	}

	public String getStopName() {
		return stopName;
	}

}
