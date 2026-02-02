package belkin.dev.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record EventSearchDto(

        String name,

        @Min(1)
        Integer placesMin,

        @Min(1)
        Integer placesMax,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        LocalDateTime dateStartAfter,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        LocalDateTime dateStartBefore,

        @Min(0)
        BigDecimal costMin,

        @Min(1)
        BigDecimal costMax,

        @Min(0)
        Integer durationMin,

        @Min(0)
        Integer durationMax,

        @Min(1)
        Integer locationId,

        @Pattern(regexp = "WAIT_START|STARTED|CANCELLED|FINISHED",
                message = "Недопустимый статус")
        String status

) {
}
