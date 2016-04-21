package restaurant.administrator.controller.handlers;

import restaurant.administrator.controller.Server;
import restaurant.common.Message;
import restaurant.common.MessageType;
import restaurant.common.Connection;
import restaurant.common.UnexpectedMessageException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
class WaiterHandler extends Handler {
    private Map<String, Connection> waitersPairsNameAndConnection =
            Server.getWaitersLinksFromNameToConnection();
    private Map<String, Connection> clientsPairsNameAndConnection =
            Server.getClientsLinksFromNameToConnection();
    private BlockingQueue<String> waiters = Server.getWaiters();

    WaiterHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        try {
            requestActorName();
            Server.updateConnectionsInfo("Waiter " + actorName + " was connected.");
            waiters.add(actorName);
            waitersPairsNameAndConnection.put(actorName, connection);
            handlerMainLoop();
        } catch (IOException |
                ClassNotFoundException |
                UnexpectedMessageException e) {
            e.printStackTrace();
        } finally {
            waiters.remove(actorName);
            waitersPairsNameAndConnection.remove(actorName);
            informServerAndCloseConnection("Waiter");
        }
    }

    @Override
    protected void handlerMainLoop()
            throws IOException, ClassNotFoundException, UnexpectedMessageException {
        while(true) {
            Message message = connection.receive();
            String clientName = message.getFirstString();
            if(message.getMessageType() == MessageType.TEXT && clientName != null) {
                Connection clientConnection = clientsPairsNameAndConnection.get(clientName);
                if (clientConnection != null) {
                    clientConnection.send(message);
                }
            } else {
                throw new UnexpectedMessageException(message);
            }
        }
    }
}
