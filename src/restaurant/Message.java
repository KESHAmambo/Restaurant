package restaurant;

import restaurant.kitchen.Order;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class Message {
    private final MessageType messageType;
    private final String clientName;
    private final int tableNumber;
    private final Order order;
    private final String text;

    public Message(MessageType messageType) {
        this.messageType = messageType;
        clientName = null;
        order = null;
        text = null;
        tableNumber = 0;
    }

    public Message(MessageType messageType, String text) {
        this.messageType = messageType;
        this.text = text;
        clientName = null;
        tableNumber = 0;
        order = null;
    }

    public Message(MessageType messageType, Order order) {
        this.messageType = messageType;
        this.order = order;
        clientName = null;
        tableNumber = 0;
        text = null;
    }

    public Message(MessageType messageType, String clientName, int tableNumber) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.tableNumber = tableNumber;
        order = null;
        text = null;
    }

    public Message(MessageType messageType, String clientName, String text) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.text = text;
        tableNumber = 0;
        order = null;
    }

    public Message(MessageType messageType, Order order, String name) {
        this.messageType = messageType;
        this.order = order;
        clientName = null;
        text = name;
        tableNumber = 0;
    }

    public Message(int tableNumber, String clientName, MessageType messageType) {
        this.tableNumber = tableNumber;
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

    public int getTableNumber() {
        return tableNumber;
    }

    public Order getOrder() {
        return order;
    }

    public String getText() {
        return text;
    }
}
