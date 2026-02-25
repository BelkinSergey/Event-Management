package belkin.dev.events.kafka.mapper;

import belkin.dev.events.EventStatus;
import belkin.dev.events.dto.Event;
import belkin.dev.events.kafka.NotificationStatus;
import belkin.dev.events.kafka.dto.EventChangeNotificationDto;
import belkin.dev.events.kafka.dto.NotificationDto;
import belkin.dev.events.mapper.EventMapper;
import belkin.dev.events.model.EventEntity;
import belkin.dev.events.model.RegistrationEntity;
import belkin.dev.users.dto.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class NotificationMapper {

    private final EventMapper eventMapper;

    public NotificationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }


    public NotificationDto toNotificationDtoFromEntity(EventEntity oldEntity,
                                                       EventEntity updateEvent,
                                                       User user,
                                                       String status) {
        return new NotificationDto(
                updateEvent.getId(),
                user.id(),
                updateEvent.getOwnerId(),
                LocalDateTime.now().toString(),
                NotificationStatus.UNREAD,
                makeListChanges(oldEntity, eventMapper.toEventFromEntity(updateEvent), status),
                makeUsersIdList(updateEvent.getRegistrationList())
        );
    }

    public NotificationDto toNotificationDtoFromScheduler(EventEntity eventEntity) {

        Event updatedEvent = new Event(
                eventEntity.getName(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId()
        );

        String status = eventEntity.getStatus().equals(EventStatus.WAIT_START.toString()) ?
                EventStatus.STARTED.toString() : EventStatus.FINISHED.toString();

        return new NotificationDto(
                eventEntity.getId(),
                null,
                eventEntity.getOwnerId(),
                LocalDateTime.now().toString(),
                NotificationStatus.UNREAD,
                makeListChanges(eventEntity, updatedEvent, status),
                makeUsersIdList(eventEntity.getRegistrationList())
        );
    }

    public NotificationDto toNotificationDtoToDelete(EventEntity eventEntity, User user) {

        Event updatedEvent = new Event(
                eventEntity.getName(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId()
        );

        return new NotificationDto(
                eventEntity.getId(),
                user.id(),
                eventEntity.getOwnerId(),
                LocalDateTime.now().toString(),
                NotificationStatus.UNREAD,
                makeListChanges(eventEntity, updatedEvent, EventStatus.CANCELLED.toString()),
                makeUsersIdList(eventEntity.getRegistrationList())
        );
    }

    private List<Integer> makeUsersIdList(List<RegistrationEntity> regList) {

        return regList.stream()
                .map(RegistrationEntity::getUserId)
                .toList();
    }


    private List<EventChangeNotificationDto<?>> makeListChanges(EventEntity event,
                                                                Event updatedEvent,
                                                                String status) {

        List<EventChangeNotificationDto<?>> listChanges = new ArrayList<>();

        Map<String, Object> map = Map.of(
                "name", updatedEvent.name(),
                "maxPlaces", updatedEvent.maxPlaces(),
                "date", updatedEvent.date(),
                "cost", updatedEvent.cost(),
                "duration", updatedEvent.duration(),
                "locationId", updatedEvent.locationId(),
                "status", status
        );

        map.forEach((name, newValue) -> {
            if (newValue != null) {
                var result = choseParameter(name, newValue, event);

                if (result != null) {
                    listChanges.add(result);
                }
            }
        });
        return listChanges;
    }


    private EventChangeNotificationDto<?> choseParameter(String name,
                                                         Object newValue,
                                                         EventEntity event) {
        if (event != null) {
            return switch (name) {

                case "name" -> makeChangeNotification(event.getName(), newValue, name);
                case "maxPlaces" -> makeChangeNotification(event.getMaxPlaces(), newValue, name);
                case "date" -> makeChangeNotification(event.getDate(), newValue, name);
                case "cost" -> makeChangeNotification(event.getCost(), newValue, name);
                case "duration" -> makeChangeNotification(event.getDuration(), newValue, name);
                case "locationId" -> makeChangeNotification(event.getLocationId(), newValue, name);
                case "status" -> makeChangeNotification(event.getStatus(), newValue, name);

                default -> throw new IllegalStateException("Unexpected value: " + name);
            };

        } else {

            return switch (name) {
                case "name", "maxPlaces", "date", "cost", "duration", "locationId", "status" ->
                        makeChangeNotification(null, newValue, name);


                default -> throw new IllegalStateException("Unexpected value: " + name);
            };
        }
    }

    private EventChangeNotificationDto<?> makeChangeNotification(Object oldValue,
                                                                 Object newValue,
                                                                 String name) {

        if (Objects.equals(oldValue, newValue)) {
            return null;
        }
        if (name.equals("date")) {
            String oldString = oldValue instanceof LocalDateTime ? ((LocalDateTime) oldValue)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;

            String newString = newValue instanceof LocalDateTime ? ((LocalDateTime) newValue)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;

            return new EventChangeNotificationDto<>(oldString, newString, name);
        }
        return new EventChangeNotificationDto<>(name, oldValue, newValue);
    }
}




