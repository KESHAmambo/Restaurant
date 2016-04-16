package restaurant.client;

import restaurant.Actor;
import restaurant.client.view.ClientView;
import restaurant.Message;
import restaurant.MessageType;
import restaurant.kitchen.Dish;

import java.io.IOException;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientController extends Actor {
    private ClientModel model = new ClientModel(this);
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
        receiveMenuChanges();
        view.updateMenuPanels();
        askNewClientName();
        while(true) {
            Message message = connection.receive();
            if(message.getMessageType() == MessageType.TEXT) {
                informAboutNewText(message.getText());
            }
        }
    }

    private void receiveMenuChanges() throws IOException, ClassNotFoundException {
        Message message = connection.receive();
        if(message.getMessageType() == MessageType.DISHES_NUMBER) {
            int numberOfDishes = message.getNumberOfDishes();
            for(int i = 0; i < numberOfDishes; i++) {
                message = connection.receive();
                processDishMessage(message);
            }
        } else {
            throw new IOException("Unexpected message");
        }
        model.serializeMenu();
    }

    private void processDishMessage(Message message) throws IOException {
        switch(message.getMessageType()) {
            case STATUS_CHANGED_DISH:
                changeDishStatus(message.getDish());
                break;
            case DISH_WITHOUT_IMAGE:
                processAddedOrEditedDish(message.getDish(), false);
                break;
            case DISH_WITH_IMAGE:
                processAddedOrEditedDish(message.getDish(), true);
                break;
            default:
                throw new IOException("Unexpected message");
        }
    }

    private void processAddedOrEditedDish(Dish dish, boolean needImage) throws IOException {
        model.processAddedOrEditedDish(dish, needImage);
    }

    public void downloadImage(String imageName) throws IOException {
        connection.downloadImage(imageName);
    }

    private void changeDishStatus(Dish dish) {
        model.changeDishStatus(dish);
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
