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

public class District {
  private String idTurtle;

  public District(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public ArrayList<ArrayList<Infrastructure>> districtNestedList() {
    ArrayList<ArrayList<Infrastructure>> result = new ArrayList<>();
    ArrayList<String> valuesOfLists = new ArrayList<>();
    ArrayList<String> districtNestedList = connectVirtuoso("http://www.example.com/districtNestedList");
    for(String value:districtNestedList) { 
    	 ArrayList<String> lists = connectVirtuoso("http://www.essi.upc.edu/~jvarga/gql/hasElement", value); 
    	 for(String list:lists){ 
    	 	 valuesOfLists = connectVirtuoso("http://www.essi.upc.edu/~jvarga/gql/hasElement", list); 
    	 	 ArrayList<Infrastructure> correctValuesOfLists = new ArrayList<>();
    	 	 for(String valueOfList :valuesOfLists){ 
    	 	 	 if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", valueOfList).get(0).equals("http://www.example.com/MetroAndBusStop")) correctValuesOfLists.add(new MetroAndBusStop(valueOfList)); 
    	 	 	 if(connectVirtuoso("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", valueOfList).get(0).equals("http://www.example.com/BicingStation")) correctValuesOfLists.add(new BicingStation(valueOfList)); 
    	 	 }
    	 	 result.add(correctValuesOfLists);
    	 } 
    } 
    if(result.size() == 0) return null;
    else return result;
  }

  public String districtName() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/districtName") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  public Integer districtNumber() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/districtNumber") ;
    if(result.size() == 0) return null;
    else return Integer.parseInt(modifyScalarValue(result.get(0)));
  }

  public ArrayList<String> connectVirtuoso(String value, String id) {
    VirtGraph graph = new VirtGraph ("TFG", "jdbc:virtuoso://localhost:1111", "dba", "dba");
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/BBB> WHERE {"
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
    Query sparql = QueryFactory.create("Select ?valor FROM <http://localhost:8890/BBB> WHERE {"
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
