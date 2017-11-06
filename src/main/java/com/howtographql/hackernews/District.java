package com.howtographql.hackernews;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class District {
	private String idTurtle;
	
	private Integer districtNumber;
	private String districtName;
	
	public District(String idTurtle){
		this.idTurtle = idTurtle;
	}
	
	public District(String districtName, Integer districtNumber){
		this.setDistrictNumber(districtNumber);
		this.setDistrictName(districtName);
	}

	public Integer getDistrictNumber() {
		return Integer.parseInt(modifyScalarValue(connectVirtuoso("districtNumber").get(0)));
	}

	public void setDistrictNumber(Integer districtNumber) {
		this.districtNumber = districtNumber;
	}

	public String getDistrictName() {
		return modifyScalarValue(connectVirtuoso("districtName").get(0));
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	private String getIdTurtle() {
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
	

