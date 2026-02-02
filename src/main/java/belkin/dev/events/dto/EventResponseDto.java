package belkin.dev.events.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponseDto(

        Integer id,

        String name,

        Integer ownerId,

        Integer maxPlaces,

        Integer occupiedPlaces,

        LocalDateTime date,

        BigDecimal cost,

        Integer duration,

        Integer locationId,

        String status

) {
}
