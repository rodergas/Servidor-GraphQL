package com.howtographql.hackernews;

import java.util.ArrayList;


public class BicingStation extends Infrastructure {
	private final String stationStreetName;
	private final String stationType;
	private final Integer stationBikesNumber;
	private final Integer stationID;
	private final Float stationAltitude;
	private final Integer stationSlotsNumber;
	private final Integer stationStreetNumber;
	private final ArrayList<BicingStation> nearByStation;
	private final String stationStatus;
	
	public BicingStation(ArrayList<Infrastructure> nearByInfrastructure, GeographicalCoordinate locatedIn, String infrastructureType ,String stationStreetName ,String stationType, Integer stationBikesNumber, Integer stationID, Float stationAltitude, Integer stationSlotsNumber, Integer stationStreetNumber, ArrayList<BicingStation> nearByStation, String stationStatus){
		super(nearByInfrastructure, locatedIn, infrastructureType);
		this.stationStreetName = stationStreetName;
		this.stationType = stationType;
		this.stationBikesNumber = stationBikesNumber;
		this.stationID = stationID;
		this.stationAltitude = stationAltitude;
		this.stationSlotsNumber = stationSlotsNumber;
		this.stationStreetNumber = stationStreetNumber;
		this.nearByStation = nearByStation;
		this.stationStatus = stationStatus;
		
	}

	public String getStationStreetName() {
		return stationStreetName;
	}

	public String getStationType() {
		return stationType;
	}

	public Integer getStationBikesNumber() {
		return stationBikesNumber;
	}

	public Integer getStationID() {
		return stationID;
	}

	public Float getStationAltitude() {
		return stationAltitude;
	}

	public Integer getStationSlotsNumber() {
		return stationSlotsNumber;
	}

	public Integer getStationStreetNumber() {
		return stationStreetNumber;
	}

	public ArrayList<BicingStation> getNearByStation() {
		return nearByStation;
	}

	public String getStationStatus() {
		return stationStatus;
	}
}