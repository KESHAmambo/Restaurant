package restaurant.administator.handlers;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.administator.Connection;
import restaurant.administator.Server;

import java.io.IOException;
import java.util.List;

/**
 * Created by Аркадий on 13.03.2016.
 */
public abstract class Handler implements Runnable {
    private static List<String> actorsNames = Server.getActorsNames();
    protected final Connection connection;
    protected String actorName;
    public Handler(Connection connection) {
        this.connection = connection;
    }

    protected void requestActorName() throws IOException, ClassNotFoundException {
        while (true) {
            connection.send(new Message(MessageType.NAME_REQUEST));
            Message actorNameMessage = connection.receive();
            if(actorNameMessage.getMessageType() == MessageType.ACTOR_NAME) {
                String name = actorNameMessage.getText();
                if(name != null && !name.isEmpty() && !actorsNames.contains(name)) {
                    actorName = name;
                    actorsNames.add(actorName);
                    connection.send(new Message(MessageType.NAME_ACCEPTED));
                    break;
                }
            }
        }
    }
}
