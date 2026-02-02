package belkin.dev.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventUpdateDto(

        String name,

        Integer maxPlaces,

        @FutureOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        LocalDateTime date,

        @Min(100)
        BigDecimal cost,

        @Min(30)
        Integer duration,

        @Min(1)
        Integer locationId

) {
}
