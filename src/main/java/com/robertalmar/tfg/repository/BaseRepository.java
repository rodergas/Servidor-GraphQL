package com.robertalmar.tfg.repository;

import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robertalmar.tfg.query.SparqlQueryProvider;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public abstract class BaseRepository {

	private final Logger log = LoggerFactory.getLogger(BaseRepository.class);
	
	protected final VirtGraph graph;
	
	protected final SparqlQueryProvider queryProvider;

	
	public BaseRepository(VirtGraph graph, SparqlQueryProvider queryProvider) {
		this.graph = graph;
		this.queryProvider = queryProvider;
	}
	
	protected ResultSet executeSelect(String queryName, Map<String, String> queryParam) {
		Query sparql = queryProvider.getQuery(queryName, queryParam);
		
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, graph);
		
		log.debug("About to execute: \n{}", sparql.toString());
		
		return vqe.execSelect();
	}
	
	protected ResultSet executeSelect(String queryName) {
		return this.executeSelect(queryName, null);
	}
}
