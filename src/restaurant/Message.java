package restaurant;

import restaurant.kitchen.Order;

import java.io.Serializable;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class Message implements Serializable {
    private final MessageType messageType;
    private final String clientName;
    private final Order order;
    private final String text;

    public Message(MessageType messageType) {
        this.messageType = messageType;
        clientName = null;
        order = null;
        text = null;
    }

    public Message(MessageType messageType, Order order) {
        this.messageType = messageType;
        this.order = order;
        clientName = null;
        text = null;
    }

    public Message(MessageType messageType, String clientName) {
        this.messageType = messageType;
        this.clientName = clientName;
        order = null;
        text = null;
    }

    public Message(MessageType messageType, String clientName, String text) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.text = text;
        order = null;
    }

    public Message(MessageType messageType, Order order, String name) {
        this.messageType = messageType;
        this.order = order;
        clientName = null;
        text = name;
    }

    public Message(int tableNumber, String clientName, MessageType messageType) {
        this.clientName = clientName;
        this.messageType = messageType;
        order = null;
        text = null;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getClientName() {
        return clientName;
    }

    public Order getOrder() {
        return order;
    }

    public String getText() {
        return text;
    }
}
