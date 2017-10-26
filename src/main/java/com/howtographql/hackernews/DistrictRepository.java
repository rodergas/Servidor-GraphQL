package com.howtographql.hackernews;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;

public class DistrictRepository {
	
	private final List<District> Districts;
	
	public DistrictRepository(){
		Districts = new ArrayList<>();
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
