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
    	
    	Query sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example4> WHERE {?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/District'))}");

    	VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		ResultSet res = vqe.execSelect();
    	
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/districtName> ?districtName}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/districtNumber> ?districtNumber}."
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
			ResultSet res2 = vqe.execSelect();
			
			String districtName = "";
			Integer districtNumber = 0;
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				if(valid(qs2, "districtName")) districtName = modifyScalarValue(qs2.get("districtName").toString());	
				if(valid(qs2, "districtNumber")) districtNumber = Integer.parseInt(modifyScalarValue(qs2.get("districtNumber").toString()));
			}
			 Districts.add(new District(districtName, districtNumber));
		}
	}
	
    public List<District> getAllDistricts() {
        return Districts;
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
