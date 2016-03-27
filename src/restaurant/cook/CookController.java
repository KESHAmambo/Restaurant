package restaurant.cook;

import restaurant.Actor;
import restaurant.Message;
import restaurant.MessageType;
import restaurant.cook.view.CookView;
import restaurant.kitchen.Order;

import java.io.IOException;

/**
 * Created by Аркадий on 14.03.2016.
 */
public class CookController extends Actor {
    private CookModel model = new CookModel();
    private CookView view = new CookView(this, model);

    public static void main(String[] args) {
        CookController cookController = new CookController();
        cookController.view.initView();
        cookController.run();
    }

    @Override
    protected void actorHandshake() throws IOException, ClassNotFoundException {
        shake(MessageType.COOK_CONNECTION);
    }

    @Override
    protected void actorMainLoop() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                Message receivedMessage = connection.receive();
                if(receivedMessage.getMessageType() == MessageType.ORDER) {
                    Order order = receivedMessage.getOrder();
                    informAboutNewOrder(order);
                } else if(receivedMessage.getMessageType() == MessageType.PING) {
                    //do nothing
                } else {
                    throw new IOException("Unexpected message!");
                }
            }
        } finally {
            connection.close();
        }
    }

    private void informAboutNewOrder(Order order) {
        model.setCurrentOrder(order);
        view.refreshCurrentOrder();
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
        return view.askName();
    }

    @Override
    protected void notifyConnectionStatusChanged(boolean actorConnected) {
        view.notifyConnectionStatusChanged(actorConnected);
    }
}
