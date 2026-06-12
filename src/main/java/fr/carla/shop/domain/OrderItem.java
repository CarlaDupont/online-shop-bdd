package fr.carla.shop.domain;

public class OrderItem {

    private final Product product;
    private int quantity;

    public OrderItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}