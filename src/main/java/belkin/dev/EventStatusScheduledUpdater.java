package belkin.dev;

import belkin.dev.events.EventStatus;
import belkin.dev.events.kafka.EventNotificationSender;
import belkin.dev.events.kafka.dto.NotificationDto;
import belkin.dev.events.kafka.mapper.NotificationMapper;
import belkin.dev.events.model.EventEntity;
import belkin.dev.events.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@EnableScheduling
@Configuration
public class EventStatusScheduledUpdater {
    private final static Logger log = LoggerFactory.getLogger(EventStatusScheduledUpdater.class);
    private final EventRepository eventRepository;
    private final NotificationMapper notificationMapper;
    private final EventNotificationSender eventNotificationSender;

    public EventStatusScheduledUpdater(EventRepository eventRepository, NotificationMapper notificationMapper, EventNotificationSender eventNotificationSender) {
        this.eventRepository = eventRepository;
        this.notificationMapper = notificationMapper;
        this.eventNotificationSender = eventNotificationSender;
    }

    @Scheduled(cron = "${event.stats.cron}")
    @Transactional
    public void updateEventStatusesToStarted() {
        log.info("EventStatusScheduledUpdater started");

        List<EventEntity> startList = eventRepository.findStartedEvents();
        List<EventEntity> finishedEvents = eventRepository.findFinishedEvents();

        processEvents(startList, EventStatus.STARTED);
        processEvents(finishedEvents, EventStatus.FINISHED);


        // eventRepository.updateNotStartedEvent(EventStatus.STARTED.toString());
        log.info("EventStatusScheduledUpdater finished");
    }

//    @Scheduled(cron = "${event.stats.cron}")
//    @Transactional
//    public void updateEventStatusesToFinished() {
//        log.info("second Method started");
//        eventRepository.updateStartedEvent(EventStatus.FINISHED.toString());
//    }

    private void processEvents(List<EventEntity> events, EventStatus status) {

        if (!events.isEmpty()) {
            List<NotificationDto> notificationDtoList = events.stream()
                    .map(notificationMapper::toNotificationDtoFromScheduler)
                    .toList();
            List<Integer> idsList = events.stream()
                    .map(eventEntity -> eventEntity.getId())
                    .toList();


            eventRepository.updateStatusBatch(idsList, status.toString());

            notificationDtoList.forEach(eventNotificationSender::sendNotification);

        }


    }
}
