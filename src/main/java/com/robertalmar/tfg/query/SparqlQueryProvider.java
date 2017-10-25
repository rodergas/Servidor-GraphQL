package com.robertalmar.tfg.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

public class SparqlQueryProvider {
	
	private final Properties queryProperties;
	
	private String dbIri;
	
	public SparqlQueryProvider(Properties queryProperties) {
		this.queryProperties = queryProperties;
	}
	
	public Query getQuery(String queryName) {
		return this.getQuery(queryName, null);
	}
	
	public Query getQuery(String queryName, Map<String, String> queryParams) {
		return getQueryString(queryName)
				.map(query -> mapToQuery(query, queryParams))
				.map(QueryFactory::create)
				.orElseThrow(() -> new QueryNotDefinedException("No query defined with name '" + queryName + "'"));
	}
	
	private Optional<String> getQueryString(String queryName) {
		String query = queryProperties.getProperty(queryName);
		
		return Optional.ofNullable(query);
	}
	
	private Query mapToQuery(String query, Map<String, String> queryParams) {
		
		ParameterizedSparqlString pq = new ParameterizedSparqlString(query);
		
		if(queryParams == null) {
			queryParams = new HashMap<>();
		}
		
		if(dbIri != null && !dbIri.isEmpty() && !queryParams.containsKey("dbIri")) {
			pq.setIri("dbIri", dbIri);
		}
		
		queryParams.forEach(pq::setIri);
		
		return pq.asQuery();
	}
	
	public void setDbIri(String dbIri) {
		this.dbIri = dbIri;
	}
}
