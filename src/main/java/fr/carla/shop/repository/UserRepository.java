package fr.carla.shop.repository;

import fr.carla.shop.domain.User;

import java.util.Optional;

public interface UserRepository {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    void save(User user);
}