package com.howtographql.hackernews;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;


public class Query implements GraphQLQueryResolver {
    
    private final GeographicalCoordinateRepository geographicalCoordinateRepository;
    private final BicingStationRepository bicingStationRepository;
    private final MetroAndBusStopRepository metroAndBusStopRepository;
    private final DistrictRepository districtRepository;
    private final SuburbRepository suburbRepository;


    public Query(MetroAndBusStopRepository metroAndBusStopRepository, BicingStationRepository bicingStationRepository, SuburbRepository suburbRepository, GeographicalCoordinateRepository geographicalCoordinateRepository, DistrictRepository districtRepository) {
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
    
    public District getDistrict(String id){
    	return districtRepository.getDistrict(id);
    }
    
    public Suburb getSuburb(String id){
    	return suburbRepository.getSuburb(id);
    }
    
    public MetroAndBusStop getMetroAndBusStop(String id){
    	return metroAndBusStopRepository.getMetroAndBusStop(id);
    }
    
    public GeographicalCoordinate getGeographicalCoordinate(String id){
    	return geographicalCoordinateRepository.getGeographicalCoordinate(id);
    }
    
    public BicingStation getBicingStation(String id){
    	return bicingStationRepository.getBicingStation(id);
    }

	
}
