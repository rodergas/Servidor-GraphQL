package com.howtographql.hackernews;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;


public class Query implements GraphQLRootResolver {
    
    private final GeographicalCoordinateRepository geographicalCoordinateRepository;

    public Query(GeographicalCoordinateRepository geographicalCoordinateRepository) {
        this.geographicalCoordinateRepository = geographicalCoordinateRepository;
    }

    public List<GeographicalCoordinate> allGeographicalCoordinates() {
        return geographicalCoordinateRepository.getAllGeographicalCoordinates();
    }

}
