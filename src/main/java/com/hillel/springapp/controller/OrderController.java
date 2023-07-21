package com.hillel.springapp.controller;

import com.hillel.springapp.dto.OrderDTO;
import com.hillel.springapp.dto.ProductDTO;
import com.hillel.springapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        if (orderDTO != null) {
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTOs = orderService.getAllOrders();
        return ResponseEntity.ok(orderDTOs);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.addOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{orderId}/products")
    public ResponseEntity<OrderDTO> addProductToOrder(@PathVariable Long orderId, @RequestBody ProductDTO productDTO) {
        OrderDTO updatedOrder = orderService.addProductToOrder(orderId, productDTO);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<OrderDTO> deleteProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        OrderDTO updatedOrder = orderService.deleteProductFromOrder(orderId, productId);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{orderId}/products")
    public ResponseEntity<List<ProductDTO>> getAllProductsInOrder(@PathVariable Long orderId) {
        List<ProductDTO> productDTOs = orderService.getAllProductsInOrder(orderId);
        if (productDTOs != null) {
            return ResponseEntity.ok(productDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}