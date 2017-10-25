package com.robertalmar.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.robertalmar.tfg.model.BicingStation;
import com.robertalmar.tfg.model.GeographicalCoordinate;
import com.robertalmar.tfg.model.MetroAndBusStop;

@Component
public class QueryResolver implements GraphQLQueryResolver {
    
    private final GeographicalCoordinateRepository geographicalCoordinateRepository;
    
    private final BicingStationRepository bicingStationRepository;
    
    private final MetroAndBusStopRepository metroAndBusStopRepository;


    public QueryResolver(GeographicalCoordinateRepository geographicalCoordinateRepository, BicingStationRepository bicingStationRepository, MetroAndBusStopRepository metroAndBusStopRepository ) {
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
