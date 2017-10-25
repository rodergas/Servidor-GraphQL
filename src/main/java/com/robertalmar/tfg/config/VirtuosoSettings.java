package com.robertalmar.tfg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("virtuoso")
public class VirtuosoSettings {
	
	private Connection connection;
	
	private Query query;
	

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	static class Connection {
		
		private String graph;
		
		private String host;
		
		private String user;
		
		private String password;
	
		public String getGraph() {
			return graph;
		}
	
		public void setGraph(String graph) {
			this.graph = graph;
		}
	
		public String getHost() {
			return host;
		}
	
		public void setHost(String host) {
			this.host = host;
		}
	
		public String getUser() {
			return user;
		}
	
		public void setUser(String user) {
			this.user = user;
		}
	
		public String getPassword() {
			return password;
		}
	
		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	static class Query {
		private String file;
		
		private String dbIri;

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public String getDbIri() {
			return dbIri;
		}

		public void setDbIri(String dbIri) {
			this.dbIri = dbIri;
		}
	}
}
