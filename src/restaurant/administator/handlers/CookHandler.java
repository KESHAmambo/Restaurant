package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administator.Connection;
import restaurant.administator.Server;
import restaurant.kitchen.Order;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class CookHandler extends Handler {
    private BlockingQueue<Order> waitingOrders = Server.getWaitingOrders();
    private BlockingQueue<String> waiters = Server.getWaiters();
    private Map<String, Connection> waitersLinksFromNameToConnection =
            Server.getWaitersLinksFromNameToConnection();

    private boolean cookingOrder = false;

    public CookHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        cookingOrder = false;
        try {
            requestActorName();
            handlerMainLoop();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            informServerAndCloseConnection("Cook");
        }
    }

    @Override
    protected void handlerMainLoop()
            throws InterruptedException, IOException, ClassNotFoundException {
        while(true) {
            if(!cookingOrder) {
                waitForOrder();
            } else {
                waitForCooking();
            }
        }
    }

    private void waitForCooking() throws IOException, InterruptedException, ClassNotFoundException {
        Message message = connection.receive();
        if(message.getMessageType() == MessageType.ORDER_IS_READY) {
            Order order = message.getOrder();
            if(order != null) {
                order.setCook(actorName);
                try {
                    String waiterName = order.getWaiter();
                    Connection waiterConnection = waitersLinksFromNameToConnection.get(waiterName);
                    waiterConnection.send(message);
                } catch (IOException | NullPointerException e) {
                    sendToAnotherWaiter(message);
                    e.printStackTrace();
                }
                cookingOrder = false;
                Server.addOrderToCooksStatisticsBase(order, actorName);
            }
        }
    }

    private void sendToAnotherWaiter(Message message) throws InterruptedException {
        while (true) {
            try {
                String waiterName = waiters.take();
                waiters.put(waiterName);
                Connection waiterConnection = waitersLinksFromNameToConnection.get(waiterName);
                waiterConnection.send(message);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("RESENT TO ANOTHER WAITER");
    }

    private void waitForOrder() throws InterruptedException, IOException {
        Order order;
        while(true) {
            order = waitingOrders.poll(3, TimeUnit.SECONDS);
            if(order != null) {
                break;
            } else {
                connection.send(new Message(MessageType.PING));
            }
        }

        Message message = new Message(MessageType.ORDER, order);
        try {
            connection.send(message);
            cookingOrder = true;
        } catch (IOException e) {
            waitingOrders.put(order);
            throw e;
        }
    }
}
