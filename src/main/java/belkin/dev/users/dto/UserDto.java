package belkin.dev.users.dto;

import belkin.dev.users.UserRole;

public record UserDto(

        Integer id,

        String login,

        Integer age,

        UserRole role

) {
}
