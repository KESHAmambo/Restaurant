package restaurant;

import restaurant.administator.Connection;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Аркадий on 14.03.2016.
 */
public abstract class Actor {
    protected String actorName;
    protected Connection connection;
    protected volatile boolean actorConnected = false;

    protected void run() {
        try {
            String serverAddress = askServerAddress();
            int serverPort = askServerPort();
            Socket socket = new Socket(serverAddress, serverPort);
            connection = new Connection(socket);
            clientHandshake();
            clientMainLoop();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            notifyConnectionStatusChanged(false);
        }
    }

    protected void clientHandshake() throws IOException, ClassNotFoundException {
        Message handshakeMessage = new Message(MessageType.COOK_CONNECTION);
        connection.send(handshakeMessage);
        String name = null;
        while(true) {
            Message receiveMessage = connection.receive();
            switch(receiveMessage.getMessageType()) {
                case NAME_REQUEST:
                    name = askName();
                    connection.send(new Message(MessageType.ACTOR_NAME, name));
                    break;
                case NAME_ACCEPTED:
                    actorName = name;
                    notifyConnectionStatusChanged(true);
                    return;
                default:
                    throw new IOException("Unexpected MessageType");
            }
        }
    }

    protected abstract void clientMainLoop() throws IOException, ClassNotFoundException;

    public abstract void sendMessage(Message message);

    protected abstract int askServerPort();

    protected abstract String askServerAddress();

    protected abstract String askName();

    protected abstract void notifyConnectionStatusChanged(boolean actorConnected);

}
