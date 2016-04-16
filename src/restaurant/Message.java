package restaurant;

import restaurant.kitchen.Dish;
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
    private final double bill;
    private final int numberOfDishes;
    private final Dish dish;

    public Message(MessageType messageType) {
        this.messageType = messageType;
        clientName = null;
        order = null;
        text = null;
        bill = 0;
        numberOfDishes = 0;
        dish = null;
    }

    public Message(MessageType messageType, Order order) {
        this.messageType = messageType;
        this.order = order;
        clientName = null;
        text = null;
        bill = 0;
        numberOfDishes = 0;
        dish = null;
    }

    public Message(MessageType messageType, String clientName) {
        this.messageType = messageType;
        this.clientName = clientName;
        order = null;
        text = null;
        bill = 0;
        numberOfDishes = 0;
        dish = null;
    }

    public Message(MessageType messageType, int numberOfDishes) {
        this.messageType = messageType;
        this.numberOfDishes = numberOfDishes;
        clientName = null;
        order = null;
        text = null;
        bill = 0;
        dish = null;
    }

    public Message(MessageType messageType, Dish dish) {
        this.messageType = messageType;
        this.dish = dish;
        this.clientName = null;
        this.order = null;
        this.text = null;
        this.bill = 0;
        this.numberOfDishes = 0;
    }

    public Message(MessageType messageType, String clientName, double bill) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.bill = bill;
        order = null;
        text = null;
        numberOfDishes = 0;
        dish = null;
    }

    public Message(MessageType messageType, String clientName, String text) {
        this.messageType = messageType;
        this.clientName = clientName;
        this.text = text;
        order = null;
        bill = 0;
        numberOfDishes = 0;
        dish = null;
    }

    public Message(MessageType messageType, Order order, String name) {
        this.messageType = messageType;
        this.order = order;
        clientName = null;
        text = name;
        bill = 0;
        numberOfDishes = 0;
        dish = null;
    }

    public Message(int tableNumber, String clientName, MessageType messageType) {
        this.clientName = clientName;
        this.messageType = messageType;
        order = null;
        text = null;
        bill = 0;
        numberOfDishes = 0;
        dish = null;
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

    public int getNumberOfDishes() {
        return numberOfDishes;
    }

    public Dish getDish() {
        return dish;
    }

    public String getText() {
        return text;
    }

    public double getBill() {
        return bill;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", clientName='" + clientName + '\'' +
                ", order=" + order +
                ", text='" + text + '\'' +
                ", bill=" + bill +
                ", numberOfDishes=" + numberOfDishes +
                ", dish=" + dish +
                '}';
    }
}
