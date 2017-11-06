package com.howtographql.hackernews;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class Suburb {
	
	private String idTurtle;
	
	private String suburbName;
	private District belongsTo;
	private ArrayList<MetroAndBusStop> providesStop;
	
	public Suburb(String id){
		this.setIdTurtle(id);	
	}
	
	public Suburb(String suburbName, District belongsTo, ArrayList<MetroAndBusStop> providesStop){
		this.setSuburbName(suburbName);
		this.setBelongsTo(belongsTo);
		this.setProvidesStop(providesStop);
	}

	public String getSuburbName() {
		return modifyScalarValue(connectVirtuoso("suburbName").get(0));
	}

	public void setSuburbName(String suburbName) {
		this.suburbName = suburbName;
	}

	public District getBelongsTo() {
		return new District(connectVirtuoso("belongsTo").get(0));
	}

	public void setBelongsTo(District belongsTo) {
		this.belongsTo = belongsTo;
	}

	public ArrayList<MetroAndBusStop> getProvidesStop() {
		ArrayList<String> provideStops = connectVirtuoso("providesStop");
		ArrayList<MetroAndBusStop> metroAndBus = new ArrayList<>();
		
	    for(String id:provideStops) metroAndBus.add(new MetroAndBusStop(id));  
	          
		return metroAndBus;
	}

	public void setProvidesStop(ArrayList<MetroAndBusStop> providesStop) {
		this.providesStop = providesStop;
	}

	public String getIdTurtle() {
		return idTurtle;
	}

	private void setIdTurtle(String idTurtle) {
		this.idTurtle = idTurtle;
	}
	
    private String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
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
}
