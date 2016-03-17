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

    private HandlerFactory() {}

    public static Handler byMessage(Message message, Connection connection) {
        switch(message.getMessageType()) {
            case COOK_CONNECTION:
                return new CookHandler(connection);
            case WAITER_CONNECTION:
                waiters.add(connection);
                return new WaiterHandler(connection);
            case CLIENT_CONNECTION:
                return new ClientHandler(connection);
            default:
                return null;
        }
    }

}
