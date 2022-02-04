package com.example.grpc.resource;

import com.example.grpc.*;
import com.example.grpc.dto.ProductInputDto;
import com.example.grpc.dto.ProductOutputDto;
import com.example.grpc.service.ProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        var productInputDto = ProductInputDto.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantityInStock(request.getQuantityInStock())
                .build();

        var productOutputDto = this.productService.create(productInputDto);
        var productResponse = ProductResponse.newBuilder()
                .setId(productOutputDto.getId())
                .setName(productInputDto.getName())
                .setPrice(productInputDto.getPrice())
                .setQuantityInStock(productInputDto.getQuantityInStock())
                .build();
        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
        var productById = request.getId();
        ProductOutputDto productOutputDto = this.productService.findById(productById);
        ProductResponse productResponse = ProductResponse.newBuilder()
                .setId(productOutputDto.getId())
                .setName(productOutputDto.getName())
                .setPrice(productOutputDto.getPrice())
                .setQuantityInStock(productOutputDto.getQuantityInStock())
                .build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RequestById request, StreamObserver<ProductResponse> responseObserver) {
        super.delete(request, responseObserver);
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        super.findAll(request, responseObserver);
    }
}
