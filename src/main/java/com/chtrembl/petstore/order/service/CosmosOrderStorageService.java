package com.chtrembl.petstore.order.service;


import com.chtrembl.petstore.order.model.Order;
import com.chtrembl.petstore.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("cosmosOrderStorageService")
@Slf4j
@RequiredArgsConstructor
public class CosmosOrderStorageService implements OrderStorageService {

    private final OrderRepository orderRepository;
    private final OrderConvertor convertor;

    @Override
    public Order saveOrder(Order order) {
        try {
            log.info("Persisting order {} to CosmosDB", order.getId());

            // Repository save returns the saved entity
            return convertor.toModel(orderRepository.save(convertor.toDao(order)));

        } catch (Exception e) {
            log.error("Failed to persist order {} to CosmosDB: {}", order.getId(), e.getMessage(), e);
            return order; // Return the original order even if persistence fails
        }
    }

    @Override
    public Order findById(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                log.warn("Attempted to find order with null or empty id");
                return null;
            }

            // Repository findById returns Optional
            return convertor.toModel(orderRepository.findById(id).orElse(null));

        } catch (Exception e) {
            log.error("Failed to read order {} from CosmosDB: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int countOrders() {
        try {
            // Repository count returns long
            return (int) orderRepository.count();

        } catch (Exception e) {
            log.error("Failed to count orders in CosmosDB: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Optional: Delete an order
     */
    public void deleteOrder(String id) {
        try {
            orderRepository.deleteById(id);
            log.info("Deleted order {} from CosmosDB", id);
        } catch (Exception e) {
            log.error("Failed to delete order {} from CosmosDB: {}", id, e.getMessage(), e);
        }
    }

    /**
     * Optional: Check if order exists
     */
    public boolean exists(String id) {
        try {
            return orderRepository.existsById(id);
        } catch (Exception e) {
            log.error("Failed to check existence of order {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}