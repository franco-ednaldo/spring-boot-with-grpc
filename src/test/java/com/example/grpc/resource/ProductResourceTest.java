package com.example.grpc.resource;

import com.example.grpc.ProductRequest;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductServiceGrpc;
import com.example.grpc.RequestById;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceGrpcStub;

    @Autowired
    private Flyway flyway;

    private static final String ERROR_MESSAGE = "ALREADY_EXISTS: Produto Televisão LG já cadastrado no Sistema.";

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

        Assertions.assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantity_in_stock");

        Assertions.assertThat(productResponde.getId()).isGreaterThan(0);
        Assertions.assertThat(productResponde.getName()).isEqualTo(productRequest.getName());
        Assertions.assertThat(productResponde.getPrice()).isEqualTo(productRequest.getPrice());
        Assertions.assertThat(productResponde.getQuantityInStock()).isEqualTo(productRequest.getQuantityInStock());
    }

    @Test
    @DisplayName("Try to create product duplicate")
    public void tryToCreateDuplicateProduct() {
        var productRequest = ProductRequest.newBuilder()
                .setName("Televisão LG")
                .setPrice(1500.0)
                .setQuantityInStock(1)
                .build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
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
        Assertions.assertThat(productResponse.getId()).isEqualTo(productId);
        Assertions.assertThat(productResponse.getName()).isEqualTo("CELULAR");
        Assertions.assertThat(productResponse.getPrice()).isEqualTo(1000.99);
        Assertions.assertThat(productResponse.getQuantityInStock()).isEqualTo(10);
    }

    private ProductRequest createComponent(String name, double price, Integer quantidade) {
        return ProductRequest.newBuilder()
                .setName(name)
                .setPrice(price)
                .setQuantityInStock(quantidade)
                .build();

    }


}
