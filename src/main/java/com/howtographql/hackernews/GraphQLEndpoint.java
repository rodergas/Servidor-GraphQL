package com.howtographql.hackernews;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
	
    public GraphQLEndpoint() {
        super(buildSchema());
    }
    
    private static GraphQLSchema buildSchema() {
    	GeographicalCoordinateRepository geographicalCoordinateRepository = new GeographicalCoordinateRepository();
    	BicingStationRepository bicingStationRepository = new BicingStationRepository();
    	MetroAndBusStopRepository metroAndBusStopRepository = new MetroAndBusStopRepository();
    	DistrictRepository districtRepository = new DistrictRepository();
    	SuburbRepository suburbRepository = new SuburbRepository();
    	
    	
        return SchemaParser.newParser()
                .file("tfg.graphqls")
                .resolvers(new Query(geographicalCoordinateRepository,bicingStationRepository, metroAndBusStopRepository, districtRepository, suburbRepository))
                //.dictionary(MetroAndBusStop.class)
                .build()
                .makeExecutableSchema();
    }
}
