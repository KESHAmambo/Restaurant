package restaurant.administrator.controller.handlers;

import restaurant.administrator.controller.Server;
import restaurant.common.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Аркадий on 13.03.2016.
 */
class CookHandler extends Handler {
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
            Server.updateConnectionsInfo("Cook " + actorName + " was connected.");
            handlerMainLoop();
        } catch (IOException |
                InterruptedException |
                ClassNotFoundException |
                UnexpectedMessageException e) {
            e.printStackTrace();
        } finally {
            informServerAndCloseConnection("Cook");
        }
    }

    @Override
    protected void handlerMainLoop()
            throws InterruptedException, IOException,
            ClassNotFoundException, UnexpectedMessageException {
        while(true) {
            if(!cookingOrder) {
                waitForOrder();
            } else {
                waitForCooking();
            }
        }
    }

    private void waitForCooking()
            throws IOException, InterruptedException,
            ClassNotFoundException, UnexpectedMessageException {
        Message message = connection.receive();
        if(message.getMessageType() == MessageType.ORDER_IS_READY) {
            Order order = message.getOrder();
            if(order != null) {
                processReadyOrder(message, order);
            }
        } else {
            throw new UnexpectedMessageException(message);
        }
    }

    private void processReadyOrder(Message message, Order order)
            throws InterruptedException {
        try {
            order.setReadyTime(new Date());
            order.setCookName(actorName);
            String waiterName = order.getWaiterName();
            Connection waiterConnection = waitersLinksFromNameToConnection.get(waiterName);
            waiterConnection.send(message);

            cookingOrder = false;
            Server.writeOrderToDatabase(order);
        } catch (IOException | NullPointerException e) {
            sendToAnotherWaiter(message, order);
            e.printStackTrace();
        }
    }

    private void sendToAnotherWaiter(Message message, Order order)
            throws InterruptedException {
        while (true) {
            try {
                String waiterName = waiters.take();
                waiters.put(waiterName);
                Connection waiterConnection = waitersLinksFromNameToConnection.get(waiterName);
                waiterConnection.send(message);

                order.setWaiterName(waiterName);
                Server.writeOrderToDatabase(order);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForOrder()
            throws InterruptedException, IOException {
        Order order;
        while(true) {
            order = waitingOrders.poll(3, TimeUnit.SECONDS);
            if(order != null) {
                break;
            } else {
                connection.send(new Message(MessageType.PING));
            }
        }

        order.setStartedCookTime(new Date());
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
