package restaurant.administrator.controller.handlers;

import restaurant.common.Message;
import restaurant.common.Connection;

import java.io.IOException;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class HandlerFactory {
    private HandlerFactory() {}

    public static Handler byMessage(Message message, Connection connection) throws IOException {
        switch(message.getMessageType()) {
            case COOK_CONNECTION:
                return new CookHandler(connection);
            case WAITER_CONNECTION:
                return new WaiterHandler(connection);
            case CLIENT_CONNECTION:
                return new ClientHandler(connection);
            default:
                throw new IOException("Unexpected connection type: " + message.getMessageType());
        }
    }

}
