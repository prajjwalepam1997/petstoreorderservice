package com.chtrembl.petstore.order.service;

import com.chtrembl.petstore.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service("inMemoryOrderStorageService")
@Slf4j
@RequiredArgsConstructor
public class InMemoryOrderStorageService implements OrderStorageService {

    private static final String ORDERS = "orders";

    private final CacheManager cacheManager;

    @Override
    public Order saveOrder(Order order) {
        Cache cache = cacheManager.getCache(ORDERS);
        if (cache != null && order != null && order.getId() != null) {
            cache.put(order.getId(), order);
            log.info("Saved order {} in in-memory cache", order.getId());
        }
        return order;
    }

    @Override
    public Order findById(String id) {
        Cache cache = cacheManager.getCache(ORDERS);
        if (cache == null || id == null) {
            return null;
        }
        Cache.ValueWrapper wrapper = cache.get(id);
        if (wrapper == null) {
            return null;
        }
        Object value = wrapper.get();
        return value instanceof Order ? (Order) value : null;
    }

    @Override
    public int countOrders() {
        try {
            org.springframework.cache.concurrent.ConcurrentMapCache mapCache =
                    (org.springframework.cache.concurrent.ConcurrentMapCache) cacheManager.getCache(ORDERS);
            return mapCache != null ? mapCache.getNativeCache().size() : 0;
        } catch (Exception e) {
            log.warn("Could not get in-memory orders cache size: {}", e.getMessage());
            return 0;
        }
    }
}
