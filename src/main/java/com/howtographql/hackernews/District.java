package com.howtographql.hackernews;

public class District {
	private Integer districtNumber;
	private String districtName;
	
	public District(String districtName, Integer districtNumber){
		this.setDistrictNumber(districtNumber);
		this.setDistrictName(districtName);
	}

	public Integer getDistrictNumber() {
		return districtNumber;
	}

	public void setDistrictNumber(Integer districtNumber) {
		this.districtNumber = districtNumber;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	
}
