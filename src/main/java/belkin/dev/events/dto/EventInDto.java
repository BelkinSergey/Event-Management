package belkin.dev.events.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventInDto(

        @NotBlank(message = "Имя не может быть пустым!")
        String name,

        @NotNull
        @Positive
        @Min(0)
        Integer maxPlaces,

        @NotNull
        @FutureOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        LocalDateTime date,

        @NotNull
        @Min(100)
        BigDecimal cost,

        @NotNull
        @Min(15)
        Integer duration,

        @NotNull
        @Positive
        Integer locationId

) {

}
