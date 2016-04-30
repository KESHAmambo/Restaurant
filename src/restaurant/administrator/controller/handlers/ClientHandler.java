package restaurant.administrator.controller.handlers;

import restaurant.administrator.controller.Server;
import restaurant.common.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
class ClientHandler extends Handler {
    private Map<String, Connection> clientsLinksFromNameToConnection =
            Server.getClientsLinksFromNameToConnection();
    private Map<String, Connection> waitersLinksFromNameToConnection =
            Server.getWaitersLinksFromNameToConnection();
    private BlockingQueue<Order> waitingOrders = Server.getWaitingOrders();
    private BlockingQueue<String> waiters = Server.getWaiters();
    private Menu menu = Server.getMenu();

    private String waiterName;
    private Connection waiterConnection;
    private String currentName;
    private boolean hasCurrentClient = false;

    ClientHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        try {
            requestActorName();
            Server.updateConnectionsInfo("Table " + actorName + " was connected.");
            sendMenu();
            sendImages();
            handlerMainLoop();
        } catch (IOException |
                InterruptedException |
                ClassNotFoundException |
                UnexpectedMessageException e) {
            e.printStackTrace();
        } finally {
            informServerAndCloseConnection("Client");
        }
    }

    private void sendMenu() throws IOException {
        connection.send(new Message(MessageType.MENU, menu));
    }

    private void sendImages()
            throws IOException, ClassNotFoundException, UnexpectedMessageException {
        Message message = connection.receive();
        if(message.getMessageType() == MessageType.IMAGES_COUNT) {
            int imagesCount = message.getImagesCount();
            sendImages(imagesCount);
        } else {
            throw new UnexpectedMessageException(message);
        }
    }

    private void sendImages(int imagesCount)
            throws IOException, ClassNotFoundException, UnexpectedMessageException {
        for(int i = 0; i < imagesCount; i++) {
            Message message = connection.receive();
            if(message.getMessageType() == MessageType.IMAGE_REQEUST) {
                String serverImagePath = message.getFirstString();
                connection.sendImage(serverImagePath);
            } else {
                throw new UnexpectedMessageException(message);
            }
        }
    }

    @Override
    protected void handlerMainLoop()
            throws IOException, ClassNotFoundException,
            InterruptedException, UnexpectedMessageException {
        while (true) {
            if (!hasCurrentClient) {
                waitForNewClient();
            } else {
                workWithCurrentClient();
            }
        }
    }

    private void workWithCurrentClient()
            throws IOException, ClassNotFoundException,
            InterruptedException, UnexpectedMessageException {
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
            default:
                throw new UnexpectedMessageException(message);
        }
    }

    /**
     * If waiter was disconnected, IOException throws
     * while trying to send him a message.
     * So we should set new waiter and waiterConnection for
     * current client and inform that waiter, that this
     * client is now in his area of responsibility.
     */
    private void setNewWaiterAndSend(Message message, boolean informAboutNewClient)
            throws InterruptedException {
        try {
            setNewWaiter();
            if (informAboutNewClient) {
                waiterConnection.send(new Message(
                        MessageType.NEW_CLIENT, message.getFirstString()));
            }
            waiterConnection.send(message);
        } catch (IOException e) {
            setNewWaiterAndSend(message, informAboutNewClient);
        }
    }

    private void waitForNewClient()
            throws IOException, ClassNotFoundException, InterruptedException {
        Message newClientMessage = connection.receive();
        if (newClientMessage.getMessageType() == MessageType.NEW_CLIENT) {
            hasCurrentClient = true;
            currentName = newClientMessage.getFirstString();
            clientsLinksFromNameToConnection.put(currentName, connection);

            setNewWaiterAndSend(newClientMessage, false);
        }
    }

    private void setNewWaiter()
            throws InterruptedException {
        waiterName = waiters.take();
        waiters.put(waiterName);
        waiterConnection = waitersLinksFromNameToConnection.get(waiterName);
    }
}
