package com.robertalmar.tfg.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Component;

import com.robertalmar.tfg.model.BicingStation;
import com.robertalmar.tfg.model.GeographicalCoordinate;
import com.robertalmar.tfg.model.Infrastructure;
import com.robertalmar.tfg.query.SparqlQueryProvider;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

@Component
public class BicingStationRepository extends BaseRepository {
	
	private final List<BicingStation> bicingStations;
	
	
	public BicingStationRepository(VirtGraph graph, SparqlQueryProvider queryProvider){
		super(graph, queryProvider);
		
		bicingStations = new ArrayList<>();
		
		executeQuery();
	}
		
    	
	private void executeQuery() {	
		Query sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
				+ "?s ?p ?o filter ( regex(?s,'www.instance.com')) filter ( regex(?o, 'www.example.com/BicingStation')).}");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);

		ResultSet res = vqe.execSelect();
		while(res.hasNext()){
			QuerySolution qs = res.next();
			RDFNode subject = qs.get("s");
			
			
			
			sparql = QueryFactory.create("Select * FROM <http://localhost:8890/Example4> WHERE {"
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationID> ?stationID}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationType> ?stationType}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationStreetName> ?stationStreetName}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationStatus> ?stationStatus}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationStreetNumber> ?stationStreetNumber}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationSlotsNumber> ?stationSlotsNumber}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationBikesNumber> ?stationBikesNumber}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/stationAltitude> ?stationAltitude}."
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/locatedIn> ?o}." //GeographicalCoordinate
					+ "OPTIONAL {?o <http://www.example.com/longitude> ?longitude}."//GeographicalCoordinate
					+ "OPTIONAL {?o <http://www.example.com/latitude> ?latitude}." //GeographicalCoordinate
					+ "OPTIONAL {<" + subject.toString() + "> <http://www.example.com/nearByInfrastructure> ?nearByInfrastructure}." //nearByInfrastructures
					+ "OPTIONAL { ?nearByInfrastructure <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?tipoInfrastructure}." //tipoifnrastructure
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationID> ?nearByInfrastructureStationID}." //biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationType> ?nearByInfrastructureStationType}." //biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStreetName> ?nearByInfrastructureStationStreetName}." //biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStatus> ?nearByInfrastructureStationStatus}." //biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationStreetNumber> ?nearByInfrastructureStationStreetNumber}."//biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/slotsNumber> ?nearByInfrastructureSlotsNumber}."//biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationBikesNumber> ?nearByInfrastructureStationBikesNumber}."//biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stationAltitude> ?nearByInfrastructureStationAltitude}."//biciStation
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopName> ?nearByInfrastructureStopName}." //metrobus
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopAddress> ?nearByInfrastructureStopAddress}." //metrobus
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/stopPhone> ?nearByInfrastructureStopPhone}." //metrobus
					+ "OPTIONAL { ?nearByInfrastructure <http://www.example.com/locatedIn> ?nearByInfrastructureLocatedIn}." //GeographicalCoordinateNear
					+ "OPTIONAL { ?nearByInfrastructureLocatedIn <http://www.example.com/longitude> ?nearByInfrastructureLongitude}."//GeographicalCoordinateNear
					+ "OPTIONAL { ?nearByInfrastructureLocatedIn <http://www.example.com/latitude> ?nearByInfrastructureLatitude}."//GeographicalCoordinateNear
					+ "}");
			
			vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    	//Model modelNameInstance = vqe.execConstruct();
			ResultSet res2 = vqe.execSelect();
			
			String stationStreetName = "";
			String stationType ="";
			Integer stationBikesNumber = 0;
			Integer stationID = 0;
			Float stationAltitude = 0.0f;
			Integer stationSlotsNumber = 0;
			Integer stationStreetNumber = 0;
			String stationStatus = "";
			Float latitude = 0.0f;
			Float longitude = 0.0f;
			
			String nearStationStreetName = "";
			String nearStationType ="";
			Integer nearStationBikesNumber = 0;
			Integer nearStationID = 0;
			Float nearStationAltitude = 0.0f;
			Integer nearStationSlotsNumber = 0;
			Integer nearStationStreetNumber = 0;
			String nearStationStatus = "";
			Float nearLatitude = 0.0f;
			Float nearLongitude = 0.0f;
			ArrayList<Infrastructure> nearByInfrastructure = new ArrayList<>();
			while(res2.hasNext()){
				QuerySolution qs2 = res2.next();
				String resultat = "";
				
				if(valid(qs2, "longitude")){
					longitude = Float.parseFloat(modifyScalarValue(qs2.get("longitude").toString()));
				}
				if(valid(qs2, "latitude")){
					latitude = Float.parseFloat(modifyScalarValue(qs2.get("latitude").toString()));	
				}
				if(valid(qs2, "stationStreetName")){
					stationStreetName = modifyScalarValue(qs2.get("stationStreetName").toString());
				}
				if(valid(qs2, "stationType")){
					stationType = modifyScalarValue(qs2.get("stationType").toString());
				}
				if(valid(qs2, "stationBikesNumber")){
					stationBikesNumber = Integer.parseInt(modifyScalarValue(qs2.get("stationBikesNumber").toString()));
				}
				if(valid(qs2, "stationID")){
					stationID = Integer.parseInt(modifyScalarValue(qs2.get("stationID").toString()));
				}
				if(valid(qs2, "stationAltitude")){
					stationAltitude = Float.parseFloat(modifyScalarValue(qs2.get("stationAltitude").toString()));
				}
				if(valid(qs2, "stationSlotsNumber")){
					stationSlotsNumber = Integer.parseInt(modifyScalarValue(qs2.get("stationSlotsNumber").toString()));
				}
				if(valid(qs2, "stationStreetNumber")){
					stationStreetNumber = Integer.parseInt(modifyScalarValue(qs2.get("stationStreetNumber").toString()));
				}
				if(valid(qs2, "stationStatus")){
					stationStatus = modifyScalarValue(qs2.get("stationStatus").toString());
				}
				
				if(valid(qs2, "nearByInfrastructure")){
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

						System.out.println("entrooooo " + nearLongitude + " " + nearLatitude);
						nearByInfrastructure.add(new BicingStation(null, new GeographicalCoordinate(nearLongitude, nearLatitude), "BicingStation", nearStationStreetName,nearStationType,nearStationBikesNumber, nearStationID, nearStationAltitude, nearStationSlotsNumber, nearStationStreetNumber, null,nearStationStatus ));
						
					}else if(qs2.get("tipoInfrastructure").toString().contains("MetroAndBusStop")){
						
					}
				}

				System.out.println("-----------------------");
			}
			
			bicingStations.add(new BicingStation(nearByInfrastructure, new GeographicalCoordinate(longitude, latitude), "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, null, stationStatus));
			nearByInfrastructure = new ArrayList<>();
		}
			



		/*
    	
    	StmtIterator it =  modelNameInstance.listStatements();

	     GeographicalCoordinate locatedIn;
		 String stationStreetName = "";
		 String stationType ="";
		 Integer stationBikesNumber = 0;
		 Integer stationID = 0;
		 Float stationAltitude = 0.0f;
		 Integer stationSlotsNumber = 0;
		 Integer stationStreetNumber = 0;
		 String stationStatus = "";
		 Float latitude = 0.0f;
		 Float longitude = 0.0f;
		 ArrayList<Infrastructure> infrastructures = new ArrayList<>();
    	while(it.hasNext()){
    		Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     
		     System.out.println("bici "  + s.toString() + " " + p.toString() + " " + o.toString());
		     
		     int index = o.toString().indexOf("^");
		     String oFinal = "";
		     if(index >= 0){
		    	 oFinal =  o.toString().substring(0, index);
		     
		     
				 if(p.toString().contains("stationStreetName")) stationStreetName = oFinal;
			     else if(p.toString().contains("stationType")) stationType = oFinal;
			     else if(p.toString().contains("stationBikesNumber")) stationBikesNumber = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationID")) stationID = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationAltitude")) stationAltitude = Float.parseFloat(oFinal);
			     else if(p.toString().contains("stationSlotsNumber")) stationSlotsNumber = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationStreetNumber")) stationStreetNumber = Integer.parseInt(oFinal);
			     else if(p.toString().contains("stationStatus")) stationStatus = oFinal;
			     
		     }else{
		    	 //Embedded object
		    	 
		     }
		     
    	}
    	*/
    	//BicingStations.add(new BicingStation(null, new GeographicalCoordinate(longitude, latitude), "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, null, stationStatus));
    	
    	/*
    	while (it.hasNext()) {
		     Statement stmt = it.next();
		     
		     RDFNode s = stmt.getSubject();
		     RDFNode p = stmt.getPredicate();
		     RDFNode o = stmt.getObject();
		     
		     System.out.println("bici "  + s.toString() + " " + p.toString() + " " + o.toString());
		     
		     sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'"+ s.toString() +"')) filter ( !regex(?o,'www.example.com/BicingStation'))}");
		     vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		     Model modelValuesIntance = vqe.execConstruct();
		     //Valores de la instancia
		     StmtIterator it2 =  modelValuesIntance.listStatements();
		    GeographicalCoordinate locatedIn;
			String stationStreetName = "";
			String stationType ="";
			Integer stationBikesNumber = 0;
			Integer stationID = 0;
			Float stationAltitude = 0.0f;
			Integer stationSlotsNumber = 0;
			Integer stationStreetNumber = 0;
			String stationStatus = "";
		     while (it2.hasNext()) {

		    	 Statement stmt2 = it2.next();
		    	 
		    	 RDFNode sV = stmt2.getSubject();
			     RDFNode pV = stmt2.getPredicate();
			     RDFNode oV = stmt2.getObject();
			     
			     
			     
			     
			     int index = oV.toString().indexOf("^");
			     String oFinal = "";
			     if(index >= 0) oFinal =  oV.toString().substring(0, index); //ScalarValue
			     else {
			    	 //ObjectValue
			    	 sparql = QueryFactory.create("select * FROM <http://localhost:8890/Example3> WHERE {?s ?p ?o filter ( regex(?s,'"+ oV.toString() +"'))}");
				     vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
				     Model objectValues = vqe.execConstruct();
				     StmtIterator it3 =  objectValues.listStatements();
				     while (it3.hasNext()) {

				    	 Statement stmt3 = it3.next();
				    	 
				    	 RDFNode sObjectValue = stmt3.getSubject();
					     RDFNode pObjectValue = stmt3.getPredicate();
					     RDFNode oObjectValue = stmt3.getObject();
					     
					     System.out.println(sObjectValue.toString() + " " + pObjectValue.toString() + " " + oObjectValue.toString());
					     
					     
				     }
			     }
			     
			     if(pV.toString().contains("stationStreetName")) stationStreetName = oFinal;
			     else if(pV.toString().contains("stationType")) stationType = oFinal;
			     else if(pV.toString().contains("stationBikesNumber")) stationBikesNumber = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationID")) stationID = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationAltitude")) stationAltitude = Float.parseFloat(oFinal);
			     else if(pV.toString().contains("stationSlotsNumber")) stationSlotsNumber = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationStreetNumber")) stationStreetNumber = Integer.parseInt(oFinal);
			     else if(pV.toString().contains("stationStatus")) stationStatus = oFinal;
		     }
		     BicingStations.add(new BicingStation(null, null, "BicingStation", stationStreetName, stationType, stationBikesNumber, stationID, stationAltitude, stationSlotsNumber, stationStreetNumber, null, stationStatus));
		}
		*/

	}
	
    public List<BicingStation> getAllBicingStations() {
        return bicingStations;
    }
    
    public boolean valid(QuerySolution qs , String value){
    	if(qs.get(value) != null) return true;
    	else return false;
    }
    public String modifyScalarValue(String value){
    		int index = value.indexOf("^");
		return  value.substring(0, index);
    }
}
