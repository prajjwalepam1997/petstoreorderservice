package com.chtrembl.petstore.order.service;

import com.chtrembl.petstore.order.model.Order;

public interface OrderStorageService {

    Order saveOrder(Order order);

    Order findById(String id);

    int countOrders();
}
