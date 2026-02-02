package belkin.dev.locations.service;

import belkin.dev.locations.LocationSearchFilter;
import belkin.dev.locations.dto.Location;

import java.util.List;

public interface LocationService {


    Location findLocationById(Integer id);


    List<Location> getAllLocations(LocationSearchFilter locationSearchFilter);

    Location createLocation(Location location);

    Location updatelocation(Integer id, Location location);

    void deleteLocation(Integer id);
}
