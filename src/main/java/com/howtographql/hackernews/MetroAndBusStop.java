package com.howtographql.hackernews;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class MetroAndBusStop implements Infrastructure {
	private String idTurtle;
	
	private  ArrayList<Infrastructure> nearByInfrastructure;
	private  GeographicalCoordinate locatedIn;
	private  String infrastructureType;
	private  String stopAddress;
	private  Integer stopPhone;
	private  String stopName;
	
	public MetroAndBusStop(String idTurtle){
		this.setIdTurtle(idTurtle);
	}
	
	public MetroAndBusStop(ArrayList<Infrastructure> nearByInfrastructure, GeographicalCoordinate locatedIn, String infrastructureType, String stopAddress, Integer stopPhone, String stopName){
		this.nearByInfrastructure = nearByInfrastructure;
		this.locatedIn = locatedIn;
		this.infrastructureType = infrastructureType;
		this.stopAddress = stopAddress;
		this.stopPhone = stopPhone;
		this.stopName = stopName;

	}
	
	public ArrayList<Infrastructure> getNearByInfrastructure(){
		return nearByInfrastructure;
	}
	
	public GeographicalCoordinate getLocatedIn(){
		return new GeographicalCoordinate(connectVirtuoso("locatedIn").get(0));
	}
	
	public String getInfrastructureType(){
		return "MetroAndBusStop";
	}

	public String getStopAddress() {
		return modifyScalarValue(connectVirtuoso("stopAddress").get(0));
	}

	public Integer getStopPhone() {
		return Integer.parseInt(modifyScalarValue(connectVirtuoso("stopPhone").get(0)));
	}

	public String getStopName() {
		return modifyScalarValue(connectVirtuoso("stopName").get(0));
	}

	private String getIdTurtle() {
		return idTurtle;
	}

	private void setIdTurtle(String idTurtle) {
		this.idTurtle = idTurtle;
	}
	
    private ArrayList<String> connectVirtuoso(String value){
		VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/Example4> WHERE {"
				+ "OPTIONAL { <"+ this.getIdTurtle() +"> <http://www.example.com/"+  value + "> ?valor}."
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
