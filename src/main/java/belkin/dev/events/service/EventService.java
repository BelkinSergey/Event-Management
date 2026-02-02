package belkin.dev.events.service;

import belkin.dev.events.dto.Event;
import belkin.dev.events.dto.EventResponseDto;
import belkin.dev.events.dto.EventSearchDto;

import java.util.List;

public interface EventService {
    EventResponseDto createEvent(Event event);

    EventResponseDto findEventById(Integer id);

    void registerToEvent(Integer id);

    EventResponseDto updateEvent(Integer eventId, Event eventFromUpdateDto);

    void deleteEvent(Integer eventId);

    List<EventResponseDto> searchEvent(EventSearchDto eventSearchDto);

    List<EventResponseDto> getAllEventsByOwner();

    void canselRegistration(Integer eventId);

    List<EventResponseDto> getAllEventsByRegisterUser();

}
