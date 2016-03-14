package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administator.Connection;
import restaurant.administator.Server;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class WaiterHandler extends Handler {
    Map<String, Connection> clientsNameToConnectionLinks = Server.getClientsNameToConnectionLinks();

    public WaiterHandler(Connection connection, String waiterName) {
        super(connection, waiterName);
    }

    @Override
    public void run() {
        try {
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
        } catch (IOException | ClassNotFoundException ignore) {
        } finally {
            Server.showWarningMessage("Waiter " + actorName + " was disconnected!");
            Server.getActorsNames().remove(actorName);
            try {
                connection.close();
            } catch (IOException ignore) {}
        }
    }
}
