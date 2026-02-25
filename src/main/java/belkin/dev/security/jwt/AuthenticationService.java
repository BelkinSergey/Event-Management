package belkin.dev.security.jwt;


import belkin.dev.users.SignInRequest;
import belkin.dev.users.dto.User;
import belkin.dev.users.model.UserEntity;
import belkin.dev.users.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
    }

    public String authenticateUser(SignInRequest signInRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.login(),
                        signInRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity userEntity = userRepository.findByLogin(signInRequest.login())
                .orElseThrow(() -> new RuntimeException("пользователь не найден"));
        return jwtTokenManager.generateToken(signInRequest.login(), userEntity.getId(), userEntity.getRole());
    }

    public User getCurrentAuthenticatedUserOrThrow() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication not present");
        }
        return (User) authentication.getPrincipal();
    }
}
