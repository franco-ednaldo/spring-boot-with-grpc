package com.example.grpc.service;

import com.example.grpc.dto.ProductInputDto;
import com.example.grpc.dto.ProductOutputDto;

import java.util.List;

public interface ProductService {
    ProductOutputDto create(final ProductInputDto productInputDto);

    ProductOutputDto findById(final Long id);

    void delete(final Long id);

    List<ProductOutputDto> findAll();
}
