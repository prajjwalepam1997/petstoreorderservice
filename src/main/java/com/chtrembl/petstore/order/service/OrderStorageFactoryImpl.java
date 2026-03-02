package com.chtrembl.petstore.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStorageFactoryImpl implements OrderStorageFactory {

    @Qualifier("inMemoryOrderStorageService")
    private final OrderStorageService inMemoryOrderStorageService;

    @Qualifier("cosmosOrderStorageService")
    private final OrderStorageService cosmosOrderStorageService;

    @Value("${order.storage.type:IN_MEMORY}")
    private String storageTypeProperty;

    @Override
    public OrderStorageService getCurrentStorage() {
        StorageType type;
        try {
            type = StorageType.valueOf(storageTypeProperty.toUpperCase());
        } catch (Exception ex) {
            log.warn("Invalid order.storage.type '{}', defaulting to IN_MEMORY", storageTypeProperty);
            type = StorageType.IN_MEMORY;
        }
        return getStorage(type);
    }

    @Override
    public OrderStorageService getStorage(StorageType type) {
        return switch (type) {
            case COSMOS -> cosmosOrderStorageService;
            case IN_MEMORY -> inMemoryOrderStorageService;
        };
    }
}
