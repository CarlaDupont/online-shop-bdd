package fr.carla.shop.service;

import fr.carla.shop.domain.CustomerOrder;
import fr.carla.shop.domain.Product;
import fr.carla.shop.exception.OrderNotFoundException;
import fr.carla.shop.exception.ProductNotFoundInOrderException;
import fr.carla.shop.repository.OrderRepository;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String addProductToOrder(String orderId, Product product) {
        CustomerOrder order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.addProduct(product);
        orderRepository.save(order);

        return "Product added to order";
    }

    public String removeProductFromOrder(String orderId, String productReference) {
        CustomerOrder order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.containsProduct(productReference)) {
            throw new ProductNotFoundInOrderException(productReference);
        }

        order.removeOneProduct(productReference);
        orderRepository.save(order);

        return "Product removed from order";
    }

    public String validateOrder(String orderId) {
        CustomerOrder order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        orderRepository.save(order);

        return "Order confirmed";
    }
}