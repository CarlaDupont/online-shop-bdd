package fr.carla.shop.service;

import fr.carla.shop.domain.User;
import fr.carla.shop.exception.AccountAlreadyExistsException;
import fr.carla.shop.repository.UserRepository;

public class RegistrationService {

    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(String email, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new AccountAlreadyExistsException(username);
        }

        User user = new User(email, username, password);
        userRepository.save(user);

        return "Account created for " + username;
    }
}