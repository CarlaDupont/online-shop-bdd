package fr.carla.shop.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productReference) {
        super("Product not found: " + productReference);
    }
}