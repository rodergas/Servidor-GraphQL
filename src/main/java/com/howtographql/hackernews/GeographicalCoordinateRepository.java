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

public class GeographicalCoordinateRepository {
    private final List<GeographicalCoordinate> GeographicalCoordinates;

    public GeographicalCoordinateRepository() {
    	GeographicalCoordinates = new ArrayList<>();
    	
    	VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
    	
    	Query sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/GeographicalCoordinate'))}");
    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	Model modelNameInstance = vqe.execConstruct();
    	
    	StmtIterator it =  modelNameInstance.listStatements();
    	//Cuantas instancias hay
		while (it.hasNext()) {
		     Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     
		     sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'"+ s.toString() +"')) filter ( !regex(?o,'www.example.com/GeographicalCoordinate'))}");
		     vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		     Model modelValuesIntance = vqe.execConstruct();
		     //Valores de la instancia
		     StmtIterator it2 =  modelValuesIntance.listStatements();
	    	 float latitude = 0.0f;
	    	 float longitude = 0.0f;
		     while (it2.hasNext()) {

		    	 Statement stmt2 = it2.next();
		    	 
		    	 RDFNode sV = stmt2.getSubject();
			     RDFNode pV = stmt2.getPredicate();
			     RDFNode oV = stmt2.getObject();
			     
			     
			     int index = oV.toString().indexOf("^");
			     String oFinal =  oV.toString().substring(0, index);
			     
			     if(pV.toString().contains("latitude")) latitude = Float.parseFloat(oFinal);
			     else if(pV.toString().contains("longitude")) longitude = Float.parseFloat(oFinal);
			     //System.out.println(sV.toString() + " " + pV.toString() + " " + oV.toString());
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
    
}
