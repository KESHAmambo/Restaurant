package restaurant.administrator.handlers;

import restaurant.Message;
import restaurant.administrator.Connection;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class HandlerFactory {
    private HandlerFactory() {}

    public static Handler byMessage(Message message, Connection connection) {
        switch(message.getMessageType()) {
            case COOK_CONNECTION:
                return new CookHandler(connection);
            case WAITER_CONNECTION:
                return new WaiterHandler(connection);
            case CLIENT_CONNECTION:
                return new ClientHandler(connection);
            default:
                return null;
        }
    }

}
