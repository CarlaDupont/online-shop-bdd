package fr.carla.shop.domain;

public class Product {

    private final String reference;
    private final String name;
    private final String category;
    private final double price;

    public Product(String reference, String name, String category, double price) {
        this.reference = reference;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
}