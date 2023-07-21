package com.hillel.springapp;

import com.hillel.springapp.dto.OrderDTO;
import com.hillel.springapp.dto.ProductDTO;
import com.hillel.springapp.entity.Order;
import com.hillel.springapp.entity.Product;
import com.hillel.springapp.mapper.OrderMapper;
import com.hillel.springapp.mapper.ProductMapper;
import com.hillel.springapp.repository.OrderRepository;
import com.hillel.springapp.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductMapper productMapper;

    private OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, orderMapper, productMapper);
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(orderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderId, result.getId());
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        List<OrderDTO> orderDTOs = new ArrayList<>();
        orderDTOs.add(new OrderDTO());
        orderDTOs.add(new OrderDTO());

        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Mockito.when(orderMapper.ordersToOrderDTOs(orders)).thenReturn(orderDTOs);

        List<OrderDTO> result = orderService.getAllOrders();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testAddOrder() {
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();

        Mockito.when(orderMapper.orderDTOToOrder(orderDTO)).thenReturn(order);
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.addOrder(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDTO, result);
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;
        Order order = new Order();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.doNothing().when(orderRepository).delete(order);

        boolean result = orderService.deleteOrder(orderId);

        Assertions.assertTrue(result);
        Mockito.verify(orderRepository).delete(order);
    }

    @Test
    void testAddProductToOrder() {
        Long orderId = 1L;
        Long productId = 2L;
        Order existingOrder = new Order();
        ProductDTO productDTO = new ProductDTO();
        Product product = new Product();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        Mockito.when(productMapper.productDTOToProduct(productDTO)).thenReturn(product);
        Mockito.when(orderMapper.orderToOrderDTO(existingOrder)).thenReturn(new OrderDTO());

        OrderDTO result = orderService.addProductToOrder(orderId, productDTO);

        Assertions.assertNotNull(result);
        Mockito.verify(orderRepository).save(existingOrder);
    }

    @Test
    void testDeleteProductFromOrder() {
        Long orderId = 1L;
        Long productId = 2L;
        Order existingOrder = new Order();
        Product product = new Product();
        product.setId(productId);

        existingOrder.getProducts().add(product);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        OrderDTO result = orderService.deleteProductFromOrder(orderId, productId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, existingOrder.getProducts().size());
        Mockito.verify(orderRepository).save(existingOrder);
    }

    @Test
    void testClearOrder() {
        Long orderId = 1L;
        Order existingOrder = new Order();
        Product product = new Product();

        existingOrder.getProducts().add(product);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        OrderDTO result = orderService.clearOrder(orderId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(existingOrder.getProducts().isEmpty());
        Mockito.verify(orderRepository).save(existingOrder);
    }

    @Test
    void testGetAllProductsInOrder() {
        Long orderId = 1L;
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        order.setProducts(products);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(productMapper.productToProductDTO(Mockito.any())).thenReturn(new ProductDTO());

        List<ProductDTO> result = orderService.getAllProductsInOrder(orderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}