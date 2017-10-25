package com.robertalmar.tfg.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.robertalmar.tfg.model.MetroAndBusStop;
import com.robertalmar.tfg.query.SparqlQueryProvider;

import virtuoso.jena.driver.VirtGraph;

@Component
public class MetroAndBusStopRepository extends BaseRepository {
	
	private final List<MetroAndBusStop> metroAndBusStops;
	
	public MetroAndBusStopRepository(VirtGraph graph, SparqlQueryProvider queryProvider) {
		super(graph, queryProvider);
		
		metroAndBusStops = new ArrayList<>();
	}
	
    public List<MetroAndBusStop> getAllMetroAndBusStops() {
        return metroAndBusStops;
    }
}
