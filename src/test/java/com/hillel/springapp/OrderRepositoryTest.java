package com.hillel.springapp;

import com.hillel.springapp.entity.Order;
import com.hillel.springapp.entity.Product;
import com.hillel.springapp.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;




    @Test
    public void testFindById() {
        Long orderId = 32L;
        Optional<Order> foundOrder = orderRepository.findById(orderId);


        Assertions.assertTrue(foundOrder.isPresent());
        Assertions.assertEquals(orderId, foundOrder.get().getId());
    }

    @Test
    public void testSave() {
        Order order = new Order();
        order.setDate("2023-07-09");
        order.setCost(400.0);
        order.setProducts(new ArrayList<>());

        Product product = new Product();
        product.setName("Product 1");
        product.setCost(50.0);

        List<Product> products = new ArrayList<>();
        products.add(product);
        order.setProducts(products);

        Order savedOrder = orderRepository.save(order);

        Assertions.assertNotNull(savedOrder.getId());
        Assertions.assertEquals(order.getDate(), savedOrder.getDate());
        Assertions.assertEquals(order.getCost(), savedOrder.getCost());
        Assertions.assertEquals(order.getProducts().size(), savedOrder.getProducts().size());
    }

    @Test
    public void testDelete() {
        Long orderId = 42L;
        Order order = new Order();
        order.setId(orderId);
        orderRepository.delete(order);

        Assertions.assertFalse(orderRepository.findById(orderId).isPresent());
    }

    @Test
    public void testAddProductToOrder() {
        Order order = new Order();
        order.setDate("2023-07-09");
        order.setCost(400.0);
        order.setProducts(new ArrayList<>());
        Order savedOrder = orderRepository.save(order);

        Product product = new Product();
        product.setName("Product 1");
        product.setCost(50.0);

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        orderRepository.addProductToOrder(savedOrder.getId(), productList);

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        Assertions.assertTrue(foundOrder.isPresent());
        Assertions.assertEquals(savedOrder.getId(), foundOrder.get().getId());
        Assertions.assertEquals(savedOrder.getDate(), foundOrder.get().getDate());
        Assertions.assertEquals(savedOrder.getCost(), foundOrder.get().getCost());
        Assertions.assertEquals(1, foundOrder.get().getProducts().size());
        Assertions.assertEquals(product.getName(), foundOrder.get().getProducts().get(0).getName());
        Assertions.assertEquals(product.getCost(), foundOrder.get().getProducts().get(0).getCost());
    }

    @Test
    public void testDeleteProductFromOrder() {
        Order order = new Order();
        order.setDate("2023-07-10");
        order.setCost(500.0);

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setCost(50.0);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setCost(100.0);

        products.add(product1);
        products.add(product2);

        order.setProducts(products);
        Order savedOrder = orderRepository.save(order);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        orderRepository.deleteProductFromOrder(savedOrder.getId(), productList);

        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        Assertions.assertTrue(retrievedOrder.isPresent());
        Assertions.assertEquals(1, retrievedOrder.get().getProducts().size());
        Assertions.assertEquals(product2.getName(), retrievedOrder.get().getProducts().get(0).getName());
        Assertions.assertEquals(product2.getCost(), retrievedOrder.get().getProducts().get(0).getCost());
    }


    @Test
    public void testFindAllProductsInOrder() {
        Order order = new Order();
        order.setDate("2023-07-12");
        order.setCost(700.0);
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setCost(50.0);
        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setCost(100.0);
        products.add(product1);
        products.add(product2);
        order.setProducts(products);
        Order savedOrder = orderRepository.save(order);
        List<Product> retrievedProducts = orderRepository.findAllProductsInOrder(savedOrder.getId());

        Assertions.assertNotNull(retrievedProducts);
        Assertions.assertEquals(2, retrievedProducts.size());
        Assertions.assertEquals(product1.getName(), retrievedProducts.get(0).getName());
        Assertions.assertEquals(product1.getCost(), retrievedProducts.get(0).getCost());
        Assertions.assertEquals(product2.getName(), retrievedProducts.get(1).getName());
        Assertions.assertEquals(product2.getCost(), retrievedProducts.get(1).getCost());
    }
}
