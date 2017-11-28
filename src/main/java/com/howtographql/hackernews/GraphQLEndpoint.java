package com.howtographql.hackernews;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.servlet.SimpleGraphQLServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndPoint extends SimpleGraphQLServlet {
  public GraphQLEndPoint() {
    super(SchemaParser.newParser()
    .file("ejemplo.graphqls")
    .resolvers(new Query(new MetroAndBusStopRepository(), new SuburbRepository(), new BicingStationRepository(), new GeographicalCoordinateRepository(), new DistrictRepository()))
    .build()
    .makeExecutableSchema());}
}
