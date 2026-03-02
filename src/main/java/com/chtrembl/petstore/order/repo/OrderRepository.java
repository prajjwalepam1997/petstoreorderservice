package com.chtrembl.petstore.order.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.chtrembl.petstore.order.dao.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CosmosRepository<Order, String> {
    // You can add custom query methods here if needed
}
