package belkin.dev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LocationDto(

        @Null
        Integer id,

        @NotBlank(message = "Имя не может быть пустым!")
        @Size(max = 50)
        String name,

        @NotBlank(message = "Адрес не может быть пустым!")
        @Size(max = 100)
        String address,

        @Min(value = 5, message = "Вместимость локации минимум 5 человек!")
        Integer capacity,

        String description
) {

}
