package restaurant.kitchen;

import java.io.Serializable;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class Message implements Serializable {
    private final MessageType messageType;
    private final String firstString;
    private final String secondString;
    private final Order order;
    private final double bill;
    private final int imagesCount;
    private final Dish dish;
    private final Menu menu;

    public Message(MessageType messageType) {
        this.messageType = messageType;
        firstString = null;
        order = null;
        secondString = null;
        bill = 0;
        imagesCount = 0;
        dish = null;
        menu = null;
    }

    public Message(MessageType messageType, Order order) {
        this.messageType = messageType;
        this.order = order;
        firstString = null;
        secondString = null;
        bill = 0;
        imagesCount = 0;
        dish = null;
        menu = null;
    }

    public Message(MessageType messageType, String firstString) {
        this.messageType = messageType;
        this.firstString = firstString;
        order = null;
        secondString = null;
        bill = 0;
        imagesCount = 0;
        dish = null;
        menu = null;
    }

    public Message(MessageType messageType, int imagesCount) {
        this.messageType = messageType;
        this.imagesCount = imagesCount;
        firstString = null;
        order = null;
        secondString = null;
        bill = 0;
        dish = null;
        menu = null;
    }

    public Message(MessageType messageType, Dish dish) {
        this.messageType = messageType;
        this.dish = dish;
        this.firstString = null;
        this.order = null;
        this.secondString = null;
        this.bill = 0;
        this.imagesCount = 0;
        menu = null;
    }

    public Message(MessageType messageType, Menu menu) {
        this.messageType = messageType;
        this.menu = menu;
        this.dish = null;
        this.firstString = null;
        this.order = null;
        this.secondString = null;
        this.bill = 0;
        this.imagesCount = 0;
    }

    public Message(MessageType messageType, String firstString, double bill) {
        this.messageType = messageType;
        this.firstString = firstString;
        this.bill = bill;
        order = null;
        secondString = null;
        imagesCount = 0;
        dish = null;
        menu = null;
    }

    public Message(MessageType messageType, String firstString, String secondString) {
        this.messageType = messageType;
        this.firstString = firstString;
        this.secondString = secondString;
        order = null;
        bill = 0;
        imagesCount = 0;
        dish = null;
        menu = null;
    }

    public Message(MessageType messageType, Order order, String name) {
        this.messageType = messageType;
        this.order = order;
        firstString = null;
        secondString = name;
        bill = 0;
        imagesCount = 0;
        dish = null;
        menu = null;
    }


    public MessageType getMessageType() {
        return messageType;
    }

    public String getFirstString() {
        return firstString;
    }

    public String getSecondString() {
        return secondString;
    }

    public Order getOrder() {
        return order;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    public Dish getDish() {
        return dish;
    }

    public double getBill() {
        return bill;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", firstString='" + firstString + '\'' +
                ", order=" + order +
                ", secondString='" + secondString + '\'' +
                ", bill=" + bill +
                ", imagesCount=" + imagesCount +
                ", dish=" + dish +
                '}';
    }
}

