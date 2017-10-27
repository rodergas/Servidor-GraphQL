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

public class SuburbRepository {
	
	private final List<Suburb> Suburbs;
	
	public SuburbRepository(){
		Suburbs = new ArrayList<>();
		
VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
    	Query sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example4> WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/Suburb'))}");

    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
    	
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/suburbName> ?suburbName}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/belongsTo> ?district}."
					+ "OPTIONAL { ?district <http://www.example.com/districtName> ?districtName}."
					+ "OPTIONAL { ?district <http://www.example.com/districtNumber> ?districtNumber}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res2 = vqe.execSelect();
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {"
					+ "<" + subject.toString() + "> <http://www.example.com/providesStop> ?provideStop."
					+ "OPTIONAL { ?provideStop <http://www.example.com/stopName> ?provideStopName.}"
					+ "OPTIONAL { ?provideStop <http://www.example.com/stopAddress> ?provideStopAddress.}"
					+ "OPTIONAL { ?provideStop <http://www.example.com/stopPhone> ?provideStopPhone.}"
					+ "OPTIONAL { ?provideStop <http://www.example.com/locatedIn> ?provideStopLocation.}"
					+ "OPTIONAL { ?provideStopLocation <http://www.example.com/longitude> ?provideStopLongitude.}"
					+ "OPTIONAL { ?provideStopLocation <http://www.example.com/latitude> ?provideStopLatitude.}"
					+ "}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res3 = vqe.execSelect();
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {"
					+ "<" + subject.toString() + "> <http://www.example.com/providesStop> ?provideStop."
					+ "OPTIONAL {"
					+ "		?provideStop <http://www.example.com/nearByInfrastructure> ?nearByInfrastructure."
					
					+ "OPTIONAL { ?nearByInfrastructure <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?tipoInfrastructure.}"
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
					+ "}"
					
					+ "}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res4 = vqe.execSelect();
			
			
			String suburbName = "";
			String districtName = "";
			Integer districtNumber = 0;
			
			String provideStopName = "";
			String provideStopAddress = "";
			Integer provideStopPhone = 0;
			Float longitude = 0.0f;
			Float latitude = 0.0f;
			ArrayList<MetroAndBusStop> providesStop = new ArrayList<>();
			
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
			
			ArrayList<Infrastructure> nearInfrastructureProvideStop = new ArrayList<>();
			
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				if(valid(qs2, "districtName")) districtName = modifyScalarValue(qs2.get("districtName").toString());	
				if(valid(qs2, "districtNumber")) districtNumber = Integer.parseInt(modifyScalarValue(qs2.get("districtNumber").toString()));
				if(valid(qs2, "suburbName")) suburbName = modifyScalarValue(qs2.get("suburbName").toString());
				
				while(res3.hasNext()){
					qs2 = res3.next();
					System.out.println(qs2);
					if(valid(qs2, "provideStopName")) provideStopName = modifyScalarValue(qs2.get("provideStopName").toString());	
					if(valid(qs2, "provideStopAddress")) provideStopAddress = modifyScalarValue(qs2.get("provideStopAddress").toString());
					if(valid(qs2, "provideStopPhone")) provideStopPhone = Integer.parseInt(modifyScalarValue(qs2.get("provideStopPhone").toString()));
					if(valid(qs2, "provideStopLongitude")) longitude = Float.parseFloat(modifyScalarValue(qs2.get("provideStopLongitude").toString()));
					if(valid(qs2, "provideStopLatitude")) latitude = Float.parseFloat(modifyScalarValue(qs2.get("provideStopLatitude").toString()));
					while(res4.hasNext()){
						qs2 = res4.next();
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
			
									nearInfrastructureProvideStop.add(new BicingStation(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "BicingStation", nearStationStreetName,nearStationType,nearStationBikesNumber, nearStationID, nearStationAltitude, nearStationSlotsNumber, nearStationStreetNumber, null,nearStationStatus ));
									
								}else if(qs2.get("tipoInfrastructure").toString().contains("MetroAndBusStop")){
									if(valid(qs2, "nearByInfrastructureStopName")) nearStopName =  modifyScalarValue(qs2.get("nearByInfrastructureStopName").toString());
									if(valid(qs2, "nearByInfrastructureStopAddress")) nearStopAddress = modifyScalarValue(qs2.get("nearByInfrastructureStopAddress").toString());
									if(valid(qs2, "nearByInfrastructureStopPhone")) nearStopPhone = Integer.parseInt(modifyScalarValue(qs2.get("nearByInfrastructureStopPhone").toString()));
									if(valid(qs2, "nearByInfrastructureLatitude")) nearLatitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLatitude").toString()));
									if(valid(qs2, "nearByInfrastructureLongitude")) nearLongitude = Float.parseFloat(modifyScalarValue(qs2.get("nearByInfrastructureLongitude").toString()));
									
									nearInfrastructureProvideStop.add(new MetroAndBusStop(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "MetroAndBusStop" , nearStopAddress,  nearStopPhone, nearStopName));
									
								}
							}	
						}
					}
					providesStop.add(new MetroAndBusStop(nearInfrastructureProvideStop, new GeographicalCoordinate(longitude, latitude), "MetroAndBusStop", provideStopAddress, provideStopPhone, provideStopName));
					nearInfrastructureProvideStop = new ArrayList<>();
				}
				

				
			}
			Suburbs.add(new Suburb(suburbName, new District(districtName, districtNumber), providesStop));
		}
	}

	
    public List<Suburb> getAllSuburbs() {
        return Suburbs;
    }
    
    
    public boolean valid(QuerySolution qs , String value){
    	return qs.get(value) != null;
    }
    
    public String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }
}
