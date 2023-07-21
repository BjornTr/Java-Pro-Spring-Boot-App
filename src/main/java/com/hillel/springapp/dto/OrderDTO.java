package com.hillel.springapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private long id;
    private String date;
    private double cost;
    private List<ProductDTO> products;
}
