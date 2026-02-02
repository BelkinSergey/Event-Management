package belkin.dev.events.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Event(


        String name,

        Integer maxPlaces,

        LocalDateTime date,

        BigDecimal cost,

        Integer duration,

        Integer locationId


) {
}
