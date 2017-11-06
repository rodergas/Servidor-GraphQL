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

public class DistrictRepository {
	
	private final List<District> Districts;
	
	public DistrictRepository(){
		Districts = new ArrayList<>();
		
    	VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    	
    	Query sparql = QueryFactory.create("Select ?subject ?districtName ?districtNumber FROM <http://localhost:8890/Example4> WHERE {"
    			+ "OPTIONAL { ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/District>}."
    			+ "OPTIONAL {?subject <http://www.example.com/districtName> ?districtName}."
				+ "OPTIONAL {?subject <http://www.example.com/districtNumber> ?districtNumber}."
				+ "}");

    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
    	
		while(res.hasNext()){
			QuerySolution qs = res.next();
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			
			String districtName = "";
			Integer districtNumber = 0;

			if(qs.contains("districtName")) districtName = modifyScalarValue(qs.get("districtName").toString());	
			if(qs.contains("districtNumber")) districtNumber = Integer.parseInt(modifyScalarValue(qs.get("districtNumber").toString()));

			 Districts.add(new District(districtName, districtNumber));
		}
	}
	
    public List<District> getAllDistricts() {
        return Districts;
    }
    
    
    public String modifyScalarValue(String value){
    	int index = value.toString().indexOf("^");
		String resultat =  value.toString().substring(0, index);
		return resultat;
    }

}
