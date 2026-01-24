package belkin.dev.service;

import belkin.dev.LocationSearchFilter;
import belkin.dev.dto.Location;

import java.util.List;

public interface LocationService {


    Location findLocationById(Integer id);


    List<Location> getAllLocations(LocationSearchFilter locationSearchFilter);

    Location createLocation(Location location);

    Location updatelocation(Integer id, Location location);

    void deleteLocation(Integer id);
}
