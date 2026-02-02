package belkin.dev.users.service;

import belkin.dev.users.SignUpRequest;
import belkin.dev.users.UserRole;
import belkin.dev.users.dto.User;
import belkin.dev.users.model.UserEntity;
import belkin.dev.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("Такой логин уже занят!");
        }
        var hashedPass = passwordEncoder.encode(signUpRequest.password());
        var userToSave = new UserEntity(
                null,
                signUpRequest.login(),
                hashedPass,
                signUpRequest.age(),
                UserRole.USER.name()
        );
        var saved = userRepository.save(userToSave);
        return mapToDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("ползьзователь не найден"));
        return mapToDomain(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Integer userId) {
        var user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new EntityNotFoundException("сущность с id " + userId + " не найдена"));
        return mapToDomain(user);
    }

    private static User mapToDomain(UserEntity entity) {

        return new User(
                entity.getId(),
                entity.getLogin(),
                entity.getAge(),
                UserRole.valueOf(entity.getRole())
        );
    }
}
