package com.hillel.springapp.service;

import com.hillel.springapp.dto.OrderDTO;
import com.hillel.springapp.dto.ProductDTO;
import com.hillel.springapp.entity.Order;
import com.hillel.springapp.entity.Product;
import com.hillel.springapp.mapper.OrderMapper;
import com.hillel.springapp.mapper.ProductMapper;
import com.hillel.springapp.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
    }

    public OrderDTO getOrderById(Long id) {
        logger.info("Getting order by ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::orderToOrderDTO).orElse(null);
    }

    public List<OrderDTO> getAllOrders() {
        logger.info("Getting all orders");
        List<Order> orders = orderRepository.findAll();
        return orderMapper.ordersToOrderDTOs(orders);
    }

    public OrderDTO addOrder(OrderDTO orderDTO) {
        logger.info("Adding new order: {}", orderDTO);
        Order order = orderMapper.orderDTOToOrder(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(savedOrder);
    }

    public boolean deleteOrder(Long id) {
        logger.info("Deleting order with ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.delete(order.get());
            return true;
        } else {
            return false;
        }
    }

    public OrderDTO addProductToOrder(Long orderId, ProductDTO productDTO) {
        logger.info("Adding product to order. Order ID: {}, Product: {}", orderId, productDTO);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order existingOrder = order.get();
            List<Product> products = existingOrder.getProducts();
            products.add(productMapper.productDTOToProduct(productDTO));
            orderRepository.save(existingOrder);
            return orderMapper.orderToOrderDTO(existingOrder);
        } else {
            return null;
        }
    }

    public OrderDTO deleteProductFromOrder(Long orderId, Long productId) {
        logger.info("Deleting product from order. Order ID: {}, Product ID: {}", orderId, productId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order existingOrder = order.get();
            List<Product> products = existingOrder.getProducts();
            products.removeIf(p -> p.getId().equals(productId));
            orderRepository.save(existingOrder);
            return orderMapper.orderToOrderDTO(existingOrder);
        } else {
            return null;
        }
    }

    public OrderDTO clearOrder(Long orderId) {
        logger.info("Clearing order. Order ID: {}", orderId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order existingOrder = order.get();
            existingOrder.getProducts().clear();
            orderRepository.save(existingOrder);
            return orderMapper.orderToOrderDTO(existingOrder);
        } else {
            return null;
        }
    }

    public List<ProductDTO> getAllProductsInOrder(Long orderId) {
        logger.info("Getting all products in order. Order ID: {}", orderId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            List<Product> products = order.get().getProducts();
            return products.stream()
                    .map(productMapper::productToProductDTO)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}