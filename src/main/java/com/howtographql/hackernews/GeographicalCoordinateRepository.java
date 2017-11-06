package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import com.coxautodev.graphql.tools.GraphQLResolver;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class GeographicalCoordinateRepository {
    private final List<GeographicalCoordinate> GeographicalCoordinates;

    public GeographicalCoordinateRepository() {
    	GeographicalCoordinates = new ArrayList<>();

    	VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
    	Query sparql = QueryFactory.create("Select ?subject  FROM <http://localhost:8890/Example4> WHERE {"
    			+ "OPTIONAL { ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/GeographicalCoordinate>}."
				+ "}");
    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
    	
		while(res.hasNext()){
			QuerySolution qs = res.next();
			String subject = qs.get("?subject").toString();
			GeographicalCoordinates.add(new GeographicalCoordinate(subject));
		}
    }

    public List<GeographicalCoordinate> getAllGeographicalCoordinates() {

        return GeographicalCoordinates;
    }
    
    public void saveGeographicalCoordinate(GeographicalCoordinate geographicalCoordinate) {
    	GeographicalCoordinates.add(geographicalCoordinate);
    }
    
    
    public String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }
    
}
