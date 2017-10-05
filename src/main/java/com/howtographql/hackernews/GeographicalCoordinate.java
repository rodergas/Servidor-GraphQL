package com.howtographql.hackernews;

public class GeographicalCoordinate {
	private final Float longitude;
	private final Float latitude;
	
	public GeographicalCoordinate(Float longitude, Float latitude){
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public Float getLatitude() {
		return latitude;
	}
	

}
