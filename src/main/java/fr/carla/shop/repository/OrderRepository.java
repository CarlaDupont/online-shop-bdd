package fr.carla.shop.repository;

import fr.carla.shop.domain.CustomerOrder;

import java.util.Optional;

public interface OrderRepository {

    Optional<CustomerOrder> findById(String orderId);

    void save(CustomerOrder order);
}