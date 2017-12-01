package serverGraphQL;

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

public class GeographicalCoordinate {
  private String idTurtle;

  public GeographicalCoordinate(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public Float latitude() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/latitude") ;
    if(result.size() == 0) return null;
    else return Float.parseFloat(modifyScalarValue(result.get(0)));
  }

  public Float longitude() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/longitude") ;
    if(result.size() == 0) return null;
    else return Float.parseFloat(modifyScalarValue(result.get(0)));
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
