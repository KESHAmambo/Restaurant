package restaurant.cook;

import restaurant.Actor;
import restaurant.Message;
import restaurant.MessageType;
import restaurant.kitchen.Order;

import java.io.IOException;

/**
 * Created by Аркадий on 14.03.2016.
 */
public class CookController extends Actor {
    private CookView view = new CookView(this);
    private CookModel model = new CookModel();

    public static void main(String[] args) {
        CookController cookController = new CookController();
        cookController.run();
    }

    public CookModel getModel() {
        return model;
    }

    @Override
    protected void clientMainLoop() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                Message receivedMessage = connection.receive();
                if(receivedMessage.getMessageType() == MessageType.ORDER) {
                    Order order = receivedMessage.getOrder();
                    informAboutNewOrder(order);
                } else {
                    throw new IOException("Unexpected message!");
                }
            }
        } finally {
            connection.close();
        }
    }

    void sendMessage(Message message) throws IOException {
        connection.send(message);
    }

    private void informAboutNewOrder(Order order) {
        model.setOrder(order);
        view.refreshOrder();
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
        return view.askName();
    }

    @Override
    protected void notifyConnectionStatusChanged(boolean connectionStatus) {
        view.notifyConnectionStatusChanged(connectionStatus);
    }
}
