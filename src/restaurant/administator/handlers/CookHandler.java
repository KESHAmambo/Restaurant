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

    private boolean isCookingOrder = false;

    public CookHandler(Connection connection, String cookName) {
        super(connection, cookName);
    }

    @Override
    public void run() {
        isCookingOrder = false;
        try {
            while(true) {
                if(!isCookingOrder) {
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
                order.getWaiter().send(message);
                isCookingOrder = false;
                Server.addOrderToCooksStatisticsBase(order, actorName);
            }
        }
    }

    private void waitForOrder() throws InterruptedException, IOException {
        Order order = waitingOrders.take();
        Message message = new Message(MessageType.ORDER, order);
        try {
            connection.send(message);
            isCookingOrder = true;
        } catch (IOException e) {
            if(order != null) {
                waitingOrders.put(order);
            }
            throw e;
        }
    }
}
