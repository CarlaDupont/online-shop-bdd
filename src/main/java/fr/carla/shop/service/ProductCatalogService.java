package fr.carla.shop.service;

import fr.carla.shop.domain.Product;
import fr.carla.shop.exception.ProductNotFoundException;
import fr.carla.shop.repository.ProductRepository;

import java.util.List;

public class ProductCatalogService {

    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> searchByKeyword(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    public List<Product> searchByMaxPrice(double maxPrice) {
        return productRepository.searchByMaxPrice(maxPrice);
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product findByReference(String reference) {
        return productRepository
                .findByReference(reference)
                .orElseThrow(() -> new ProductNotFoundException(reference));
    }
}