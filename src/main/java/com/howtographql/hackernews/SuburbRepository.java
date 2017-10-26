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
			
			String suburbName = "";
			String districtName = "";
			Integer districtNumber = 0;
			
			String provideStopName = "";
			String provideStopAddress = "";
			Integer provideStopPhone = 0;
			Float longitude = 0.0f;
			Float latitude = 0.0f;
			ArrayList<MetroAndBusStop> providesStop = new ArrayList<>();
			
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				if(valid(qs2, "districtName")) districtName = modifyScalarValue(qs2.get("districtName").toString());	
				if(valid(qs2, "districtNumber")) districtNumber = Integer.parseInt(modifyScalarValue(qs2.get("districtNumber").toString()));
				if(valid(qs2, "suburbName")) suburbName = modifyScalarValue(qs2.get("suburbName").toString());
				
				while(res3.hasNext()){
					qs2 = res3.next();
					if(valid(qs2, "provideStopName")) provideStopName = modifyScalarValue(qs2.get("provideStopName").toString());	
					if(valid(qs2, "provideStopAddress")) provideStopAddress = modifyScalarValue(qs2.get("provideStopAddress").toString());
					if(valid(qs2, "provideStopPhone")) provideStopPhone = Integer.parseInt(modifyScalarValue(qs2.get("provideStopPhone").toString()));
					if(valid(qs2, "provideStopLongitude")) longitude = Float.parseFloat(modifyScalarValue(qs2.get("provideStopLongitude").toString()));
					if(valid(qs2, "provideStopLatitude")) latitude = Float.parseFloat(modifyScalarValue(qs2.get("provideStopLatitude").toString()));
					
					providesStop.add(new MetroAndBusStop(null, new GeographicalCoordinate(longitude, latitude), "MetroAndBusStop", provideStopAddress, provideStopPhone, provideStopName));
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
