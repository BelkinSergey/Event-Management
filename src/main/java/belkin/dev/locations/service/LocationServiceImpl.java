package belkin.dev.locations.service;

import belkin.dev.locations.LocationSearchFilter;
import belkin.dev.locations.dto.Location;
import belkin.dev.locations.mapper.LocationMapper;
import belkin.dev.locations.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Location createLocation(Location location) {
        var locationToSave = locationMapper.toEntity(location);
        var savedLocation = locationRepository.save(locationToSave);
        return locationMapper.fromEntity(savedLocation);
    }


    @Override
    @Transactional(readOnly = true)
    public Location findLocationById(Integer id) {

        var foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "сущность по id=%s не найдена".formatted(id)
                ));
        return locationMapper.fromEntity(foundLocation);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional
    public void deleteLocation(Integer id) {
        findLocationById(id);
        locationRepository.deleteById(id);
    }
}
