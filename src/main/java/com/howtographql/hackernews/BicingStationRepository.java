package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class BicingStationRepository {
	private final List<BicingStation> BicingStations;
	
	public BicingStationRepository(){
		
		BicingStations = new ArrayList<>();
		VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
    	
    	Query sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o,'www.example.com/BicingStation'))}");
    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	Model modelNameInstance = vqe.execConstruct();
    	
    	StmtIterator it =  modelNameInstance.listStatements();
    	
    	while (it.hasNext()) {
		     Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     System.out.println(s.toString() + " " + p.toString() + " " + o.toString());
		     
		     sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'"+ s.toString() +"')) filter ( !regex(?o,'www.example.com/BicingStation'))}");
		     vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		     Model modelValuesIntance = vqe.execConstruct();
		     //Valores de la instancia
		     StmtIterator it2 =  modelValuesIntance.listStatements();

			String stationStreetName = "";
			String stationType ="";
			Integer stationBikesNumber = 0;
			Integer stationID = 0;
			Float stationAltitude = 0.0f;
			Integer stationSlotsNumber = 0;
			Integer stationStreetNumber = 0;
			String stationStatus = "";
		     while (it2.hasNext()) {

		    	 Statement stmt2 = it2.next();
		    	 
		    	 RDFNode sV = stmt2.getSubject();
			     RDFNode pV = stmt2.getPredicate();
			     RDFNode oV = stmt2.getObject();
			     
			     
			     int index = oV.toString().indexOf("^");
			     String oFinal =  oV.toString().substring(0, index);
			     
			     if(pV.toString().contains("stationStreetName")) stationStreetName = oFinal;
			     else if(pV.toString().contains("stationType")) stationType = oFinal;
			     else if(pV.toString().contains("stationBikesNumber")) stationBikesNumber = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationID")) stationID = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationAltitude")) stationAltitude = Float.parseFloat(oFinal);
			     else if(pV.toString().contains("stationSlotsNumber")) stationSlotsNumber = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationStreetNumber")) stationStreetNumber = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationStatus")) stationStatus = oFinal;
		     }
		     BicingStations.add(new BicingStation(null, null, "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, null, stationStatus));
		}
	}
	
    public List<BicingStation> getAllBicingStations() {
        return BicingStations;
    }
}
