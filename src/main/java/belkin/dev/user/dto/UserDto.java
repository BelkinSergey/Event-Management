package belkin.dev.user.dto;

import belkin.dev.user.UserRole;

public record UserDto(

        Integer id,

        String login,

        Integer age,

        UserRole role

) {
}
