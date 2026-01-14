package belkin.dev.controller;

import belkin.dev.LocationSearchFilter;
import belkin.dev.dto.Location;
import belkin.dev.dto.LocationDto;
import belkin.dev.mapper.LocationMapper;
import belkin.dev.service.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    private final LocationMapper locationMapper;

    public LocationController(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationToCreate
    ) {
        log.info("Получен запрос на создание новой локации:{}", locationToCreate);
        Location createdLocation = locationService.createLocation(
                locationMapper.toLocation(locationToCreate)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationMapper.toLocationDto(createdLocation));
    }


    @GetMapping
    public List<LocationDto> getAllLocations(@Valid LocationSearchFilter locationSearchFilter) {
        log.info("Получен запрос на получение всех локаций");
        return locationService.getAllLocations(locationSearchFilter).stream()
                .map(locationMapper::toLocationDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> findLocationById(@PathVariable Integer id) {
        log.info("Получен запрос на поиск локации по id: id{}", id);

        var foundLocation = locationService.findLocationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.toLocationDto(foundLocation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Integer id,
                                                      @Valid @RequestBody LocationDto locationDtoToUpdate) {
        log.info("Обновляем локацию по id {}, данные {}", id, locationDtoToUpdate);
        var updatedLocation = locationService.updatelocation(
                id,
                locationMapper.toLocation(locationDtoToUpdate)
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationMapper.toLocationDto(updatedLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@Valid @PathVariable Integer id) {
        log.info("Удаляем локацию по id {}", id);
        locationService.deleteLocation(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

