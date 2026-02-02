package belkin.dev.locations.dto;

public record Location(

        Integer id,

        String name,

        String address,

        Integer capacity,

        String description
) {
}
