package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administator.Connection;
import restaurant.administator.Server;
import restaurant.kitchen.Order;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class CookHandler extends Handler {
    private final BlockingQueue<Order> waitingOrders = Server.getWaitingOrders();

    private boolean cookingOrder = false;

    public CookHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        cookingOrder = false;
        try {
            requestActorName();
            while(true) {
                if(!cookingOrder) {
                    waitForOrder();
                } else {
                    waitForCooking();
                }
            }
        } catch (IOException | InterruptedException | ClassNotFoundException ignore) {
        } finally {
            Server.showWarningMessage("Cook " + actorName + " was disconnected!");
            Server.getActorsNames().remove(actorName);
            try {
                connection.close();
            } catch (IOException ignore) {}
        }
    }

    private void waitForCooking() throws IOException, InterruptedException, ClassNotFoundException {
        Message message = connection.receive();
        if(message.getMessageType() == MessageType.ORDER_IS_READY) {
            Order order = message.getOrder();
            if(order != null) {
                order.setCook(actorName);
                order.getWaiter().send(message);
                cookingOrder = false;
                Server.addOrderToCooksStatisticsBase(order, actorName);
            }
        }
    }

    private void waitForOrder() throws InterruptedException, IOException {
        Order order = waitingOrders.take();
        Message message = new Message(MessageType.ORDER, order);
        try {
            connection.send(message);
            cookingOrder = true;
        } catch (IOException e) {
            if(order != null) {
                waitingOrders.put(order);
            }
            throw e;
        }
    }
}
