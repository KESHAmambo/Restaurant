package restaurant;

import restaurant.administrator.Connection;

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
            actorHandshake();
            actorMainLoop();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            notifyConnectionStatusChanged(false);
        }
    }

    protected abstract void actorHandshake() throws  IOException, ClassNotFoundException;

    protected void shake(MessageType messageType) throws IOException, ClassNotFoundException {
        Message handshakeMessage = new Message(messageType);
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

    protected abstract void actorMainLoop() throws IOException, ClassNotFoundException;

    public abstract void sendMessage(Message message);

    protected abstract int askServerPort();

    protected abstract String askServerAddress();

    protected abstract String askName();

    protected abstract void notifyConnectionStatusChanged(boolean actorConnected);

}
