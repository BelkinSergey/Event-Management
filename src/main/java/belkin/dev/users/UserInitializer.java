package belkin.dev.users;

import belkin.dev.users.model.UserEntity;
import belkin.dev.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserInitializer.class);

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createDefaultUser();
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private void createDefaultUser() {
        if (userRepository.findByRole("ADMIN").isEmpty()) {
            var admin = new UserEntity(
                    null,
                    "admin",
                    passwordEncoder.encode("admin"),
                    11,
                    UserRole.ADMIN.toString()
            );
            userRepository.save(admin);
        }
        if (userRepository.findByRole("USER").isEmpty()) {
            var user = new UserEntity(
                    null,
                    "user",
                    passwordEncoder.encode("user"),
                    11,
                    UserRole.USER.toString()
            );
            userRepository.save(user);
        }

    }
}
