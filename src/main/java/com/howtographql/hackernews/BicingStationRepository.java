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
    	
		Query sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
				+ "?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/BicingStation')).}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);

		ResultSet res = vqe.execSelect();
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");
			System.out.println(qs);
			
			
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "<" + subject.toString() + "> <http://www.example.com/stationID> ?stationID."
					+ "<" + subject.toString() + "> <http://www.example.com/stationType> ?stationType."
					+ "<" + subject.toString() + "> <http://www.example.com/stationStreetName> ?stationStreetName."
					+ "<" + subject.toString() + "> <http://www.example.com/stationStatus> ?stationStatus."
					+ "<" + subject.toString() + "> <http://www.example.com/stationStreetNumber> ?stationStreetNumber."
					+ "<" + subject.toString() + "> <http://www.example.com/stationSlotsNumber> ?stationSlotsNumber."
					+ "<" + subject.toString() + "> <http://www.example.com/stationBikesNumber> ?stationBikesNumber."
					+ "<" + subject.toString() + "> <http://www.example.com/stationAltitude> ?stationAltitude."
					+ "<" + subject.toString() + "> <http://www.example.com/locatedIn> ?o." //GeographicalCoordinate
					+ "?o <http://www.example.com/longitude> ?longitude."//GeographicalCoordinate
					+ "?o <http://www.example.com/latitude> ?latitude." //GeographicalCoordinate
					/*
					+ "OPTIONAL {"
							+ "<" + subject.toString() + "> <http://www.example.com/nearByInfrastructure> ?nearByInfrastructure."
							+ "?nearByInfrastructure <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?tipoInfrastructure."
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationID> ?nearByInfrastructureStationID.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationType> ?nearByInfrastructureStationType.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStreetName> ?nearByInfrastructureStationStreetName.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStatus> ?nearByInfrastructureStationStatus.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStreetNumber> ?nearByInfrastructureStationStreetNumber.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationSlotsNumber> ?nearByInfrastructureSlotsNumber.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationBikesNumber> ?nearByInfrastructureStationBikesNumber.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationAltitude> ?nearByInfrastructureStationAltitude.}"
							//+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/locatedIn> ?nearByInfrastructureLocatedIn.}"
							//+ "OPTIONAL { ?nearByInfrastructureLocatedIn <http://www.example.com/longitude> ?nearByInfrastructureLongitude.}"
							//+ "OPTIONAL { ?nearByInfrastructureLocatedIn <http://www.example.com/latitude> ?nearByInfrastructureLatitude.}"
							
							+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopName> ?nearByInfrastructureStopName}." //metrobus
							+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopAddress> ?nearByInfrastructureStopAddress}." //metrobus
							+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopPhone> ?nearByInfrastructureStopPhone}."
							
					+ "}."
					*/	
					
					/*
					+ "OPTIONAL {"
							+ "<" + subject.toString() + "> <http://www.example.com/nearByStation> ?nearByStation."
							+ "?nearByStation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?nearByStationTipoInfrastructure."
							+ "?nearByStation <http://www.example.com/stationID> ?nearByStationStationID."
							+ "?nearByStation <http://www.example.com/stationType> ?nearByStationStationType."
							+ "?nearByStation <http://www.example.com/stationStreetName> ?nearByStationStationStreetName."
							+ "?nearByStation <http://www.example.com/stationStatus> ?nearByStationStationStatus."
							+ "?nearByStation <http://www.example.com/stationStreetNumber> ?nearByStationStationStreetNumber."
							+ "?nearByStation <http://www.example.com/stationSlotsNumber> ?nearByStationStationSlotsNumber."
							+ "?nearByStation <http://www.example.com/stationBikesNumber> ?nearByStationStationBikesNumber."
							+ "?nearByStation <http://www.example.com/stationAltitude> ?nearByStationStationAltitude."
							+ "?nearByStation <http://www.example.com/locatedIn> ?nearByStationLocatedIn." //GeographicalCoordinate
							+ "?nearByStationLocatedIn <http://www.example.com/longitude> ?nearByStationLongitude."//GeographicalCoordinate
							+ "?nearByStationLocatedIn <http://www.example.com/latitude> ?nearByStationLatitude." //GeographicalCoordinate
					+ "}."
					*/
					
				+ "}");
			
			System.out.println(subject.toString());
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res2 = vqe.execSelect();
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {"
					+ "<" + subject.toString() + "> <http://www.example.com/nearByInfrastructure> ?nearByInfrastructure."
					+ "?nearByInfrastructure <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?tipoInfrastructure."
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationID> ?nearByInfrastructureStationID.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationType> ?nearByInfrastructureStationType.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStreetName> ?nearByInfrastructureStationStreetName.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStatus> ?nearByInfrastructureStationStatus.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStreetNumber> ?nearByInfrastructureStationStreetNumber.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationSlotsNumber> ?nearByInfrastructureSlotsNumber.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationBikesNumber> ?nearByInfrastructureStationBikesNumber.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationAltitude> ?nearByInfrastructureStationAltitude.}"
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/locatedIn> ?nearByInfrastructureLocatedIn.}"
					+ "OPTIONAL { ?nearByInfrastructureLocatedIn <http://www.example.com/longitude> ?nearByInfrastructureLongitude.}"
					+ "OPTIONAL { ?nearByInfrastructureLocatedIn <http://www.example.com/latitude> ?nearByInfrastructureLatitude.}"
					
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopName> ?nearByInfrastructureStopName}." //metrobus
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopAddress> ?nearByInfrastructureStopAddress}." //metrobus
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopPhone> ?nearByInfrastructureStopPhone}."
					+ "}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res3 = vqe.execSelect();
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {"
					+ "<" + subject.toString() + "> <http://www.example.com/nearByStation> ?nearByStation."
					+ "?nearByStation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?nearByStationTipoInfrastructure."
					+ "?nearByStation <http://www.example.com/stationID> ?nearByStationStationID."
					+ "?nearByStation <http://www.example.com/stationType> ?nearByStationStationType."
					+ "?nearByStation <http://www.example.com/stationStreetName> ?nearByStationStationStreetName."
					+ "?nearByStation <http://www.example.com/stationStatus> ?nearByStationStationStatus."
					+ "?nearByStation <http://www.example.com/stationStreetNumber> ?nearByStationStationStreetNumber."
					+ "?nearByStation <http://www.example.com/stationSlotsNumber> ?nearByStationStationSlotsNumber."
					+ "?nearByStation <http://www.example.com/stationBikesNumber> ?nearByStationStationBikesNumber."
					+ "?nearByStation <http://www.example.com/stationAltitude> ?nearByStationStationAltitude."
					+ "?nearByStation <http://www.example.com/locatedIn> ?nearByStationLocatedIn." //GeographicalCoordinate
					+ "?nearByStationLocatedIn <http://www.example.com/longitude> ?nearByStationLongitude."//GeographicalCoordinate
					+ "?nearByStationLocatedIn <http://www.example.com/latitude> ?nearByStationLatitude." //GeographicalCoordinate
					+ "}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res4 = vqe.execSelect();
			
			//Attributes BicingStation
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
			ArrayList<BicingStation> nearBicingStation = new ArrayList<>();
			
			//Attributes nearBicingStation
			String nearStationStreetName = "";
			String nearStationType ="";
			Integer nearStationBikesNumber = 0;
			Integer nearStationID = 0;
			Float nearStationAltitude = 0.0f;
			Integer nearStationSlotsNumber = 0;
			Integer nearStationStreetNumber = 0;
			String nearStationStatus = "";
			
			//Attributes nearMetroAndBus
			String nearStopName = "";
			String nearStopAddress = "";
			Integer nearStopPhone = 0;
		
			//Attributes infrastructure
			Float nearLatitude = 0.0f;
			Float nearLongitude = 0.0f;
			ArrayList<Infrastructure> nearByInfrastructure = new ArrayList<>();
			
			
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				System.out.println(qs2);
				
				if(valid(qs2, "longitude")) longitude = Float.parseFloat(modifyScalarValue(qs2.get("longitude").toString()));
				if(valid(qs2, "latitude")) latitude = Float.parseFloat(modifyScalarValue(qs2.get("latitude").toString()));	
				if(valid(qs2, "stationStreetName")) stationStreetName = modifyScalarValue(qs2.get("stationStreetName").toString());
				if(valid(qs2, "stationType")) stationType = modifyScalarValue(qs2.get("stationType").toString());
				if(valid(qs2, "stationBikesNumber")) stationBikesNumber = Integer.parseInt(modifyScalarValue(qs2.get("stationBikesNumber").toString()));
				if(valid(qs2, "stationID")) stationID = Integer.parseInt(modifyScalarValue(qs2.get("stationID").toString()));
				if(valid(qs2, "stationAltitude")) stationAltitude = Float.parseFloat(modifyScalarValue(qs2.get("stationAltitude").toString()));
				if(valid(qs2, "stationSlotsNumber")) stationSlotsNumber = Integer.parseInt(modifyScalarValue(qs2.get("stationSlotsNumber").toString()));
				if(valid(qs2, "stationStreetNumber")) stationStreetNumber = Integer.parseInt(modifyScalarValue(qs2.get("stationStreetNumber").toString()));
				if(valid(qs2, "stationStatus")) stationStatus = modifyScalarValue(qs2.get("stationStatus").toString());

				/*
				if(valid(qs2, "nearByInfrastructure")){
					if(valid(qs2, "tipoInfrastructure")){
						if(qs2.get("tipoInfrastructure").toString().contains("BicingStation")){
							
							if(valid(qs2, "nearByInfrastructureStationStreetName")) nearStationStreetName =  modifyScalarValue(qs2.get("nearByInfrastructureStationStreetName").toString());
							if(valid(qs2, "nearByInfrastructureStationType")) nearStationType = modifyScalarValue(qs2.get("nearByInfrastructureStationType").toString());
							if(valid(qs2, "nearByInfrastructureStationBikesNumber")) nearStationBikesNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStationBikesNumber").toString()));
							if(valid(qs2, "nearByInfrastructureStationID")) nearStationID = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStationID").toString()));
							if(valid(qs2, "nearByInfrastructureStationAltitude")) nearStationAltitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureStationAltitude").toString()));
							if(valid(qs2, "nearByInfrastructureSlotsNumber")) nearStationSlotsNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureSlotsNumber").toString()));
							if(valid(qs2, "nearByInfrastructureStationStreetNumber")) nearStationStreetNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStationStreetNumber").toString()));
							if(valid(qs2, "nearByInfrastructureStationStatus")) nearStationStatus = modifyScalarValue(qs2.get("nearByInfrastructureStationStatus").toString());
							if(valid(qs2, "nearByInfrastructureLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLatitude").toString()));
							if(valid(qs2, "nearByInfrastructureLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLongitude").toString()));
	
							nearByInfrastructure.add(new BicingStation(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "BicingStation", nearStationStreetName,nearStationType,nearStationBikesNumber, nearStationID, nearStationAltitude, nearStationSlotsNumber, nearStationStreetNumber, null,nearStationStatus ));
							
						}else if(qs2.get("tipoInfrastructure").toString().contains("MetroAndBusStop")){
							if(valid(qs2, "nearByInfrastructureStopName")) nearStopName =  modifyScalarValue(qs2.get("nearByInfrastructureStopName").toString());
							if(valid(qs2, "nearByInfrastructureStopAddress")) nearStopAddress = modifyScalarValue(qs2.get("nearByInfrastructureStopAddress").toString());
							if(valid(qs2, "nearByInfrastructureStopPhone")) nearStopPhone = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStopPhone").toString()));
							if(valid(qs2, "nearByInfrastructureLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLatitude").toString()));
							if(valid(qs2, "nearByInfrastructureLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLongitude").toString()));
							
							nearByInfrastructure.add(new MetroAndBusStop(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "MetroAndBusStop" , nearStopAddress,  nearStopPhone, nearStopName));
							
						}
					}
					
				}
				*/
				/*
				if(valid(qs2, "nearByStation")){
					if(valid(qs2, "nearByStationStationStreetName")) nearStationStreetName =  modifyScalarValue(qs2.get("nearByStationStationStreetName").toString());
					if(valid(qs2, "nearByStationStationType")) nearStationType = modifyScalarValue(qs2.get("nearByStationStationType").toString());
					if(valid(qs2, "nearByStationStationBikesNumber")) nearStationBikesNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationStationBikesNumber").toString()));
					if(valid(qs2, "nearByStationStationID")) {System.out.println("iep " + qs2.get("nearByStationStationID").toString() );nearStationID = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationStationID").toString()));}
					if(valid(qs2, "nearByStationStationAltitude")) nearStationAltitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByStationStationAltitude").toString()));
					if(valid(qs2, "nearByStationSlotsNumber")) nearStationSlotsNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationSlotsNumber").toString()));
					if(valid(qs2, "nearByStationStationStreetNumber")) nearStationStreetNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationStationStreetNumber").toString()));
					if(valid(qs2, "nearByStationStationStatus")) nearStationStatus = modifyScalarValue(qs2.get("nearByStationStationStatus").toString());
					if(valid(qs2, "nearByStationLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByStationLatitude").toString()));
					if(valid(qs2, "nearByStationLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByStationLongitude").toString()));
					
					nearBicingStation.add(new BicingStation(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "BicingStation", nearStationStreetName,nearStationType,nearStationBikesNumber, nearStationID, nearStationAltitude, nearStationSlotsNumber, nearStationStreetNumber, null,nearStationStatus ));
				
				}
				*/
				while(res3.hasNext()){
					qs2 = res3.next();
					if(valid(qs2, "nearByInfrastructure")){
						if(valid(qs2, "tipoInfrastructure")){
							if(qs2.get("tipoInfrastructure").toString().contains("BicingStation")){
								
								if(valid(qs2, "nearByInfrastructureStationStreetName")) nearStationStreetName =  modifyScalarValue(qs2.get("nearByInfrastructureStationStreetName").toString());
								if(valid(qs2, "nearByInfrastructureStationType")) nearStationType = modifyScalarValue(qs2.get("nearByInfrastructureStationType").toString());
								if(valid(qs2, "nearByInfrastructureStationBikesNumber")) nearStationBikesNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStationBikesNumber").toString()));
								if(valid(qs2, "nearByInfrastructureStationID")) nearStationID = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStationID").toString()));
								if(valid(qs2, "nearByInfrastructureStationAltitude")) nearStationAltitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureStationAltitude").toString()));
								if(valid(qs2, "nearByInfrastructureSlotsNumber")) nearStationSlotsNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureSlotsNumber").toString()));
								if(valid(qs2, "nearByInfrastructureStationStreetNumber")) nearStationStreetNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStationStreetNumber").toString()));
								if(valid(qs2, "nearByInfrastructureStationStatus")) nearStationStatus = modifyScalarValue(qs2.get("nearByInfrastructureStationStatus").toString());
								if(valid(qs2, "nearByInfrastructureLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLatitude").toString()));
								if(valid(qs2, "nearByInfrastructureLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLongitude").toString()));
		
								nearByInfrastructure.add(new BicingStation(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "BicingStation", nearStationStreetName,nearStationType,nearStationBikesNumber, nearStationID, nearStationAltitude, nearStationSlotsNumber, nearStationStreetNumber, null,nearStationStatus ));
								
							}else if(qs2.get("tipoInfrastructure").toString().contains("MetroAndBusStop")){
								if(valid(qs2, "nearByInfrastructureStopName")) nearStopName =  modifyScalarValue(qs2.get("nearByInfrastructureStopName").toString());
								if(valid(qs2, "nearByInfrastructureStopAddress")) nearStopAddress = modifyScalarValue(qs2.get("nearByInfrastructureStopAddress").toString());
								if(valid(qs2, "nearByInfrastructureStopPhone")) nearStopPhone = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStopPhone").toString()));
								if(valid(qs2, "nearByInfrastructureLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLatitude").toString()));
								if(valid(qs2, "nearByInfrastructureLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLongitude").toString()));
								
								nearByInfrastructure.add(new MetroAndBusStop(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "MetroAndBusStop" , nearStopAddress,  nearStopPhone, nearStopName));
								
							}
						}					
					}
				}
				
				while(res4.hasNext()){
					qs2 = res4.next();
					if(valid(qs2, "nearByStation")){
						if(valid(qs2, "nearByStationStationStreetName")) nearStationStreetName =  modifyScalarValue(qs2.get("nearByStationStationStreetName").toString());
						if(valid(qs2, "nearByStationStationType")) nearStationType = modifyScalarValue(qs2.get("nearByStationStationType").toString());
						if(valid(qs2, "nearByStationStationBikesNumber")) nearStationBikesNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationStationBikesNumber").toString()));
						if(valid(qs2, "nearByStationStationID")) {System.out.println("iep " + qs2.get("nearByStationStationID").toString() );nearStationID = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationStationID").toString()));}
						if(valid(qs2, "nearByStationStationAltitude")) nearStationAltitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByStationStationAltitude").toString()));
						if(valid(qs2, "nearByStationSlotsNumber")) nearStationSlotsNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationSlotsNumber").toString()));
						if(valid(qs2, "nearByStationStationStreetNumber")) nearStationStreetNumber = Integer.parseInt(modifyScalarValue(qs2.get("nearByStationStationStreetNumber").toString()));
						if(valid(qs2, "nearByStationStationStatus")) nearStationStatus = modifyScalarValue(qs2.get("nearByStationStationStatus").toString());
						if(valid(qs2, "nearByStationLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByStationLatitude").toString()));
						if(valid(qs2, "nearByStationLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByStationLongitude").toString()));
						
						nearBicingStation.add(new BicingStation(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "BicingStation", nearStationStreetName,nearStationType,nearStationBikesNumber, nearStationID, nearStationAltitude, nearStationSlotsNumber, nearStationStreetNumber, null,nearStationStatus ));
					
					}
				}

				System.out.println("-----------------------");
			}
			
			BicingStations.add(new BicingStation(nearByInfrastructure, new GeographicalCoordinate(longitude, latitude), "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, nearBicingStation, stationStatus));
			nearByInfrastructure = new ArrayList<>();
			nearBicingStation = new ArrayList<>();
		}
			
	}
	
    public List<BicingStation> getAllBicingStations() {
        return BicingStations;
    }
    
    public String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }
    
    public boolean valid(QuerySolution qs , String value){
    	return qs.get(value) != null;
    }
}
