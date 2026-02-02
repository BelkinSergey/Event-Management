package belkin.dev.events.repository;

import belkin.dev.events.model.EventEntity;
import belkin.dev.events.model.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE FROM RegistrationEntity r
            WHERE r.event = :event
            AND r.userId = :userId
            """)
    void canselRegistration(@Param("userId") Integer userId,
                            @Param("event") EventEntity event);
}
