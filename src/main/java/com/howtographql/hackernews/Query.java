package com.howtographql.hackernews;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;


public class Query implements GraphQLQueryResolver {
    
    private final GeographicalCoordinateRepository geographicalCoordinateRepository;
    private final BicingStationRepository bicingStationRepository;
    private final MetroAndBusStopRepository metroAndBusStopRepository;
    private final DistrictRepository districtRepository;
    private final SuburbRepository suburbRepository;


    public Query(GeographicalCoordinateRepository geographicalCoordinateRepository, BicingStationRepository bicingStationRepository, MetroAndBusStopRepository metroAndBusStopRepository, DistrictRepository districtRepository, SuburbRepository suburbRepository ) {
        this.geographicalCoordinateRepository = geographicalCoordinateRepository;
        this.bicingStationRepository = bicingStationRepository;
        this.metroAndBusStopRepository = metroAndBusStopRepository;
        this.districtRepository = districtRepository;
        this.suburbRepository = suburbRepository;

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
    
    public List<Suburb> allSuburbs(){
    	return suburbRepository.getAllSuburbs();
    }
    
    public List<District> allDistricts(){
    	return districtRepository.getAllDistricts();
    }
    
    public MetroAndBusStop getMetroAndBusStop(String name){
    	return metroAndBusStopRepository.getMetroAndBusStop(name);
    }
	
}
