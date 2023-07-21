package com.hillel.springapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private double cost;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}