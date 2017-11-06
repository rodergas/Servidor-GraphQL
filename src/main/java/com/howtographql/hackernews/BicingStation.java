package com.howtographql.hackernews;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;


public class BicingStation implements Infrastructure {
	private String idTurtle;
	
	private  ArrayList<Infrastructure> nearByInfrastructure;
	private  GeographicalCoordinate locatedIn;
	private  String infrastructureType;
	private  String stationStreetName;
	private  String stationType;
	private  Integer stationBikesNumber;
	private  Integer stationID;
	private  Float stationAltitude;
	private  Integer stationSlotsNumber;
	private  Integer stationStreetNumber;
	private  ArrayList<BicingStation> nearByStation;
	private  String stationStatus;
	
	
	public BicingStation(String idTurtle){
		this.setIdTurtle(idTurtle);
	}
	public ArrayList<Infrastructure> getNearByInfrastructure(){
		ArrayList<String> nearByInfrastructure = connectVirtuoso("http://www.example.com/nearByInfrastructure");
		ArrayList<Infrastructure> nearByInfrastructures = new ArrayList<>();

	    for(String id: nearByInfrastructure){
	    	if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", id).get(0).equals("http://www.example.com/MetroAndBusStop")) nearByInfrastructures.add(new MetroAndBusStop(id));   
	    	else if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", id).get(0).equals("http://www.example.com/BicingStation")) nearByInfrastructures.add(new BicingStation(id));  
	    } 

		return nearByInfrastructures;
	}

	public String getInfrastructureType(){
		return "BicingStation";
	}
	
	public GeographicalCoordinate getLocatedIn(){
		return new GeographicalCoordinate(connectVirtuoso("http://www.example.com/locatedIn").get(0));
	}
	

	public String getStationStreetName() {
		return modifyScalarValue(connectVirtuoso("http://www.example.com/stationStreetName").get(0));
	}

	public String getStationType() {
		return modifyScalarValue(connectVirtuoso("http://www.example.com/stationType").get(0));
	}

	public Integer getStationBikesNumber() {
		return Integer.parseInt(modifyScalarValue(connectVirtuoso("http://www.example.com/stationBikesNumber").get(0)));
	}

	public Integer getStationID() {
		return Integer.parseInt(modifyScalarValue(connectVirtuoso("http://www.example.com/stationID").get(0)));
	}

	public Float getStationAltitude() {
		return Float.parseFloat(modifyScalarValue(connectVirtuoso("http://www.example.com/stationAltitude").get(0)));
	}

	public Integer getStationSlotsNumber() {
		return Integer.parseInt(modifyScalarValue(connectVirtuoso("http://www.example.com/stationSlotsNumber").get(0)));
	}

	public Integer getStationStreetNumber() {
		return Integer.parseInt(modifyScalarValue(connectVirtuoso("http://www.example.com/stationStreetNumber").get(0)));
	}

	public ArrayList<BicingStation> getNearByStation() {
		ArrayList<String> nearByStation = connectVirtuoso("http://www.example.com/nearByStation");
		ArrayList<BicingStation> nearByStations = new ArrayList<>();
		
	    for(String id:nearByStation) nearByStations.add(new BicingStation(id));  
	          
		return nearByStations;
	}

	public String getStationStatus() {
		return modifyScalarValue(connectVirtuoso("http://www.example.com/stationStatus").get(0));
	}
	private String getIdTurtle() {
		return idTurtle;
	}
	private void setIdTurtle(String idTurtle) {
		this.idTurtle = idTurtle;
	}
	
    private ArrayList<String> connectVirtuoso(String value, String id){
		VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/Example4> WHERE {"
				+ "OPTIONAL { <"+ id +"> <"+  value + "> ?valor}."
				+ "}");

    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
		ArrayList<String> valor = new ArrayList<>();
		
		while(res.hasNext()){
			QuerySolution qs = res.next();
			valor.add(qs.get("?valor").toString());
		}
    	
		graph.close();
		return valor;
    }
    
    private ArrayList<String> connectVirtuoso(String value){
		VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/Example4> WHERE {"
				+ "OPTIONAL { <"+ this.getIdTurtle() +"> <"+  value + "> ?valor}."
				+ "}");

    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
		ArrayList<String> valor = new ArrayList<>();
		
		while(res.hasNext()){
			QuerySolution qs = res.next();
			valor.add(qs.get("?valor").toString());
		}
    	
		graph.close();
		return valor;
    }
    
    private String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }

}
