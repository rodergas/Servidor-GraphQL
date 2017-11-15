package com.howtographql.hackernews;

import java.lang.Boolean;
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

public class MetroAndBusStopexample {
  private String idTurtle;

  public MetroAndBusStopexample(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public String getInfrastructureType() {
    return modifyScalarValue(connectVirtuoso("http://www.example.com/InfrastructureType").get(0));
  }

  public GeographicalCoordinate getLocatedIn() {
    return new GeographicalCoordinate(connectVirtuoso("http://www.example.com/locatedIn").get(0));
  }

  public ArrayList<Infrastructure> getNearByInfrastructure() {
    ArrayList<String> nearByInfrastructure = connectVirtuoso("http://www.example.com/nearByInfrastructure");
    ArrayList<Infrastructure> nearByInfrastructures = new ArrayList<>();
    for(String id:nearByInfrastructure) nearByInfrastructures.add(new Infrastructure(id));
    return nearByInfrastructures;
  }

  public Boolean getStopAddress() {
    return Boolean.parseBoolean(modifyScalarValue(connectVirtuoso("http://www.example.com/stopAddress").get(0)));
  }

  public ArrayList<Integer> getStopPhone() {
    ArrayList<String> stopPhone = connectVirtuoso("http://www.example.com/stopPhone");
    ArrayList<Integer> stopPhones = new ArrayList<>();
    for(String id:stopPhone) stopPhones.add(Integer.parseInt(id));
    return stopPhones;
  }

  public String getStopName() {
    return modifyScalarValue(connectVirtuoso("http://www.example.com/stopName").get(0));
  }

  private String modifyScalarValue(String value) {
    int index = value.toString().indexOf("^");
    String resultat =  value.toString().substring(0, index);
    return resultat;
  }

  public ArrayList<String> connectVirtuoso(String value) {
    VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/Example4> WHERE {"
    + "OPTIONAL { <"+ this.getIdTurtle() +"> <"+  value + "> ?valor}."
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
