package belkin.dev.web;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,

        String detailMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime dateTime
) {
}
