package belkin.dev;

import belkin.dev.events.EventStatus;
import belkin.dev.events.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@EnableScheduling
@Configuration
public class EventStatusScheduledUpdater {
    private final static Logger log = LoggerFactory.getLogger(EventStatusScheduledUpdater.class);
    private final EventRepository eventRepository;

    public EventStatusScheduledUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.stats.cron}")
    @Async
    public void updateEventStatusesToStarted() {
        log.info("EventStatusScheduledUpdater started");

        eventRepository.updateNotStartedEvent(EventStatus.STARTED.toString());
        log.info("EventStatusScheduledUpdater finished");
    }

    @Scheduled(cron = "${event.stats.cron}")
    @Async
    public void updateEventStatusesToFinished() {
        log.info("second Method started");
        eventRepository.updateStartedEvent(EventStatus.FINISHED.toString());
    }
}
