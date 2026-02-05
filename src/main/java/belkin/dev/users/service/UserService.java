package belkin.dev.users.service;

import belkin.dev.users.SignUpRequest;
import belkin.dev.users.dto.User;

public interface UserService {
    User registerUser(SignUpRequest signUpRequest);

    User findByLogin(String login);

    User findUserById(Integer userId);
}
