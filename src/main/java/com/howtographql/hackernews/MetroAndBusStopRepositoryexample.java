package com.howtographql.hackernews;

import java.lang.String;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class MetroAndBusStopRepositoryexample {
  private final ArrayList<MetroAndBusStop> MetroAndBusStops;

  public MetroAndBusStopRepositoryexample() {
    MetroAndBusStops = new ArrayList<>();
    VirtGraph graph = new VirtGraph ("TFG_Example1", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?subject FROM <http://localhost:8890/Example4> WHERE {"
    + "OPTIONAL { ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/MetroAndBusStop>}."
    + "}");
     
    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    ResultSet res = vqe.execSelect();
    while(res.hasNext()){
    	 QuerySolution qs = res.next();
    	 String subject = qs.get("?subject").toString();
    	 MetroAndBusStops.add(new MetroAndBusStop(subject));
    }

    graph.close();
  }

  public ArrayList<MetroAndBusStop> getAllMetroAndBusStops() {
    return MetroAndBusStops;
  }

  public MetroAndBusStop getMetroAndBusStop(String id) {
    return new MetroAndBusStop(id);
  }
}