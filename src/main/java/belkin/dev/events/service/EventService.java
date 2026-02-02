package belkin.dev.events.service;

import belkin.dev.events.dto.Event;
import belkin.dev.events.dto.EventOutDto;
import belkin.dev.events.dto.EventSearchDto;

import java.util.List;

public interface EventService {
    EventOutDto createEvent(Event event);

    EventOutDto findEventById(Integer id);

    void registerToEvent(Integer id);

    EventOutDto updateEvent(Integer eventId, Event eventFromUpdateDto);

    void deleteEvent(Integer eventId);

    List<EventOutDto> searchEvent(EventSearchDto eventSearchDto);

    List<EventOutDto> getAllEventsByOwner();

    void canselRegistration(Integer eventId);

    List<EventOutDto> getAllEventsByRegisterUser();

}
