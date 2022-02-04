package com.example.grpc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInputDto {
    private final Long id;

    private final String name;

    private final Double price;

    private final Integer quantityInStock;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    @Override
    public String toString() {
        return "ProductInputDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
