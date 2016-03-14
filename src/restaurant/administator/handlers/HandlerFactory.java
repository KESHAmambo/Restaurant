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
    private static List<String> actorsNames = Server.getActorsNames();

    private HandlerFactory() {
    }

    public static Handler newHandler(Message message, Connection connection) {
        String actorName = message.getText();
        boolean isAdded = checkNameAndAdd(actorName);
        if(isAdded) {
            switch (message.getMessageType()) {
                case COOK_CONNECTION:
                    return new CookHandler(connection, actorName);
                case WAITER_CONNECTION:
                    waiters.add(connection);
                    return new WaiterHandler(connection, actorName);
                case CLIENT_CONNECTION:
                    return new ClientHandler(connection, actorName);
            }
        }
        return null;
    }

    private static boolean checkNameAndAdd(String actorName) {
        if(actorName != null && !actorsNames.contains(actorName)) {
            actorsNames.add(actorName);
            return true;
        } else {
            return false;
        }
    }
}
