package serverGraphQL;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class BicingStationRepository {
  private final ArrayList<BicingStation> BicingStations;

  public BicingStationRepository() {
    BicingStations = new ArrayList<>();
    VirtGraph graph = new VirtGraph ("jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?subject FROM <http://localhost:8890/BBB> WHERE {"
    + " ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/BicingStation>."
    + "}");
     
    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    ResultSet res = vqe.execSelect();
    while(res.hasNext()){
    	 QuerySolution qs = res.next();
    	 String subject = qs.get("?subject").toString();
    	 BicingStations.add(new BicingStation(subject));
    }

    graph.close();
  }

  public List<BicingStation> getAllBicingStations() {
    return BicingStations;
  }

  public BicingStation getBicingStation(String id) {
    return new BicingStation(id);
  }
}
