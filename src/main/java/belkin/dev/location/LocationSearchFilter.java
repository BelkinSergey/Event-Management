package belkin.dev.location;

import jakarta.validation.constraints.Min;

public record LocationSearchFilter(

        String name,

        String address,

        @Min(0)
        Integer pageNumber,

        @Min(5)
        Integer pageSize

) {
}

