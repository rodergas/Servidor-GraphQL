package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

public class MetroAndBusStopRepository {
	
	private final List<MetroAndBusStop> MetroAndBusStops;
	
	public MetroAndBusStopRepository(){
		MetroAndBusStops = new ArrayList<>();
	}
	
    public List<MetroAndBusStop> getAllMetroAndBusStops() {
        return MetroAndBusStops;
    }
}
