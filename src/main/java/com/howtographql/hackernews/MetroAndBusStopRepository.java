package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class MetroAndBusStopRepository {
	
	private final List<MetroAndBusStop> MetroAndBusStops;
	
	public MetroAndBusStopRepository(){
		MetroAndBusStops = new ArrayList<>();
		VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
		Query sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
				+ "?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/MetroAndBusStop')).}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		
		ResultSet res = vqe.execSelect();
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");

			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {<" +  subject.toString() + "> <http://www.example.com/stopName> ?stopName}." //metrobus
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stopAddress> ?stopAddress}." //metrobus
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stopPhone> ?stopPhone}."
					+ "<" + subject.toString() + "> <http://www.example.com/locatedIn> ?o." //GeographicalCoordinate
					+ "?o <http://www.example.com/longitude> ?longitude."//GeographicalCoordinate
					+ "?o <http://www.example.com/latitude> ?latitude." //GeographicalCoordinate			
				+ "}");
			
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
			
			//Attributes MetroAndBusStop
			String stopName = "";
			String stopAddress = "";
			Integer stopPhone = 0;
			Float latitude = 0.0f;
			Float longitude = 0.0f;
			
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
				
				if(valid(qs2, "stopName")) stopName =  modifyScalarValue(qs2.get("stopName").toString());
				if(valid(qs2, "stopAddress")) stopAddress = modifyScalarValue(qs2.get("stopAddress").toString());
				if(valid(qs2, "stopPhone")) stopPhone = Integer.parseInt(modifyScalarValue(qs2.get("stopPhone").toString()));
				if(valid(qs2, "latitude")) latitude = Float.parseFloat(modifyScalarValue(qs2.get("latitude").toString()));
				if(valid(qs2, "longitude")) longitude = Float.parseFloat(modifyScalarValue(qs2.get("longitude").toString()));
				
				while (res3.hasNext()){
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
				MetroAndBusStops.add(new MetroAndBusStop(nearByInfrastructure, new GeographicalCoordinate(longitude, latitude), "MetroAndBusStop" , stopAddress,  stopPhone, stopName));
			}
			
			
		
		}
	}
	
    public List<MetroAndBusStop> getAllMetroAndBusStops() {
        return MetroAndBusStops;
    }
    
    public MetroAndBusStop getMetroAndBusStop(String name){
    	
    	VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
		Query sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
				+ "OPTIONAL {<" +  name + "> <http://www.example.com/stopName> ?stopName}." //metrobus
				+ "OPTIONAL {<" + name + "> <http://www.example.com/stopAddress> ?stopAddress}." //metrobus
				+ "OPTIONAL {<" + name + "> <http://www.example.com/stopPhone> ?stopPhone}."
			+ "}");
		
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
		
		String stopName = "";
		String stopAddress = "";
		Integer stopPhone = 0;
		while(res.hasNext()){
			QuerySolution qs = res.next();
			if(qs.contains("stopName")) stopName =  modifyScalarValue(qs.get("stopName").toString());
			if(qs.contains("stopAddress")) stopAddress = modifyScalarValue(qs.get("stopAddress").toString());
			if(qs.contains("stopPhone")) stopPhone = Integer.parseInt(modifyScalarValue(qs.get("stopPhone").toString()));
			
		}
		return new MetroAndBusStop(null, null, "MetroAndBusStop" , stopAddress,  stopPhone, stopName);
		
		
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
