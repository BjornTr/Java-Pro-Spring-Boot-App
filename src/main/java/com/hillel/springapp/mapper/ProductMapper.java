package com.hillel.springapp.mapper;

import com.hillel.springapp.dto.ProductDTO;
import com.hillel.springapp.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);

    List<ProductDTO> productsToProductDTOs(List<Product> products);
}