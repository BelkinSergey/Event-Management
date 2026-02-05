package belkin.dev.events.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer ownerId;

    private Integer maxPlaces;

    private Integer occupiedPlaces;

    private LocalDateTime date;

    private BigDecimal cost;

    private Integer duration;

    private Integer locationId;

    private String status;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrationList;

    public EventEntity(Integer id,
                       String name,
                       Integer ownerId,
                       Integer maxPlaces,
                       Integer occupiedPlaces,
                       LocalDateTime date,
                       Integer duration,
                       BigDecimal cost,
                       Integer locationId,
                       String status,
                       List<RegistrationEntity> registrationList) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.duration = duration;
        this.cost = cost;
        this.locationId = locationId;
        this.status = status;
        this.registrationList = registrationList;
    }

    public EventEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RegistrationEntity> getRegistrationList() {
        return registrationList;
    }

    public void setRegistrationList(List<RegistrationEntity> registrationList) {
        this.registrationList = registrationList;
    }
}
