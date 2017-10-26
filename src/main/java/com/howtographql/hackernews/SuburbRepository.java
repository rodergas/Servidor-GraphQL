package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;

public class SuburbRepository {
	
	private final List<Suburb> Suburbs;
	
	public SuburbRepository(){
		Suburbs = new ArrayList<>();
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
