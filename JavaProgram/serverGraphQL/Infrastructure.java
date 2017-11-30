package serverGraphQL;

import java.lang.String;
import java.util.ArrayList;

public interface Infrastructure {
  GeographicalCoordinate locatedIn();

  ArrayList<Infrastructure> nearByInfrastructure();

  String InfrastructureType();
}
