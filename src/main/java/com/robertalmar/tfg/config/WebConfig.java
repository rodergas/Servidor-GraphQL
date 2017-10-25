package com.robertalmar.tfg.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.robertalmar.tfg.repository.QueryResolver;
import com.robertalmar.tfg.web.GraphQLEndpoint;

@Configuration
public class WebConfig {

	@Bean
	@Description("GraphQL Servlet registration")
	public ServletRegistrationBean<GraphQLEndpoint> graphqlServlet(QueryResolver queryResolver) {
		ServletRegistrationBean<GraphQLEndpoint> reg = 
				new ServletRegistrationBean<>(new GraphQLEndpoint(queryResolver));
		reg.setEnabled(true);
		reg.setLoadOnStartup(1);
		reg.addUrlMappings("/graphql");
		reg.setName("graphql");
		return reg;
	}
}
