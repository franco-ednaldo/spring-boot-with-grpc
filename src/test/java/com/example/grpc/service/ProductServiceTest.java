package com.example.grpc.service;

import com.example.grpc.domain.Product;
import com.example.grpc.dto.ProductInputDto;
import com.example.grpc.repository.ProductRepository;
import com.example.grpc.service.exception.BaseBusinessException;
import com.example.grpc.service.exception.NotFoundException;
import com.example.grpc.service.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    private static final String ERROR_MESSAGE = "Produto Celular já cadastrado no Sistema.";

    private static final String ERROR_MESSAGE_NOT_FOUND = "Produto com ID %s não encontrado.";

    @BeforeEach
    public void init() {
        this.productService = new ProductServiceImpl(this.productRepository);
    }

    @Test
    @DisplayName("When create product service is call with valid data a product is returned")
    public void createProduct() {
        var productSave = this.createProductEntity(1l, "Celular", 2000.0, 1);
        var productInputDto = this.createProductInputDto(null, "Celular", 2000.0, 1);
        Mockito.when(this.productRepository.save(Mockito.any(Product.class))).thenReturn(productSave);

        var result = this.productService.create(productInputDto);

        Assertions.assertThat(result.getId()).isEqualTo(productSave.getId());
        Assertions.assertThat(result.getName()).isEqualTo(productSave.getName());
        Assertions.assertThat(result.getPrice()).isEqualTo(productSave.getPrice());
        Assertions.assertThat(result.getQuantityInStock()).isEqualTo(productSave.getQuantityInStock());
    }

    @Test
    @DisplayName("Try to create product duplicate")
    public void tryToCreateDuplicateProduct() {
        var productSave = this.createProductEntity(1l, "Celular", 2000.0, 1);
        var productInputDto = this.createProductInputDto(1l, "Celular", 2000.0, 1);
        Mockito.when(this.productRepository.findByNameIgnoreCase(Mockito.any(String.class)))
                .thenReturn(Optional.of(productSave));

        Throwable exception = Assertions.catchThrowable(() -> this.productService.create(productInputDto));
        Assertions.assertThat(exception)
                .isInstanceOf(BaseBusinessException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("when not find some product by id, and then return exception")
    public void findByIdWithException() {
        final Long productId = 1l;
        Mockito.when(this.productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(()-> this.productService.findById(productId))
                .withMessage(String.format(ERROR_MESSAGE_NOT_FOUND, productId));

    }

    @Test
    @DisplayName("when find some product by id, and return success")
    public void findByIdWithSuccess() {
        final Long productId = 1l;
        var productSave = this.createProductEntity(productId, "Celular", 2000.0, 1);
        Mockito.when(this.productRepository.findById(productId)).thenReturn(Optional.of(productSave));

        var result = this.productService.findById(productId);

        Assertions.assertThat(result.getId()).isEqualTo(productSave.getId());
        Assertions.assertThat(result.getName()).isEqualTo(productSave.getName());
        Assertions.assertThat(result.getPrice()).isEqualTo(productSave.getPrice());
        Assertions.assertThat(result.getQuantityInStock()).isEqualTo(productSave.getQuantityInStock());

    }

    private ProductInputDto createProductInputDto(Long id, String name, double price, int quantityInStock) {
        return ProductInputDto.builder()
                .id(id)
                .name(name)
                .price(price)
                .quantityInStock(quantityInStock)
                .build();
    }

    private Product createProductEntity(Long id, String name, double price, Integer quantityInStock) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .quantityInStock(quantityInStock)
                .build();
    }

}
