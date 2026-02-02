package belkin.dev.events.controller;

import belkin.dev.events.dto.EventInDto;
import belkin.dev.events.dto.EventResponseDto;
import belkin.dev.events.dto.EventSearchDto;
import belkin.dev.events.dto.EventUpdateDto;
import belkin.dev.events.mapper.EventMapper;
import belkin.dev.events.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    private final EventMapper eventMapper;

    public EventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;

        this.eventMapper = eventMapper;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(
            @RequestBody @Valid EventInDto eventInDto
    ) {
        log.info("получен запрос на создание мероприятия: {}", eventInDto);
        EventResponseDto createdEvent = eventService.createEvent(
                eventMapper.toEvent(eventInDto)
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdEvent);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> findEventById(@PathVariable @Min(value = 1,
            message = "ID должен быть положительным числом") Integer id) {
        log.info("получен запрос на поиск мероприятия по id:{}", id);
        EventResponseDto eventResponseDto = eventService.findEventById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventResponseDto);
    }

    @PostMapping("/registrations/{id}")
    public ResponseEntity<String> registerToEvent(@PathVariable @Min(value = 1,
            message = "ID должен быть положительным числом") Integer id) {
        log.info("получен запрос на регистрацию на мероприятие по id:{}", id);
        eventService.registerToEvent(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Успешная регистрация на мероприятие");
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull EventResponseDto> updateEvent(
            @Min(1)
            @PathVariable("id")
            Integer eventId,
            @Valid @RequestBody EventUpdateDto eventUpdateDto
    ) {
        log.info("получен запрос на обновление мероприятия {}", eventId);
        EventResponseDto eventResponseDto = eventService.updateEvent(eventId, eventMapper.toEventFromUpdateDto(eventUpdateDto));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @Min(1) @PathVariable("id") Integer eventId
    ) {
        log.info("получен запрос на удаление мероприятия {}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventResponseDto>> searchEventByFilter(
            @Valid @RequestBody EventSearchDto eventSearchDto
    ) {
        log.info("получен запрос на поиск мероприятия");
        List<EventResponseDto> eventList = eventService.searchEvent(eventSearchDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventList);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getAllEventsByOwner() {
        log.info("получен запрос на получение всех мероприятий пользователя");
        List<EventResponseDto> eventList = eventService.getAllEventsByOwner();
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventList);
    }

    @DeleteMapping("/registrations/cancel/{id}")
    public ResponseEntity<Void> canselRegistration(@Min(1) @PathVariable("id") Integer eventId) {
        log.info("получен запрос отмену регистрации на мероприятие");
        eventService.canselRegistration(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/registrations/my")

    public ResponseEntity<List<EventResponseDto>> getAllEventsByRegisterUser() {
        log.info("получен запрос получение всех мероприятий, на которые зарагестрирован пользователь");
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAllEventsByRegisterUser());

    }
}
