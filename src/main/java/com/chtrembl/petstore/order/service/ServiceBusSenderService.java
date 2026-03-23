package com.chtrembl.petstore.order.service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

/**
 * Service for sending messages to Azure Service Bus.
 */
@Service
@Slf4j
public class ServiceBusSenderService {

    private final ServiceBusSenderClient senderClient;
    private final String queueName;

    public ServiceBusSenderService(
            @Value("${azure.servicebus.connection-string}") String connectionString,
            @Value("${azure.servicebus.queue-name}") String queueName) {
        this.queueName = queueName;
        
        if (connectionString == null || connectionString.isBlank()) {
            log.warn("Azure Service Bus connection string is not configured. Service Bus messaging will be disabled.");
            this.senderClient = null;
        } else {
            this.senderClient = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .queueName(queueName)
                    .buildClient();
            log.info("Azure Service Bus sender client initialized for queue: {}", queueName);
        }
    }

    /**
     * Send a message to the Azure Service Bus queue.
     *
     * @param message the message content as JSON string
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String message) {
        if (senderClient == null) {
            log.warn("Service Bus client is not initialized. Message will not be sent.");
            return false;
        }

        try {
            ServiceBusMessage serviceBusMessage = new ServiceBusMessage(message);
            serviceBusMessage.setContentType("application/json");
            
            senderClient.sendMessage(serviceBusMessage);
            log.info("Successfully sent message to Service Bus queue: {}", queueName);
            return true;
        } catch (Exception e) {
            log.error("Error sending message to Service Bus: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Send a message with a custom message ID for tracking.
     *
     * @param message the message content as JSON string
     * @param messageId the message ID for tracking
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String message, String messageId) {
        if (senderClient == null) {
            log.warn("Service Bus client is not initialized. Message will not be sent.");
            return false;
        }

        try {
            ServiceBusMessage serviceBusMessage = new ServiceBusMessage(message);
            serviceBusMessage.setContentType("application/json");
            serviceBusMessage.setMessageId(messageId);
            
            senderClient.sendMessage(serviceBusMessage);
            log.info("Successfully sent message to Service Bus queue: {} with messageId: {}", queueName, messageId);
            return true;
        } catch (Exception e) {
            log.error("Error sending message to Service Bus: {}", e.getMessage(), e);
            return false;
        }
    }

    @PreDestroy
    public void cleanup() {
        if (senderClient != null) {
            senderClient.close();
            log.info("Service Bus sender client closed.");
        }
    }
}
