package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import com.coxautodev.graphql.tools.GraphQLResolver;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class GeographicalCoordinateRepository{
    private final List<GeographicalCoordinate> GeographicalCoordinates;

    public GeographicalCoordinateRepository() {
    	
    	VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
    	/*
    	Query sparql = QueryFactory.create("select * WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o,'www.example.com'))}");
    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	Model model = vqe.execConstruct();
    	
    	StmtIterator it =  model.listStatements();
    	/*
		while (it.hasNext()) {
		     Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     System.out.println(s.toString() + " " + p.toString() + " " + o.toString());
		}*/
    	GeographicalCoordinates = new ArrayList<>();
        //add some links to start off with
    	GeographicalCoordinates.add(new GeographicalCoordinate(3.022f, 9.323f));
    	GeographicalCoordinates.add(new GeographicalCoordinate(4.533f, 1.233f));
    }

    public List<GeographicalCoordinate> getAllGeographicalCoordinates() {
        return GeographicalCoordinates;
    }
    
    public void saveGeographicalCoordinate(GeographicalCoordinate geographicalCoordinate) {
    	GeographicalCoordinates.add(geographicalCoordinate);
    }
}
