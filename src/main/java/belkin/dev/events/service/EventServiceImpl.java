package belkin.dev.events.service;


import belkin.dev.events.EventStatus;
import belkin.dev.events.dto.Event;
import belkin.dev.events.dto.EventResponseDto;
import belkin.dev.events.dto.EventSearchDto;
import belkin.dev.events.mapper.EventMapper;
import belkin.dev.events.model.EventEntity;
import belkin.dev.events.model.RegistrationEntity;
import belkin.dev.events.repository.EventRepository;
import belkin.dev.events.repository.RegistrationRepository;
import belkin.dev.locations.dto.Location;
import belkin.dev.locations.service.LocationService;
import belkin.dev.security.jwt.AuthenticationService;
import belkin.dev.users.dto.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;


    public EventServiceImpl(LocationService locationService,
                            AuthenticationService authenticationService,
                            EventMapper eventMapper,
                            EventRepository eventRepository,
                            RegistrationRepository registrationRepository) {
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    @Transactional
    public EventResponseDto createEvent(Event event) {
        var location = locationService.findLocationById(event.locationId());
        checkMaxPlaces(location, event);
        var user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        var entity = eventMapper.toEntityFromEvent(
                event,
                user.id(),
                0,
                EventStatus.WAIT_START.toString(),
                null
        );
        return eventMapper.toOutDtoFromEntity(eventRepository.save(entity));


    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto findEventById(Integer id) {
        return eventMapper.toOutDtoFromEntity(getEventEntity(id));

    }

    @Override
    @Transactional
    public void registerToEvent(Integer id) {
        var event = getEventEntity(id);
        checkStatusAndOccupiedPlaces(event);

        var user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        RegistrationEntity registrationEntity = new RegistrationEntity(
                user.id(),
                event
        );

        Optional<RegistrationEntity> result = event.getRegistrationList().stream()
                .filter(registration -> Objects.equals(registration
                        .getUserId(), user.id()))
                .findFirst();

        if (result.isPresent()) {
            throw new IllegalArgumentException("этот пользователь уже зарегестрирован");
        }

        var savedRegistration = registrationRepository.save(registrationEntity);
        event.getRegistrationList().add(savedRegistration);
        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        eventRepository.save(event);

    }

    @Override
    @Transactional
    public EventResponseDto updateEvent(Integer eventId, Event eventFromUpdateDto) {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        EventEntity event = getEventEntity(eventId);
        checkOwnerAndDateAndStatus(user, event);
        Integer locationId = eventFromUpdateDto.locationId();

        if (locationId != null) {
            locationService.findLocationById(locationId);
        }
        if (eventFromUpdateDto.maxPlaces() != null && eventFromUpdateDto.maxPlaces() < event.getOccupiedPlaces()) {
            throw new IllegalArgumentException("количестов максимальных мест не может быть меньше" +
                    " количества зарегестрированных на мероприятие пользователей");
        }
        eventRepository.updateEvent(
                eventId,
                eventFromUpdateDto.name(),
                eventFromUpdateDto.maxPlaces(),
                eventFromUpdateDto.date(),
                eventFromUpdateDto.cost(),
                eventFromUpdateDto.duration(),
                eventFromUpdateDto.locationId()
        );

        EventEntity updatedEvent = getEventEntity(eventId);
        return eventMapper.toOutDtoFromEntity(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Integer eventId) {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        EventEntity eventEntity = getEventEntity(eventId);
        checkOwnerAndStatus(user, eventEntity);
        eventEntity.setStatus(EventStatus.CANCELLED.toString());
        eventRepository.save(eventEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> searchEvent(EventSearchDto eventSearchDto) {
        return eventRepository.searchEventByFilter(
                        eventSearchDto.name(),
                        eventSearchDto.placesMin(),
                        eventSearchDto.placesMax(),
                        eventSearchDto.dateStartAfter(),
                        eventSearchDto.dateStartBefore(),
                        eventSearchDto.costMin(),
                        eventSearchDto.costMax(),
                        eventSearchDto.durationMin(),
                        eventSearchDto.durationMax(),
                        eventSearchDto.locationId(),
                        eventSearchDto.status()
                ).stream()
                .map(eventMapper::toOutDtoFromEntity)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEventsByOwner() {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();

        return eventRepository.findAllByOwnerId(user.id()).stream()
                .map(eventMapper::toOutDtoFromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void canselRegistration(Integer eventId) {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        EventEntity event = getEventEntity(eventId);
        if (!Objects.equals(event.getStatus(), EventStatus.WAIT_START.toString())) {
            throw new IllegalArgumentException("регистрацию  можно отменить, только если мероприятие еще не началось");
        }

        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
        eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEventsByRegisterUser() {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();

        return eventRepository.getAllEventsByUser(user.id()).stream()
                .map(eventMapper::toOutDtoFromEntity)
                .toList();
    }


    private void checkOwnerAndDateAndStatus(User user, EventEntity event) {
        if (!(Objects.equals(user.id(), event.getOwnerId()))) {
            throw new IllegalArgumentException("Пользователь не является создателем пероприятия!");
        }
        if (event.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("меропритие уже началось!");
        }
        if (event.getStatus().equals(EventStatus.CANCELLED.toString())) {
            throw new IllegalArgumentException("мероприятие отменено!");
        }
    }


    private void checkMaxPlaces(Location location, Event event) {
        if (location.capacity() < event.maxPlaces()) {
            throw new IllegalArgumentException("Количество участников не может превышать количесвто мест в локации");
        }
    }

    private EventEntity getEventEntity(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("сущность по id " + id + " не найдена"));
    }

    private void checkStatusAndOccupiedPlaces(EventEntity eventEntity) {
        if (eventEntity.getStatus().equals(EventStatus.FINISHED.toString()) ||
                eventEntity.getStatus().equals(EventStatus.CANCELLED.toString())) {
            throw new IllegalArgumentException("мероприятие закончилось, или отменено!");
        }
        if (Objects.equals(eventEntity.getMaxPlaces(), eventEntity.getOccupiedPlaces())) {
            throw new IllegalArgumentException("мест для регистрации не осталось");

        }
    }

    private void checkOwnerAndStatus(User user, EventEntity eventEntity) {

        if (!Objects.equals(user.id(), eventEntity.getOwnerId())) {
            throw new IllegalArgumentException("пользователь не является собственником мероприятия");
        }
        if (!Objects.equals(eventEntity.getStatus(), EventStatus.WAIT_START.toString())) {
            throw new IllegalArgumentException("отменить можно, только если мероприятие еще не началось");
        }
    }


}
