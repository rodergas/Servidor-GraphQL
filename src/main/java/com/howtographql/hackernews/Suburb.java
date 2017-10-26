package com.howtographql.hackernews;

import java.util.ArrayList;

public class Suburb {
	private String suburbName;
	private District belongsTo;
	private ArrayList<MetroAndBusStop> providesStop;
	
	public Suburb(String suburbName, District belongsTo, ArrayList<MetroAndBusStop> providesStop){
		this.setSuburbName(suburbName);
		this.setBelongsTo(belongsTo);
		this.setProvidesStop(providesStop);
	}

	public String getSuburbName() {
		return suburbName;
	}

	public void setSuburbName(String suburbName) {
		this.suburbName = suburbName;
	}

	public District getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(District belongsTo) {
		this.belongsTo = belongsTo;
	}

	public ArrayList<MetroAndBusStop> getProvidesStop() {
		return providesStop;
	}

	public void setProvidesStop(ArrayList<MetroAndBusStop> providesStop) {
		this.providesStop = providesStop;
	}
}
