package com.TFG.servidorGraphQL2;

import java.lang.Float;
import java.lang.Integer;
import java.lang.String;
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

  public BicingStation(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public GeographicalCoordinate getLocatedIn() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/locatedIn") ;
    if(result.size() == 0) return null;
    else return new GeographicalCoordinate(result.get(0));
  }

  public ArrayList<Infrastructure> getNearByInfrastructure() {
    ArrayList<String> nearByInfrastructure = connectVirtuoso("http://www.example.com/nearByInfrastructure");
    ArrayList<Infrastructure> nearByInfrastructures = new ArrayList<>();
    for(String id: nearByInfrastructure){
    	 if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", id).get(0).equals("http://www.example.com/MetroAndBusStop")) nearByInfrastructures.add(new MetroAndBusStop(id)); 
    	 if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", id).get(0).equals("http://www.example.com/BicingStation")) nearByInfrastructures.add(new BicingStation(id)); 
    }
    if(nearByInfrastructures.size() == 0) return null;
    else return nearByInfrastructures;
  }

  public String getInfrastructureType() {
    return "BicingStation";
  }

  public Integer getStationBikesNumber() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationBikesNumber") ;
    if(result.size() == 0) return null;
    else return Integer.parseInt(modifyScalarValue(result.get(0)));
  }

  public ArrayList<BicingStation> getNearByStation() {
    ArrayList<String> nearByStation = connectVirtuoso("http://www.example.com/nearByStation");
    ArrayList<BicingStation> nearByStations = new ArrayList<>();
    for(String id:nearByStation) nearByStations.add(new BicingStation(id));
    if(nearByStations.size() == 0) return null;
    else return nearByStations;
  }

  public Integer getStationStreetNumber() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationStreetNumber") ;
    if(result.size() == 0) return null;
    else return Integer.parseInt(modifyScalarValue(result.get(0)));
  }

  public String getStationType() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationType") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  public String getStationStatus() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationStatus") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  public Integer getStationSlotsNumber() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationSlotsNumber") ;
    if(result.size() == 0) return null;
    else return Integer.parseInt(modifyScalarValue(result.get(0)));
  }

  public Integer getStationID() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationID") ;
    if(result.size() == 0) return null;
    else return Integer.parseInt(modifyScalarValue(result.get(0)));
  }

  public Float getStationAltitude() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationAltitude") ;
    if(result.size() == 0) return null;
    else return Float.parseFloat(modifyScalarValue(result.get(0)));
  }

  public String getStationStreetName() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stationStreetName") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  private String modifyScalarValue(String value) {
    int index = value.toString().indexOf("^");
    String resultat =  value.toString().substring(0, index);
    return resultat;
  }

  public ArrayList<String> connectVirtuoso(String value) {
    VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/Config> WHERE {"
    + " <"+ this.getIdTurtle() +"> <"+  value + "> ?valor."
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

  public ArrayList<String> connectVirtuoso(String value, String id) {
    VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/Config> WHERE {"
    + " <"+ id +"> <"+  value + "> ?valor."
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

  private String getIdTurtle() {
    return idTurtle;
  }
}
