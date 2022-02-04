package com.example.grpc.resource;

import com.example.grpc.ProductRequest;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductServiceGrpc;
import com.example.grpc.RequestById;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceGrpcStub;

    @Autowired
    private Flyway flyway;

    private static final String ERROR_MESSAGE = "ALREADY_EXISTS: Produto Televisão LG já cadastrado no Sistema.";

    private static final String ERROR_MESSAGE_NOT_FOUND = "NOT_FOUND: Produto com ID %s não encontrado.";

    @BeforeEach
    public void init() {
        this.flyway.clean();
        this.flyway.migrate();
    }

    @Test
    @DisplayName("when valid data is provieded a product is created")
    public void create() {
        var productRequest = ProductRequest.newBuilder()
                .setName("Celular LG")
                .setPrice(1500.0)
                .setQuantityInStock(1)
                .build();

        var productResponde = this.productServiceGrpcStub
                .create(productRequest);

        assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantity_in_stock");

        assertThat(productResponde.getId()).isGreaterThan(0);
        assertThat(productResponde.getName()).isEqualTo(productRequest.getName());
        assertThat(productResponde.getPrice()).isEqualTo(productRequest.getPrice());
        assertThat(productResponde.getQuantityInStock()).isEqualTo(productRequest.getQuantityInStock());
    }

    @Test
    @DisplayName("Try to create product duplicate")
    public void tryToCreateDuplicateProduct() {
        var productRequest = ProductRequest.newBuilder()
                .setName("Televisão LG")
                .setPrice(1500.0)
                .setQuantityInStock(1)
                .build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.productServiceGrpcStub.create(productRequest))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("when find product by id, should return success")
    public void findByIdWithSuccess() {
        Long productId = 1l;
        var requestById = RequestById.newBuilder()
                .setId(productId)
                .build();
        ProductResponse productResponse = this.productServiceGrpcStub.findById(requestById);
        assertThat(productResponse.getId()).isEqualTo(productId);
        assertThat(productResponse.getName()).isEqualTo("CELULAR");
        assertThat(productResponse.getPrice()).isEqualTo(1000.99);
        assertThat(productResponse.getQuantityInStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("when find product by id, but not found product, and return some exception")
    public void findByIdWithError() {
        Long productId = 100l;
        var requestById = RequestById.newBuilder()
                .setId(productId)
                .build();

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> this.productServiceGrpcStub.findById(requestById))
                .withMessage(String.format(ERROR_MESSAGE_NOT_FOUND, productId));

    }

    private ProductRequest createComponent(String name, double price, Integer quantidade) {
        return ProductRequest.newBuilder()
                .setName(name)
                .setPrice(price)
                .setQuantityInStock(quantidade)
                .build();

    }

}
