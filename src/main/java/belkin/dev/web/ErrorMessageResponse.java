package belkin.dev.web;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,

        String detailMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime dateTime
) {
}
