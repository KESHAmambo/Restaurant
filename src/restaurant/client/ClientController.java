package restaurant.client;

import restaurant.Actor;
import restaurant.client.view.ClientView;
import restaurant.Message;
import restaurant.MessageType;

import java.io.IOException;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientController extends Actor {
    private ClientModel model = new ClientModel();
    private ClientView view = new ClientView(this, model);

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
        clientController.view.initView();
        clientController.run();
    }

    @Override
    protected void actorHandshake() throws IOException, ClassNotFoundException {
        shake(MessageType.CLIENT_CONNECTION);
    }

    @Override
    protected void actorMainLoop() throws IOException, ClassNotFoundException {
        askNewClientName();
        while(true) {
            Message message = connection.receive();
            if(message.getMessageType() == MessageType.TEXT) {
                informAboutNewText(message.getText());
            }
        }
    }

    private void askNewClientName() {
        view.askNewClientName();
    }

    private void informAboutNewText(String text) {
        view.informAboutNewText(text);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            connection.send(message);
        } catch (IOException e) {
            view.notifyConnectionStatusChanged(false);
        }
    }

    @Override
    protected int askServerPort() {
        return view.askServerPort();
    }

    @Override
    protected String askServerAddress() {
        return view.askServerAddress();
    }

    @Override
    protected String askName() {
        return view.askTableNumber();
    }

    @Override
    protected void notifyConnectionStatusChanged(boolean actorConnected) {
        view.notifyConnectionStatusChanged(actorConnected);
    }
}
