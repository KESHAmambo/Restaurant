package restaurant.administrator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administrator.Connection;
import restaurant.administrator.Server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class WaiterHandler extends Handler {
    private Map<String, Connection> waitersLinksFromNameToConnection =
            Server.getWaitersLinksFromNameToConnection();
    private Map<String, Connection> clientsLinksFromNameToConnection =
            Server.getClientsLinksFromNameToConnection();
    private BlockingQueue<String> waiters = Server.getWaiters();

    public WaiterHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        try {
            requestActorName();
            Server.updateConnectionsInfo("Waiter " + actorName + " was connected.");
            waiters.add(actorName);
            waitersLinksFromNameToConnection.put(actorName, connection);
            handlerMainLoop();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            waiters.remove(actorName);
            waitersLinksFromNameToConnection.remove(actorName);
            informServerAndCloseConnection("Waiter");
        }
    }

    @Override
    protected void handlerMainLoop() throws IOException, ClassNotFoundException {
        while(true) {
            Message message = connection.receive();
            String clientName = message.getClientName();
            if(message.getMessageType() == MessageType.TEXT && clientName != null) {
                Connection clientConnection = clientsLinksFromNameToConnection.get(clientName);
                if (clientConnection != null) {
                    clientConnection.send(message);
                }
            }
        }
    }
}
