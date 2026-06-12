package fr.carla.shop.exception;

public class ProductNotFoundInOrderException extends RuntimeException {

    public ProductNotFoundInOrderException(String productReference) {
        super("Product not found in order: " + productReference);
    }
}