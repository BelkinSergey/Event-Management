package belkin.dev.events.mapper;


import belkin.dev.events.dto.Event;
import belkin.dev.events.dto.EventInDto;
import belkin.dev.events.dto.EventOutDto;
import belkin.dev.events.dto.EventUpdateDto;
import belkin.dev.events.model.EventEntity;
import belkin.dev.events.model.RegistrationEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {


    public Event toEvent(EventInDto eventInDto) {
        return new Event(
                eventInDto.name(),
                eventInDto.maxPlaces(),
                eventInDto.date(),
                eventInDto.cost(),
                eventInDto.duration(),
                eventInDto.locationId()
        );
    }

    public EventEntity toEntityFromEvent(Event event,
                                         Integer userId,
                                         Integer occupiedPlaces,
                                         String eventStatus,
                                         List<RegistrationEntity> registrationList) {
        return new EventEntity(
                null,
                event.name(),
                userId,
                event.maxPlaces(),
                occupiedPlaces,
                event.date(),
                event.duration(),
                event.cost(),
                event.locationId(),
                eventStatus,
                registrationList == null ? new ArrayList<>() : registrationList
        );
    }

    public EventOutDto toOutDtoFromEntity(EventEntity eventEntity) {
        return new EventOutDto(eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()

        );
    }

    public Event toEventFromUpdateDto(EventUpdateDto eventUpdateDto) {
        return new Event(
                eventUpdateDto.name(),
                eventUpdateDto.maxPlaces(),
                eventUpdateDto.date(),
                eventUpdateDto.cost(),
                eventUpdateDto.duration(),
                eventUpdateDto.locationId()
        );
    }
}



