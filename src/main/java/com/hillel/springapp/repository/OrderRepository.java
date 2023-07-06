package com.hillel.springapp.repository;

import com.hillel.springapp.entity.Order;
import com.hillel.springapp.entity.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @NonNull
    Optional<Order> findById(@NonNull Long id);

    @NonNull <S extends Order> S save(@NonNull S order);

    void delete(@NonNull Order order);

    @Modifying
    @Query("UPDATE Order o SET o.products = :productList WHERE o.id = :orderId")
    void addProductToOrder(@Param("orderId") Long orderId, @Param("productList") List<Product> productList);

    @Modifying
    @Query("UPDATE Order o SET o.products = :productList WHERE o.id = :orderId")
    void deleteProductFromOrder(@Param("orderId") Long orderId, @Param("productList") List<Product> productList);


    @NonNull
    List<Order> findAll();

    @Query("SELECT o.products FROM Order o WHERE o.id = :orderId")
    List<Product> findAllProductsInOrder(@Param("orderId") Long orderId);
}