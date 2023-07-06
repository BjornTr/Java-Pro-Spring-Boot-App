package com.hillel.springapp;

import com.hillel.springapp.controller.OrderController;
import com.hillel.springapp.dto.OrderDTO;
import com.hillel.springapp.dto.ProductDTO;
import com.hillel.springapp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderById_ValidId_ReturnsOrder() {
        long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO(orderId, "2023-07-06", 100.0, null);
        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void testGetOrderById_InvalidId_ReturnsNotFound() {
        long orderId = 1L;
        when(orderService.getOrderById(orderId)).thenReturn(null);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void testGetAllOrders_ReturnsOrders() {
        List<OrderDTO> orderDTOs = Arrays.asList(
                new OrderDTO(1L, "2023-07-06", 100.0, null),
                new OrderDTO(2L, "2023-07-07", 200.0, null)
        );
        when(orderService.getAllOrders()).thenReturn(orderDTOs);

        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTOs, response.getBody());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testAddOrder_ValidOrder_ReturnsCreatedOrder() {
        OrderDTO orderDTO = new OrderDTO(1L, "2023-07-06", 100.0, null);
        when(orderService.addOrder(any(OrderDTO.class))).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.addOrder(orderDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).addOrder(any(OrderDTO.class));
    }

    @Test
    void testDeleteOrder_ExistingId_ReturnsNoContent() {
        long orderId = 1L;
        when(orderService.deleteOrder(orderId)).thenReturn(true);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(orderId);
    }

    @Test
    void testDeleteOrder_NonExistingId_ReturnsNotFound() {
        long orderId = 1L;
        when(orderService.deleteOrder(orderId)).thenReturn(false);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(orderId);
    }

    @Test
    void testAddProductToOrder_ExistingOrderAndValidProduct_ReturnsUpdatedOrder() {
        long orderId = 1L;
        long productId = 2L;
        OrderDTO updatedOrderDTO = new OrderDTO(orderId, "2023-07-06", 100.0, null);
        ProductDTO productDTO = new ProductDTO(productId, "Product", 50.0);
        when(orderService.addProductToOrder(eq(orderId), any(ProductDTO.class))).thenReturn(updatedOrderDTO);

        ResponseEntity<OrderDTO> response = orderController.addProductToOrder(orderId, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrderDTO, response.getBody());
        verify(orderService, times(1)).addProductToOrder(eq(orderId), any(ProductDTO.class));
    }

    @Test
    void testAddProductToOrder_NonExistingOrder_ReturnsNotFound() {
        long orderId = 1L;
        long productId = 2L;
        ProductDTO productDTO = new ProductDTO(productId, "Product", 50.0);
        when(orderService.addProductToOrder(eq(orderId), any(ProductDTO.class))).thenReturn(null);

        ResponseEntity<OrderDTO> response = orderController.addProductToOrder(orderId, productDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, times(1)).addProductToOrder(eq(orderId), any(ProductDTO.class));
    }

    @Test
    void testDeleteProductFromOrder_ExistingOrderAndProduct_ReturnsUpdatedOrder() {
        long orderId = 1L;
        long productId = 2L;
        OrderDTO updatedOrderDTO = new OrderDTO(orderId, "2023-07-06", 100.0, null);
        when(orderService.deleteProductFromOrder(eq(orderId), eq(productId))).thenReturn(updatedOrderDTO);

        ResponseEntity<OrderDTO> response = orderController.deleteProductFromOrder(orderId, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrderDTO, response.getBody());
        verify(orderService, times(1)).deleteProductFromOrder(eq(orderId), eq(productId));
    }

    @Test
    void testDeleteProductFromOrder_NonExistingOrder_ReturnsNotFound() {
        long orderId = 1L;
        long productId = 2L;
        when(orderService.deleteProductFromOrder(eq(orderId), eq(productId))).thenReturn(null);

        ResponseEntity<OrderDTO> response = orderController.deleteProductFromOrder(orderId, productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, times(1)).deleteProductFromOrder(eq(orderId), eq(productId));
    }

    @Test
    void testGetAllProductsInOrder_ExistingOrder_ReturnsProducts() {
        long orderId = 1L;
        List<ProductDTO> productDTOs = Arrays.asList(
                new ProductDTO(1L, "Product 1", 10.0),
                new ProductDTO(2L, "Product 2", 20.0)
        );
        when(orderService.getAllProductsInOrder(orderId)).thenReturn(productDTOs);

        ResponseEntity<List<ProductDTO>> response = orderController.getAllProductsInOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTOs, response.getBody());
        verify(orderService, times(1)).getAllProductsInOrder(orderId);
    }

    @Test
    void testGetAllProductsInOrder_NonExistingOrder_ReturnsNotFound() {
        long orderId = 1L;
        when(orderService.getAllProductsInOrder(orderId)).thenReturn(null);

        ResponseEntity<List<ProductDTO>> response = orderController.getAllProductsInOrder(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, times(1)).getAllProductsInOrder(orderId);
    }
}
