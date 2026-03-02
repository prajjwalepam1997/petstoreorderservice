package com.chtrembl.petstore.order.service;

public interface OrderStorageFactory {

    OrderStorageService getCurrentStorage();

    OrderStorageService getStorage(StorageType type);
}
