package com.robertalmar.tfg.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Component;

import com.robertalmar.tfg.model.GeographicalCoordinate;
import com.robertalmar.tfg.query.SparqlQueryProvider;

import virtuoso.jena.driver.VirtGraph;

@Component
public class GeographicalCoordinateRepository extends BaseRepository {

	private static final String GEOGRAPHICAL_COORDINATES_QUERY = "getGeographicalCoordinates";
	
	private static final String LAT_LON_QUERY = "getLatLon";
	
	
	private final List<GeographicalCoordinate> geographicalCoordinates;

	
	public GeographicalCoordinateRepository(VirtGraph graph, SparqlQueryProvider queryProvider) {
		super(graph, queryProvider);

		geographicalCoordinates = new ArrayList<>();

		executeQuery();
	}

	private void executeQuery() {

		ResultSet rs = this.executeSelect(GEOGRAPHICAL_COORDINATES_QUERY);
		
		rs.forEachRemaining(qs -> {
			RDFNode subject = qs.get("s");

			Map<String, String> queryParams = new HashMap<>();
			queryParams.put("locationIri", subject.toString());

			ResultSet resultsetLatLon = this.executeSelect(LAT_LON_QUERY, queryParams);
			GeographicalCoordinate coordinate = this.mapToGeographicalCoordinate(resultsetLatLon);
			
			geographicalCoordinates.add(coordinate);
		});
		
		geographicalCoordinates.forEach(System.out::println);
	}

	private GeographicalCoordinate mapToGeographicalCoordinate(ResultSet resultSet) {
		GeographicalCoordinate.Builder coordinate = new GeographicalCoordinate.Builder();
		
		resultSet.forEachRemaining(qs -> {
			if(qs.contains("latitude")) {
				String latitudeString = modifyScalarValue(qs.get("latitude").toString());
				coordinate.latitude(Float.parseFloat(latitudeString));
			}
			
			if(qs.contains("longitude")) {
				String longitudeString = modifyScalarValue(qs.get("longitude").toString());
				coordinate.longitude(Float.parseFloat(longitudeString));
			}
		});
		
		return coordinate.build();
	}

	public String modifyScalarValue(String value){
		int index = value.indexOf("^");
		return value.substring(0, index);
	}
	
	public List<GeographicalCoordinate> getAllGeographicalCoordinates() {
		return geographicalCoordinates;
	}

	public void saveGeographicalCoordinate(GeographicalCoordinate geographicalCoordinate) {
		geographicalCoordinates.add(geographicalCoordinate);
	}

}
