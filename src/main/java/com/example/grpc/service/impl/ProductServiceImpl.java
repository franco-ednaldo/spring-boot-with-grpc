package com.example.grpc.service.impl;

import com.example.grpc.domain.Product;
import com.example.grpc.dto.ProductInputDto;
import com.example.grpc.dto.ProductOutputDto;
import com.example.grpc.mapper.ProductConverter;
import com.example.grpc.repository.ProductRepository;
import com.example.grpc.service.ProductService;
import com.example.grpc.service.exception.AlreadyExistisException;
import com.example.grpc.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDto create(ProductInputDto productInputDto) {
        this.checkDuplicity(productInputDto.getName());
        var entity = ProductConverter.converter(productInputDto);
        var entitySaved = this.productRepository.save(entity);
        return ProductConverter.converter(entitySaved);
    }

    @Override
    public ProductOutputDto findById(Long id) {
        return this.productRepository.findById(id)
                .map(ProductConverter::converter)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        ProductOutputDto productOutputDto = this.findById(id);
        this.productRepository.deleteById(productOutputDto.getId());
    }

    @Override
    public List<ProductOutputDto> findAll() {
        return null;
    }

    private void checkDuplicity(final String name) {
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new AlreadyExistisException(name);
                });
    }
}
