package com.robertalmar.tfg.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.Resource;

import com.robertalmar.tfg.config.VirtuosoSettings.Connection;
import com.robertalmar.tfg.query.SparqlQueryProvider;

import virtuoso.jena.driver.VirtGraph;

@Configuration
public class VirtuosoConfig {

	@Autowired
	private VirtuosoSettings virtuosoSettings;
	
	@Bean
	public VirtGraph virtGraph() {
		Connection connectionSettings = virtuosoSettings.getConnection();
		
		return new VirtGraph (connectionSettings.getGraph(), 
				connectionSettings.getHost(), 
				connectionSettings.getUser(), 
				connectionSettings.getPassword());
	}
	
	@Bean
	@Description("Provides SparQL externalized queries")
	public SparqlQueryProvider queryProvider(@Qualifier("queriesProperties") Properties queryProperties) {
		SparqlQueryProvider provider = new SparqlQueryProvider(queryProperties);
		provider.setDbIri(virtuosoSettings.getQuery().getDbIri());
		return provider;
	}
	
	@Bean
	@Description("FactoryBean to load the SparQL queries into a Properties")
	public PropertiesFactoryBean queriesProperties(@Value("#{virtuosoSettings.query.file}") Resource queries) {
	    PropertiesFactoryBean bean = new PropertiesFactoryBean();
	    bean.setLocation(queries);
	    return bean;
	}
}
