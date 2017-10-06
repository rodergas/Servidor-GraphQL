package com.howtographql.hackernews;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;


public class Query implements GraphQLRootResolver {
    
    private final GeographicalCoordinateRepository geographicalCoordinateRepository;
    private final BicingStationRepository bicingStationRepository;

    public Query(GeographicalCoordinateRepository geographicalCoordinateRepository , BicingStationRepository bicingStationRepository) {
        this.geographicalCoordinateRepository = geographicalCoordinateRepository;
        this.bicingStationRepository = bicingStationRepository;
    }

    public List<GeographicalCoordinate> allGeographicalCoordinates() {
        return geographicalCoordinateRepository.getAllGeographicalCoordinates();
    }
    
    public List<BicingStation> allBicingStations() {
        return bicingStationRepository.getAllBicingStations();
    }

}
