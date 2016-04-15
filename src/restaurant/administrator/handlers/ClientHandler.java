package restaurant.administrator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administrator.Connection;
import restaurant.administrator.Server;
import restaurant.kitchen.Dish;
import restaurant.kitchen.Order;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class ClientHandler extends Handler {
    private Map<String, Connection> clientsLinksFromNameToConnection =
            Server.getClientsLinksFromNameToConnection();
    private Map<String, Connection> waitersLinksFromNameToConnection =
            Server.getWaitersLinksFromNameToConnection();
    private BlockingQueue<Order> waitingOrders = Server.getWaitingOrders();
    private BlockingQueue<String> waiters = Server.getWaiters();

    private List<Dish> needImageDishes = Server.getNeedImageDishes();
    private List<Dish> notNeedImageDishes = Server.getNotNeedImageDishes();
    private List<Dish> statusChangedDishes = Server.getStatusChangedDishes();

    private String waiterName;
    private Connection waiterConnection;
    private String currentName;
    private boolean hasCurrentClient = false;

    public ClientHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        try {
            requestActorName();
            Server.updateConnectionsInfo("Table " + actorName + " was connected.");
            sendMenuChanges();
            handlerMainLoop();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            informServerAndCloseConnection("Client");
        }
    }

    private void sendMenuChanges() throws IOException {
        sendNumberOfDishes();
        sendStatusChangedDishes();
        sendNotNeedImageDishes();
        sendNeedImageDishes();
    }

    private void sendNumberOfDishes() throws IOException {
        int numberOfDishes = statusChangedDishes.size() +
                notNeedImageDishes.size() + needImageDishes.size();
        connection.send(new Message(MessageType.DISHES_NUMBER, numberOfDishes));
    }

    private void sendStatusChangedDishes() throws IOException {
        for(Dish dish: statusChangedDishes) {
            connection.send(new Message(MessageType.STATUS_CHANGED_DISH, dish));
        }
    }

    private void sendNotNeedImageDishes() throws IOException {
        for(Dish dish: notNeedImageDishes) {
            connection.send(new Message(MessageType.DISH_WITHOUT_IMAGE, dish));
        }
    }

    private void sendNeedImageDishes() throws IOException {
        for(Dish dish: needImageDishes) {
            connection.send(new Message(MessageType.DISH_WITH_IMAGE, dish));
            connection.sendImage(dish.getImagePath());
        }
    }

    @Override
    protected void handlerMainLoop() throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            if (!hasCurrentClient) {
                waitForNewClient();
            } else {
                workWithCurrentClient();
            }
        }
    }

    private void workWithCurrentClient()
            throws IOException, ClassNotFoundException, InterruptedException {
        Message message = connection.receive();
        switch (message.getMessageType()) {
            case ORDER:
                Order order = message.getOrder();
                if (order != null) {
                    order.setReceivedTime(new Date());
                    order.setWaiterName(waiterName);
                    order.setTableNumber(Integer.parseInt(actorName));
                    waitingOrders.put(order);
                }
                break;
            case TEXT:
                try {
                    waiterConnection.send(message);
                } catch (IOException e) {
                    setNewWaiterAndSend(message, true);
                }
                break;
            case END_MEAL:
                hasCurrentClient = false;
                clientsLinksFromNameToConnection.remove(currentName);
                try {
                    waiterConnection.send(message);
                } catch (IOException e) {
                    setNewWaiterAndSend(message, true);
                }
                break;
        }
    }

    /**
     * If waiter was disconnected, then IOException throws
     * while trying to send him a message.
     * So we should set new waiter and waiterConnection for
     * current client and inform that waiter, that this
     * client is now in his area of responsibility.
     */
    private void setNewWaiterAndSend(Message message, boolean informAboutNewClient) throws InterruptedException {
        try {
            setNewWaiter();
            if (informAboutNewClient) {
                waiterConnection.send(new Message(
                        MessageType.NEW_CLIENT, message.getClientName()));
            }
            waiterConnection.send(message);
        } catch (IOException e) {
            setNewWaiterAndSend(message, informAboutNewClient);
        }
    }

    private void waitForNewClient() throws IOException, ClassNotFoundException, InterruptedException {
        Message newClientMessage = connection.receive();
        if (newClientMessage.getMessageType() == MessageType.NEW_CLIENT) {
            hasCurrentClient = true;
            currentName = newClientMessage.getClientName();
            clientsLinksFromNameToConnection.put(currentName, connection);

            setNewWaiterAndSend(newClientMessage, false);
        }
    }

    private void setNewWaiter() throws InterruptedException {
        waiterName = waiters.take();
        waiters.put(waiterName);
        waiterConnection = waitersLinksFromNameToConnection.get(waiterName);
    }
}
