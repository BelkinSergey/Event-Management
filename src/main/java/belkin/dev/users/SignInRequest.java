package belkin.dev.users;

import jakarta.validation.constraints.NotBlank;


public record SignInRequest(

        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
