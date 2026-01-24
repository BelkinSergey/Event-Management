package belkin.dev.repository;

import belkin.dev.model.LocationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {


    @Query("""
            SELECT l FROM LocationEntity l
            WHERE (:name IS NULL OR l.name=:name)
            AND (:address IS NULL OR l.address=:address)
            """)
    List<LocationEntity> searchAllLocations(@Param("name") String name,
                                            @Param("address") String address,
                                            Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = """
            UPDATE locations
            SET
                name = COALESCE(:name, name),
                address = COALESCE(:address, address),
                capacity = COALESCE(:capacity, capacity),
                description = COALESCE(:description, description)
            WHERE id = :id
            """, nativeQuery = true)
    void locationToUpdate(@Param("id") Integer id,
                          @Param("name") String name,
                          @Param("address") String address,
                          @Param("capacity") Integer capacity,
                          @Param("description") String description);
}
