package belkin.dev.events.kafka.dto;

import belkin.dev.events.kafka.NotificationStatus;

import java.util.List;

public record NotificationDto(

        Integer eventId,

        Integer changeUserId,

        Integer ownerId,

        String createdAt,

        NotificationStatus notificationStatus,

        List<EventChangeNotificationDto<?>> changes,

        List<Integer> usersId


) {
}
