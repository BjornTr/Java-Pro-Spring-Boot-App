package com.hillel.springapp.service;

import com.hillel.springapp.dto.ProductDTO;
import com.hillel.springapp.entity.Product;
import com.hillel.springapp.mapper.ProductMapper;
import com.hillel.springapp.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);


    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDTO getProductById(Long id) {
        logger.info("Getting product by ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::productToProductDTO).orElse(null);
    }

    public List<ProductDTO> getAllProducts() {
        logger.info("Getting all products");
        List<Product> products = productRepository.findAll();
        return productMapper.productsToProductDTOs(products);
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        logger.info("Adding new product: {}", productDTO);
        Product product = productMapper.productDTOToProduct(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDTO(savedProduct);
    }

    public boolean deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}