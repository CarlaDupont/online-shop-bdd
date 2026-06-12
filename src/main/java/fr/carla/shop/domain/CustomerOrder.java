package fr.carla.shop.domain;

import java.util.HashMap;
import java.util.Map;

public class CustomerOrder {

    private final String id;
    private final Map<String, OrderItem> items;

    public CustomerOrder(String id) {
        this.id = id;
        this.items = new HashMap<>();
    }

    public void addProduct(Product product) {
        OrderItem existingItem = items.get(product.getReference());

        if (existingItem == null) {
            items.put(product.getReference(), new OrderItem(product));
        } else {
            existingItem.increaseQuantity();
        }
    }

    public void removeOneProduct(String productReference) {
        OrderItem existingItem = items.get(productReference);

        if (existingItem.getQuantity() > 1) {
            existingItem.decreaseQuantity();
        } else {
            items.remove(productReference);
        }
    }

    public boolean containsProduct(String productReference) {
        return items.containsKey(productReference);
    }

    public int getProductQuantity(String productReference) {
        OrderItem item = items.get(productReference);

        if (item == null) {
            return 0;
        }

        return item.getQuantity();
    }

    public String getId() {
        return id;
    }

    public Map<String, OrderItem> getItems() {
        return items;
    }
}