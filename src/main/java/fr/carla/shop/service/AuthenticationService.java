package fr.carla.shop.service;

import fr.carla.shop.domain.User;
import fr.carla.shop.exception.AuthenticationFailedException;
import fr.carla.shop.repository.UserRepository;

public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(String username, String password) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(AuthenticationFailedException::new);

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationFailedException();
        }

        return "Redirected to home page";
    }
}