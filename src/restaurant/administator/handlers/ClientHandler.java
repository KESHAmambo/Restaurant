package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administator.Connection;
import restaurant.administator.Server;
import restaurant.kitchen.Order;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class ClientHandler extends Handler {
    Map<String, Connection> clientNameToConnectionLinks = Server.getClientNameToConnectionLinks();
    BlockingQueue<Order> waitingOrders = Server.getWaitingOrders();
    private BlockingQueue<Connection> waiters = Server.getWaiters();

    private Connection waiter;
    private String currentName;
    private boolean hasCurrentClient = false;

    public ClientHandler(Connection connection, String clientName) {
        super(connection, clientName);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!hasCurrentClient) {
                    waitForNewClient();
                } else {
                    workWithCurrentClient();
                }
            }
        } catch (IOException | InterruptedException | ClassNotFoundException ignore) {
        } finally {
            Server.showWarningMessage("Client " + actorName + " was disconnected!");
            Server.getActorNames().remove(actorName);
            try {
                connection.close();
            } catch (IOException ignore) {}
        }
    }

    private void workWithCurrentClient() throws IOException, ClassNotFoundException, InterruptedException {
        Message message = connection.receive();
        switch (message.getMessageType()) {
            case ORDER:
                Order order = message.getOrder();
                if (order != null) {
                    waitingOrders.put(order);
                    Server.addOrderToClientStatisticsBase(order, actorName);
                }
                break;
            case TEXT:
                waiter.send(message);
                break;
            case END_MEAL:
                hasCurrentClient = false;
                clientNameToConnectionLinks.remove(currentName);
                waiter.send(message);
                break;
        }
    }

    private void waitForNewClient() throws IOException, ClassNotFoundException, InterruptedException {
        Message newClientMessage = connection.receive();
        if (newClientMessage.getMessageType() == MessageType.NEW_CLIENT) {
            hasCurrentClient = true;
            currentName = newClientMessage.getText();
            waiter = waiters.take();
            waiters.put(waiter);
            waiter.send(newClientMessage);
            clientNameToConnectionLinks.put(currentName, connection);
        }
    }
}
