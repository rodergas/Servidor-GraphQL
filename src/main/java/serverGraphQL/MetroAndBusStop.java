package serverGraphQL;

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

public class MetroAndBusStop implements Infrastructure {
  private String idTurtle;

  public MetroAndBusStop(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public String InfrastructureType() {
    return "MetroAndBusStop";
  }

  public String stopName() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stopName") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  public Integer stopPhone() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stopPhone") ;
    if(result.size() == 0) return null;
    else return Integer.parseInt(modifyScalarValue(result.get(0)));
  }

  public String stopAddress() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/stopAddress") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  public GeographicalCoordinate locatedIn() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/locatedIn");
    if(result.size() == 0) return null;
    else return new GeographicalCoordinate(result.get(0));
  }

  public ArrayList<Infrastructure> nearByInfrastructure() {
    ArrayList<String> nearByInfrastructure = connectVirtuoso("http://www.example.com/nearByInfrastructure");
    ArrayList<Infrastructure> nearByInfrastructures = new ArrayList<>();
    for(String id: nearByInfrastructure){
    	 if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", id).get(0).equals("http://www.example.com/MetroAndBusStop")) nearByInfrastructures.add(new MetroAndBusStop(id)); 
    	 if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", id).get(0).equals("http://www.example.com/BicingStation")) nearByInfrastructures.add(new BicingStation(id)); 
    }
    if(nearByInfrastructures.size() == 0) return null;
    else return nearByInfrastructures;
  }

  public ArrayList<String> connectVirtuoso(String value, String id) {
    VirtGraph graph = new VirtGraph ("TFG", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/AAA> WHERE {"
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

  public ArrayList<String> connectVirtuoso(String value) {
    VirtGraph graph = new VirtGraph ("TFG", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/AAA> WHERE {"
    + " <"+ this.idTurtle +"> <"+  value + "> ?valor."
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

  private String modifyScalarValue(String value) {
    int index = value.toString().indexOf("^");
    String resultat =  value.toString().substring(0, index);
    return resultat;
  }
}
