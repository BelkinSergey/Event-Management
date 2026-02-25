package belkin.dev.events.kafka;

import belkin.dev.events.kafka.dto.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventNotificationSender {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationSender.class);

    private final KafkaTemplate<Integer, NotificationDto> kafkaTemplate;

    public EventNotificationSender(KafkaTemplate<Integer, NotificationDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(NotificationDto notificationDto) {
        log.info("Sending notification: event={}", notificationDto);

        var result = kafkaTemplate.send(
                "events-topic",
                notificationDto.eventId(),
                notificationDto
        );

        result.thenAccept(sendResult -> {
            log.info("Sending successful");
        });

    }
}
