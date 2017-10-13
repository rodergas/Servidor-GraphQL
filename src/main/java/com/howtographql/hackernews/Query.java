package com.howtographql.hackernews;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;


public class Query implements GraphQLQueryResolver {
    
    private final GeographicalCoordinateRepository geographicalCoordinateRepository;
    private final BicingStationRepository bicingStationRepository;
    private final MetroAndBusStopRepository metroAndBusStopRepository;


    public Query(GeographicalCoordinateRepository geographicalCoordinateRepository, BicingStationRepository bicingStationRepository, MetroAndBusStopRepository metroAndBusStopRepository ) {
        this.geographicalCoordinateRepository = geographicalCoordinateRepository;
        this.bicingStationRepository = bicingStationRepository;
        this.metroAndBusStopRepository = metroAndBusStopRepository;

    }

    public List<GeographicalCoordinate> allGeographicalCoordinates() {

        return geographicalCoordinateRepository.getAllGeographicalCoordinates();
        
    }

    public List<BicingStation> allBicingStations() {
        return bicingStationRepository.getAllBicingStations();
    }
    
    public List<MetroAndBusStop> allMetroAndBusStops() {
        return metroAndBusStopRepository.getAllMetroAndBusStops();
    }
	
}
