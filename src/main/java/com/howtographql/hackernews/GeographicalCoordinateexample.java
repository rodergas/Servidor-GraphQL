package com.howtographql.hackernews;

import java.lang.Float;
import java.lang.String;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class GeographicalCoordinateexample {
  private String idTurtle;

  public GeographicalCoordinateexample(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public Float getLatitude() {
    return Float.parseFloat(modifyScalarValue(connectVirtuoso("http://www.example.com/latitude").get(0)));
  }

  public Float getLongitude() {
    return Float.parseFloat(modifyScalarValue(connectVirtuoso("http://www.example.com/longitude").get(0)));
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