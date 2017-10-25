package com.robertalmar.tfg.web;

import com.coxautodev.graphql.tools.SchemaParser;
import com.robertalmar.tfg.repository.QueryResolver;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

public class GraphQLEndpoint extends SimpleGraphQLServlet {

	public GraphQLEndpoint(QueryResolver queryResolver) {
		super(buildSchema(queryResolver));
	}

	private static GraphQLSchema buildSchema(QueryResolver queryResolver) {
		
		return SchemaParser.newParser()
				.file("graphql/tfg.graphqls")
				.resolvers(queryResolver)
				// .dictionary(MetroAndBusStop.class)
				.build().makeExecutableSchema();
	}
}
