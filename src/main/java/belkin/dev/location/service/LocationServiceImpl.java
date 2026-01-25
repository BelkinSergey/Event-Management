package belkin.dev.location.service;

import belkin.dev.location.LocationSearchFilter;
import belkin.dev.location.dto.Location;
import belkin.dev.location.mapper.LocationMapper;
import belkin.dev.location.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationMapper locationMapper, LocationRepository locationRepository) {
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
    }


    @Override
    public Location createLocation(Location location) {
        var locationToSave = locationMapper.toEntity(location);
        var savedLocation = locationRepository.save(locationToSave);
        return locationMapper.fromEntity(savedLocation);
    }


    @Override
    public Location findLocationById(Integer id) {

        var foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "сущность по id=%s не найдена".formatted(id)
                ));
        return locationMapper.fromEntity(foundLocation);
    }

    @Override
    public List<Location> getAllLocations(LocationSearchFilter locationSearchFilter) {
        int pageSize = locationSearchFilter.pageSize() != null
                ? locationSearchFilter.pageSize() : 5;

        int pageNumber = locationSearchFilter.pageNumber() != null
                ? locationSearchFilter.pageNumber() : 0;

        Pageable pageable = Pageable
                .ofSize(pageSize)
                .withPage(pageNumber);

        return locationRepository.searchAllLocations(locationSearchFilter.name(),
                        locationSearchFilter.address(),
                        pageable).stream()
                .map(locationMapper::fromEntity)
                .toList();
    }

    @Override
    public Location updatelocation(Integer id, Location location) {
        var foundEntity = findLocationById(id);
        if (location.capacity() < foundEntity.capacity()) {
            throw new IllegalArgumentException("Количесвто участников не может быть меньше заявленного изначально");
        }
        locationRepository.locationToUpdate(
                id,
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );

        return findLocationById(id);
    }

    @Override
    public void deleteLocation(Integer id) {
        findLocationById(id);
        locationRepository.deleteById(id);
    }
}
