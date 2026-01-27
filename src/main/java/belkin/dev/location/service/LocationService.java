package belkin.dev.location.service;

import belkin.dev.location.LocationSearchFilter;
import belkin.dev.location.dto.Location;

import java.util.List;

public interface LocationService {


    Location findLocationById(Integer id);


    List<Location> getAllLocations(LocationSearchFilter locationSearchFilter);

    Location createLocation(Location location);

    Location updatelocation(Integer id, Location location);

    void deleteLocation(Integer id);
}
