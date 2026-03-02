package com.chtrembl.petstore.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderConvertor {

    /**
     * Convert DAO Order (from dao package) to Model Order (from model package)
     */
    public com.chtrembl.petstore.order.model.Order toModel(com.chtrembl.petstore.order.dao.Order dao) {
        if (dao == null) {
            return null;
        }

        com.chtrembl.petstore.order.model.Order.OrderBuilder builder = com.chtrembl.petstore.order.model.Order.builder()
                .id(dao.getId())
                .email(dao.getEmail())
                .complete(dao.getComplete() != null ? dao.getComplete() : false);

        // Convert status from DAO enum to Model enum
        if (dao.getStatus() != null) {
            builder.status(mapStatusToModel(dao.getStatus()));
        }

        // Products are the same in both packages (assuming they're from model package)
        if (dao.getProducts() != null && !dao.getProducts().isEmpty()) {
            builder.products(new ArrayList<>(dao.getProducts()));
        } else {
            builder.products(new ArrayList<>());
        }

        return builder.build();
    }

    /**
     * Convert Model Order (from model package) to DAO Order (from dao package)
     */
    public com.chtrembl.petstore.order.dao.Order toDao(com.chtrembl.petstore.order.model.Order model) {
        if (model == null) {
            return null;
        }

        com.chtrembl.petstore.order.dao.Order.OrderBuilder builder = com.chtrembl.petstore.order.dao.Order.builder()
                .id(model.getId())
                .email(model.getEmail())
                .complete(model.getComplete());

        // Convert status from Model enum to DAO enum
        if (model.getStatus() != null) {
            builder.status(mapStatusToDao(model.getStatus()));
        }

        // Products are the same in both packages
        if (model.getProducts() != null && !model.getProducts().isEmpty()) {
            builder.products(new ArrayList<>(model.getProducts()));
        } else {
            builder.products(new ArrayList<>());
        }

        return builder.build();
    }

    /**
     * Convert list of DAO Orders to list of Model Orders
     */
    public List<com.chtrembl.petstore.order.model.Order> toModelList(List<com.chtrembl.petstore.order.dao.Order> daoList) {
        if (daoList == null) {
            return new ArrayList<>();
        }
        return daoList.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of Model Orders to list of DAO Orders
     */
    public List<com.chtrembl.petstore.order.dao.Order> toDaoList(List<com.chtrembl.petstore.order.model.Order> modelList) {
        if (modelList == null) {
            return new ArrayList<>();
        }
        return modelList.stream()
                .map(this::toDao)
                .collect(Collectors.toList());
    }

    /**
     * Map DAO Status enum to Model Status enum
     */
    private com.chtrembl.petstore.order.model.Order.Status mapStatusToModel(com.chtrembl.petstore.order.dao.Order.Status daoStatus) {
        if (daoStatus == null) {
            return null;
        }

        switch (daoStatus) {
            case PLACED:
                return com.chtrembl.petstore.order.model.Order.Status.PLACED;
            case APPROVED:
                return com.chtrembl.petstore.order.model.Order.Status.APPROVED;
            case DELIVERED:
                return com.chtrembl.petstore.order.model.Order.Status.DELIVERED;
            default:
                log.warn("Unknown DAO status: {}, defaulting to PLACED", daoStatus);
                return com.chtrembl.petstore.order.model.Order.Status.PLACED;
        }
    }

    /**
     * Map Model Status enum to DAO Status enum
     */
    private com.chtrembl.petstore.order.dao.Order.Status mapStatusToDao(com.chtrembl.petstore.order.model.Order.Status modelStatus) {
        if (modelStatus == null) {
            return null;
        }

        switch (modelStatus) {
            case PLACED:
                return com.chtrembl.petstore.order.dao.Order.Status.PLACED;
            case APPROVED:
                return com.chtrembl.petstore.order.dao.Order.Status.APPROVED;
            case DELIVERED:
                return com.chtrembl.petstore.order.dao.Order.Status.DELIVERED;
            default:
                log.warn("Unknown model status: {}, defaulting to PLACED", modelStatus);
                return com.chtrembl.petstore.order.dao.Order.Status.PLACED;
        }
    }
}