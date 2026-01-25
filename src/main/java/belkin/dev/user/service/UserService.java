package belkin.dev.user.service;

import belkin.dev.user.SignUpRequest;
import belkin.dev.user.dto.User;

public interface UserService {
    User registerUser(SignUpRequest signUpRequest);

    User findByLogin(String login);

    User findUserById(Integer userId);
}
