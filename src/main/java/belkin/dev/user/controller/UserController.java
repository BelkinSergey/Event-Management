package belkin.dev.user.controller;

import belkin.dev.security.jwt.AuthenticationService;
import belkin.dev.security.jwt.JwtTokenResponse;
import belkin.dev.user.SignInRequest;
import belkin.dev.user.SignUpRequest;
import belkin.dev.user.dto.UserDto;
import belkin.dev.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }


    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        log.info("получен запрос на регистрацию пользователя: login={}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user.id(), user.login(), user.age(), user.role()));
    }


    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest
    ) {
        log.info("получен запрос на аутентификацию: {}", signInRequest);
        var token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Min(value = 1, message = "ID должен быть положительным числом")
            @Valid @PathVariable("id") Integer userId
    ) {
        log.info("получен запрос на поиск пользователья по id: login={}", userId);
        var user = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UserDto(user.id(), user.login(), user.age(), user.role()));
    }
}
