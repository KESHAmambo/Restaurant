package restaurant.waiter;

import restaurant.Actor;
import restaurant.Message;
import restaurant.kitchen.Order;
import restaurant.waiter.view.WaiterView;

import java.io.IOException;

/**
 * Created by Anatoly on 18.03.2016.
 */
public class WaiterController extends Actor {
    WaiterModel model = new WaiterModel();
    WaiterView view = new WaiterView(this, model);

    public static void main(String[] args) {
        WaiterController waiterController = new WaiterController();
        waiterController.view.initView();
        waiterController.run();
    }

    @Override
    protected void actorMainLoop() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                Message receivedMessage = connection.receive();
                switch(receivedMessage.getMessageType()) {
                    case NEW_CLIENT:
                        informAboutNewClient(receivedMessage.getClientName(), receivedMessage.getTableNumber());
                        break;
                    case ORDER_IS_READY:
                        informAboutReadyOrder(receivedMessage.getOrder());
                        break;
                    case TEXT:
                        informAboutNewText(receivedMessage.getClientName(), receivedMessage.getText());
                        break;
                    case END_MEAL:
                        informAboutEndMeal(receivedMessage.getClientName());
                }
            }
        } finally {
            connection.close();
        }
    }

    private void informAboutEndMeal(String clientName) {
        view.informAboutEndMeal(clientName);
        model.deleteClient(clientName);
    }

    private void informAboutNewText(String clientName, String text) {
        view.informAboutNewText(clientName, text);
    }

    private void informAboutReadyOrder(Order order) {
        model.addOrderToClientBill(order);
        view.informAboutReadyOrder(order);
    }

    private void informAboutNewClient(String clientName, int tableNumber) {
        WaiterModel.Client newClient = model.addNewClient(clientName, tableNumber);
        view.addNewClientDialog(newClient);
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
