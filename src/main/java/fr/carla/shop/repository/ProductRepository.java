package fr.carla.shop.repository;

import fr.carla.shop.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> searchByKeyword(String keyword);

    List<Product> searchByMaxPrice(double maxPrice);

    List<Product> findByCategory(String category);

    Optional<Product> findByReference(String reference);
}