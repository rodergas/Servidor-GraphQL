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
    	
    	Query sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example4> WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/GeographicalCoordinate'))}");

    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
    	
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/longitude> ?longitude}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/latitude> ?latitude}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res2 = vqe.execSelect();
			
			Float latitude = 0.0f;
			Float longitude = 0.0f;
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				if(valid(qs2, "latitude")) latitude = Float.parseFloat(modifyScalarValue(qs2.get("latitude").toString()));	
				if(valid(qs2, "longitude"))	longitude = Float.parseFloat(modifyScalarValue(qs2.get("longitude").toString()));
			}
			 GeographicalCoordinates.add(new GeographicalCoordinate(longitude, latitude));
		}
    	
    	
    }

    public List<GeographicalCoordinate> getAllGeographicalCoordinates() {

        return GeographicalCoordinates;
    }
    
    public void saveGeographicalCoordinate(GeographicalCoordinate geographicalCoordinate) {
    	GeographicalCoordinates.add(geographicalCoordinate);
    }
    
    public boolean valid(QuerySolution qs , String value){
    	if(qs.get(value) != null) return true;
    	else return false;
    }
    
    public String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }
    
}
