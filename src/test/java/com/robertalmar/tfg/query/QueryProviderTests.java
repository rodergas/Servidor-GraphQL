package com.robertalmar.tfg.query;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.jena.query.Query;
import org.junit.Before;
import org.junit.Test;

public class QueryProviderTests {
	
	private static final String QUERIES_FILE_LOCATION = "src/test/resources/graphql/test-queries.xml";
	
	private SparqlQueryProvider queryProvider;
	
	@Before
	public void setup() throws Exception {
		Properties properties = new Properties();
		properties.loadFromXML(new FileInputStream(QUERIES_FILE_LOCATION));
		
		queryProvider = new SparqlQueryProvider(properties);
	}
	
	@Test
	public void testExistingQuery() {
		Query query = queryProvider.getQuery("testQuery");
		
		assertNotNull(query);
	}
	
	@Test(expected = QueryNotDefinedException.class)
	public void testNonExistingProvideQuery() {
		queryProvider.getQuery("testQuery1");
	}
}
