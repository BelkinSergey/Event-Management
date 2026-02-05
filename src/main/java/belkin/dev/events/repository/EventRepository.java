package belkin.dev.events.repository;

import belkin.dev.events.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = """
            UPDATE events
            SET
                name = COALESCE(:name, name),
                max_places = COALESCE(:maxPlaces, max_places),
                date = COALESCE(:date, date),
                cost = COALESCE(:cost, cost),
                duration = COALESCE(:duration, duration),
                location_id = COALESCE(:locationId, location_id)
            WHERE id = :eventId
            """, nativeQuery = true)
    void updateEvent(@Param("eventId") Integer eventId,
                     @Param("name") String name,
                     @Param("maxPlaces") Integer maxPlaces,
                     @Param("date") LocalDateTime date,
                     @Param("cost") BigDecimal cost,
                     @Param("duration") Integer duration,
                     @Param("locationId") Integer locationId
    );

    @Query("""
                SELECT e from EventEntity e
                WHERE (:name IS NULL OR e.name LIKE %:name%)
                          AND (:placesMax IS NULL OR e.maxPlaces >= :placesMax)
                          AND (:placesMin IS NULL OR e.maxPlaces <= :placesMin)
                          AND (CAST(:dateAfter AS date) IS NULL OR e.date >= :dateAfter)
                          AND (CAST(:dateBefore AS date) IS NULL OR e.date <= :dateBefore)
                          AND (:costMin IS NULL OR e.cost >= :costMin)
                          AND (:costMax IS NULL OR e.cost <= :costMax)
                          AND (:durationMin IS NULL OR e.duration >= :durationMin)
                          AND (:durationMax IS NULL OR e.duration <= :durationMax)
                          AND (:locationId IS NULL OR e.locationId = :locationId)
                          AND (:status IS NULL OR e.status = :status)
            """)
    List<EventEntity> searchEventByFilter(@Param("name") String name,
                                          @Param("placesMax") Integer placesMax,
                                          @Param("placesMin") Integer placesMin,
                                          @Param("dateAfter") LocalDateTime dateStartAfter,
                                          @Param("dateBefore") LocalDateTime dateStarBefore,
                                          @Param("costMin") BigDecimal costMin,
                                          @Param("costMax") BigDecimal costMax,
                                          @Param("durationMin") Integer durationMin,
                                          @Param("durationMax") Integer durationMax,
                                          @Param("locationId") Integer locationId,
                                          @Param("status") String status);


    @Query("""
            
            SELECT e from EventEntity e
            WHERE e.ownerId = :ownerId
            
            """)
    List<EventEntity> findAllByOwnerId(@Param("ownerId") Integer ownerId);


    @Query("""
            SELECT e from EventEntity e
            JOIN FETCH e.registrationList r
            WHERE r.userId = :userId
            
            """)
    List<EventEntity> getAllEventsByUser(@Param("userId") Integer userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = """
            UPDATE events
            SET status = :status
            WHERE status = 'WAIT_START'
            AND date <= CURRENT_TIMESTAMP
            """, nativeQuery = true)
    void updateNotStartedEvent(@Param("status") String status);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = """
            UPDATE events
            SET status = :status
            WHERE status = 'STARTED'
            AND (date + (duration || ' minutes')::interval) <= CURRENT_TIMESTAMP
            """, nativeQuery = true)
    void updateStartedEvent(@Param("status") String status);
}

