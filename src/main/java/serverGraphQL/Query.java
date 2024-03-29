package serverGraphQL;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.lang.String;
import java.util.List;

public class Query implements GraphQLQueryResolver {
  private final MetroAndBusStopRepository MetroAndBusStopRepositoryInstance;

  private final SuburbRepository SuburbRepositoryInstance;

  private final GeographicalCoordinateRepository GeographicalCoordinateRepositoryInstance;

  private final BicingStationRepository BicingStationRepositoryInstance;

  private final DistrictRepository DistrictRepositoryInstance;

  public Query(MetroAndBusStopRepository MetroAndBusStopRepositoryInstance, SuburbRepository SuburbRepositoryInstance, GeographicalCoordinateRepository GeographicalCoordinateRepositoryInstance, BicingStationRepository BicingStationRepositoryInstance, DistrictRepository DistrictRepositoryInstance) {
    this.MetroAndBusStopRepositoryInstance = MetroAndBusStopRepositoryInstance;
    this.SuburbRepositoryInstance = SuburbRepositoryInstance;
    this.GeographicalCoordinateRepositoryInstance = GeographicalCoordinateRepositoryInstance;
    this.BicingStationRepositoryInstance = BicingStationRepositoryInstance;
    this.DistrictRepositoryInstance = DistrictRepositoryInstance;
  }

  public List<MetroAndBusStop> allMetroAndBusStops() {
    return MetroAndBusStopRepositoryInstance.getAllMetroAndBusStops();
  }

  public MetroAndBusStop getMetroAndBusStop(String id) {
    return MetroAndBusStopRepositoryInstance.getMetroAndBusStop(id);
  }

  public List<Suburb> allSuburbs() {
    return SuburbRepositoryInstance.getAllSuburbs();
  }

  public Suburb getSuburb(String id) {
    return SuburbRepositoryInstance.getSuburb(id);
  }

  public List<GeographicalCoordinate> allGeographicalCoordinates() {
    return GeographicalCoordinateRepositoryInstance.getAllGeographicalCoordinates();
  }

  public GeographicalCoordinate getGeographicalCoordinate(String id) {
    return GeographicalCoordinateRepositoryInstance.getGeographicalCoordinate(id);
  }

  public List<BicingStation> allBicingStations() {
    return BicingStationRepositoryInstance.getAllBicingStations();
  }

  public BicingStation getBicingStation(String id) {
    return BicingStationRepositoryInstance.getBicingStation(id);
  }

  public List<District> allDistricts() {
    return DistrictRepositoryInstance.getAllDistricts();
  }

  public District getDistrict(String id) {
    return DistrictRepositoryInstance.getDistrict(id);
  }
}
