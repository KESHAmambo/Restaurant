package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administator.Connection;
import restaurant.administator.Server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class WaiterHandler extends Handler {
    Map<String, Connection> clientsNameToConnectionLinks = Server.getClientsNameToConnectionLinks();
    private static BlockingQueue<Connection> waiters = Server.getWaiters();

    public WaiterHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void run() {
        try {
            requestActorName();
            waiters.add(connection);
            resendTextsToClients();
        } catch (IOException ignore) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Server.showWarningMessage("Waiter " + actorName + " was disconnected!");
            Server.getActorsNames().remove(actorName);
            try {
                connection.close();
            } catch (IOException ignore) {}
        }
    }

    private void resendTextsToClients() throws IOException, ClassNotFoundException {
        while(true) {
            Message message = connection.receive();
            String clientName = message.getClientName();
            if(message.getMessageType() == MessageType.TEXT && clientName != null) {
                Connection clientConnection = clientsNameToConnectionLinks.get(clientName);
                if (clientConnection != null) {
                    clientConnection.send(message);
                }
            }
        }
    }
}
