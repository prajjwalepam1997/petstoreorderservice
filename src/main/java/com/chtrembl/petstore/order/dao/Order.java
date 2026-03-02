package com.chtrembl.petstore.order.dao;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.chtrembl.petstore.order.model.Product;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Container(containerName = "orders")
public class Order {

    @Id
    private String id;

    private String email;

    private List<Product> products;

    private Status status;

    private Boolean complete;

    public enum Status {
        PLACED, APPROVED, DELIVERED
    }
}