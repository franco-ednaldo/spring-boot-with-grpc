package com.example.grpc.mapper;

import com.example.grpc.domain.Product;
import com.example.grpc.dto.ProductInputDto;
import com.example.grpc.dto.ProductOutputDto;

public class ProductConverter {

    static public Product converter(ProductInputDto productInputDto) {
        return Product.builder()
                .name(productInputDto.getName())
                .price(productInputDto.getPrice())
                .quantityInStock(productInputDto.getQuantityInStock())
                .build();
    }

    static public ProductOutputDto converter(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .build();
    }
}
