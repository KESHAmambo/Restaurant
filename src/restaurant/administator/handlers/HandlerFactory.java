package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.administator.Connection;
import restaurant.administator.Server;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class HandlerFactory {
    private static BlockingQueue<Connection> waiters = Server.getWaiters();
    private static List<String> actorNames = Server.getActorNames();

    private HandlerFactory() {
    }

    public static Handler newHandler(Message message, Connection connection) {
        String actorName = message.getText();
        switch(message.getMessageType()) {
            case COOK_CONNECTION:
                actorNames.add(actorName);
                return new CookHandler(connection, actorName);
            case WAITER_CONNECTION:
                actorNames.add(actorName);
                waiters.add(connection);
                return new WaiterHandler(connection, actorName);
            case CLIENT_CONNECTION:
                actorNames.add(actorName);
                return new ClientHandler(connection, actorName);
            default:
                return null;
        }
    }
}
