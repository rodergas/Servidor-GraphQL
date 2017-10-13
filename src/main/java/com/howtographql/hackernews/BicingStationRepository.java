package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RSIterator;
import org.apache.jena.rdf.model.ReifiedStatement;
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
    	
		
		
    	//Query sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/BicingStation'))}");
    	

		/*
		Query sparql = QueryFactory.create(" Select * FROM <http://localhost:8890/Example4> WHERE {"
				+ "?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/BicingStation'))."
				+ "  ?s ?p2 ?o2 .  "
				+ "OPTIONAL {?o2 <http://www.example.com/longitude> ?longitude}."
				+ "OPTIONAL {?o2 <http://www.example.com/latitude> ?latitude}."
				
				+ "OPTIONAL {?o2 <http://www.example.com/stationID> ?nearStationID}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationType> ?nearStationType}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationStreetName> ?nearStationStreetName}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationStatus> ?nearStationStatus}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationStreetNumber> ?nearStationStreetNumber}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationSlotsNumber> ?nearStationSlotsNumber}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationBikesNumber> ?nearStationBikesNumber}."
				+ "OPTIONAL {?o2 <http://www.example.com/stationAltitude> ?nearStationAltitude}."
				
				+ "OPTIONAL {?o2 <http://www.example.com/locatedIn> ?o3. ?o3 <http://www.example.com/longitude> ?longitudeNear.}"
				+ "OPTIONAL {?o2 <http://www.example.com/locatedIn> ?o3. ?o3 <http://www.example.com/latitude> ?latitudeNear.}"
				+ "}");
				*/
		Query sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
				+ "?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/BicingStation')).}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	//Model modelNameInstance = vqe.execConstruct();
		ResultSet res = vqe.execSelect();
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");
			
			
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationID> ?stationID}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationType> ?stationType}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationStreetName> ?stationStreetName}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationStatus> ?stationStatus}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationStreetNumber> ?stationStreetNumber}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationSlotsNumber> ?stationSlotsNumber}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationBikesNumber> ?stationBikesNumber}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationAltitude> ?stationAltitude}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/locatedIn> ?o}." //GeographicalCoordinate
					+ "OPTIONAL {?o <http://www.example.com/longitude> ?longitude}."//GeographicalCoordinate
					+ "OPTIONAL {?o <http://www.example.com/latitude> ?latitude}.}" //GeographicalCoordinate
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/nearByInfrastructure> ?nearByInfrastructure" //nearByInfrastructures
					+ "");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    	//Model modelNameInstance = vqe.execConstruct();
			ResultSet res2 = vqe.execSelect();
			
			String stationStreetName = "";
			String stationType ="";
			Integer stationBikesNumber = 0;
			Integer stationID = 0;
			Float stationAltitude = 0.0f;
			Integer stationSlotsNumber = 0;
			Integer stationStreetNumber = 0;
			String stationStatus = "";
			Float latitude = 0.0f;
			Float longitude = 0.0f;
			 
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				
				String resultat = "";
				
				if(qs2.get("longitude") != null){
					resultat = modifyScalarValue(qs2.get("longitude").toString());

					longitude = Float.parseFloat(resultat);
				}
				if(qs2.get("latitude") != null){
					resultat = modifyScalarValue(qs2.get("latitude").toString());
					latitude = Float.parseFloat(resultat);		
				}
				if(qs2.get("stationStreetName") != null){
					resultat = modifyScalarValue(qs2.get("stationStreetName").toString());
					stationStreetName = resultat;
				}
				if(qs2.get("stationType") != null){
					resultat = modifyScalarValue(qs2.get("stationType").toString());
					stationType = resultat;
				}
				if(qs2.get("stationBikesNumber") != null){
					resultat = modifyScalarValue(qs2.get("stationBikesNumber").toString());
					stationBikesNumber = Integer.parseInt(resultat);
				}
				if(qs2.get("stationID") != null){
					resultat = modifyScalarValue(qs2.get("stationID").toString());
					stationID = Integer.parseInt(resultat);
				}
				if(qs2.get("stationAltitude") != null){
					resultat = modifyScalarValue(qs2.get("stationAltitude").toString());
					stationAltitude = Float.parseFloat(resultat);
				}
				if(qs2.get("stationSlotsNumber") != null){
					resultat = modifyScalarValue(qs2.get("stationSlotsNumber").toString());
					stationSlotsNumber = Integer.parseInt(resultat);
				}
				if(qs2.get("stationStreetNumber") != null){
					resultat = modifyScalarValue(qs2.get("stationStreetNumber").toString());
					stationStreetNumber = Integer.parseInt(resultat);
				}
				if(qs2.get("stationStatus") != null){
					resultat = modifyScalarValue(qs2.get("stationStatus").toString());
					stationStatus = resultat;
				}
				
				if(qs2.get("nearByInfrastructure") != null){
					
				}

				
			}
			
			BicingStations.add(new BicingStation(null, new GeographicalCoordinate(longitude, latitude), "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, null, stationStatus));
		}
			



		/*
    	
    	StmtIterator it =  modelNameInstance.listStatements();

	     GeographicalCoordinate locatedIn;
		 String stationStreetName = "";
		 String stationType ="";
		 Integer stationBikesNumber = 0;
		 Integer stationID = 0;
		 Float stationAltitude = 0.0f;
		 Integer stationSlotsNumber = 0;
		 Integer stationStreetNumber = 0;
		 String stationStatus = "";
		 Float latitude = 0.0f;
		 Float longitude = 0.0f;
		 ArrayList<Infrastructure> infrastructures = new ArrayList<>();
    	while(it.hasNext()){
    		Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     
		     System.out.println("bici "  + s.toString() + " " + p.toString() + " " + o.toString());
		     
		     int index = o.toString().indexOf("^");
		     String oFinal = "";
		     if(index >= 0){
		    	 oFinal =  o.toString().substring(0, index);
		     
		     
				 if(p.toString().contains("stationStreetName")) stationStreetName = oFinal;
			     else if(p.toString().contains("stationType")) stationType = oFinal;
			     else if(p.toString().contains("stationBikesNumber")) stationBikesNumber = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationID")) stationID = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationAltitude")) stationAltitude = Float.parseFloat(oFinal);
			     else if(p.toString().contains("stationSlotsNumber")) stationSlotsNumber = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationStreetNumber")) stationStreetNumber = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationStatus")) stationStatus = oFinal;
			     
		     }else{
		    	 //Embedded object
		    	 
		     }
		     
    	}
    	*/
    	//BicingStations.add(new BicingStation(null, new GeographicalCoordinate(longitude, latitude), "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, null, stationStatus));
    	
    	/*
    	while (it.hasNext()) {
		     Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     System.out.println("bici "  + s.toString() + " " + p.toString() + " " + o.toString());
		     
		     sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'"+ s.toString() +"')) filter ( !regex(?o,'www.example.com/BicingStation'))}");
		     vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		     Model modelValuesIntance = vqe.execConstruct();
		     //Valores de la instancia
		     StmtIterator it2 =  modelValuesIntance.listStatements();
		    GeographicalCoordinate locatedIn;
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
			     String oFinal = "";
			     if(index >= 0) oFinal =  oV.toString().substring(0, index); //ScalarValue
			     else {
			    	 //ObjectValue
			    	 sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'"+ oV.toString() +"'))}");
				     vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
				     Model objectValues = vqe.execConstruct();
				     StmtIterator it3 =  objectValues.listStatements();
				     while (it3.hasNext()) {

				    	 Statement stmt3 = it3.next();
				    	 
				    	 RDFNode sObjectValue = stmt3.getSubject();
					     RDFNode pObjectValue = stmt3.getPredicate();
					     RDFNode oObjectValue = stmt3.getObject();
					     
					     System.out.println(sObjectValue.toString() + " " + pObjectValue.toString() + " " + oObjectValue.toString());
					     
					     
				     }
			     }
			     
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
		*/
	}
	
    public List<BicingStation> getAllBicingStations() {
        return BicingStations;
    }
    
    public String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }
}
