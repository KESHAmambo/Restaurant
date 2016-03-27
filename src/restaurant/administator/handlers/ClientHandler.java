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
    private Map<String, Connection> clientsLinksFromNameToConnection =
            Server.getClientsLinksFromNameToConnection();
    private Map<String, Connection> waitersLinksFromNameToConnection =
            Server.getWaitersLinksFromNameToConnection();
    private BlockingQueue<Order> waitingOrders = Server.getWaitingOrders();
    private BlockingQueue<String> waiters = Server.getWaiters();

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
            handlerMainLoop();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            informServerAndCloseConnection("Client");
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
                    order.setWaiter(waiterName);
                    waitingOrders.put(order);
                    Server.addOrderToClientsStatisticsBase(order, actorName);
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
