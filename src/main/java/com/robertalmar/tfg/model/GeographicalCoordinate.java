package com.robertalmar.tfg.model;

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

	@Override
	public String toString() {
		return "GeographicalCoordinate [longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
	public static class Builder {
		private Float longitude;
		
		private Float latitude;
		
		public Builder longitude(Float longitude){
            this.longitude = longitude;
            return this;
        }
		
		public Builder latitude(Float latitude){
            this.latitude = latitude;
            return this;
        }
		
		public GeographicalCoordinate build() {
			return new GeographicalCoordinate(longitude, latitude);
		}
	}
}
