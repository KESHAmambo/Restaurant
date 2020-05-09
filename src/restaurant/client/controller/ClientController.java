package restaurant.client.controller;

import restaurant.client.model.ClientModel;
import restaurant.common.*;
import restaurant.client.view.ClientView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientController extends ActorController {
    private final String DOWNLOAD_IMAGES_PATH = "src/restaurant/downloadedimages/";

    private ClientModel model = new ClientModel();
    private ClientView view = new ClientView(this, model);

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
        clientController.view.initView();
        clientController.run();
    }

    @Override
    protected void handshakeServer()
            throws IOException, ClassNotFoundException, UnexpectedMessageException {
        sendConnectionTypeAndActorName(MessageType.CLIENT_CONNECTION);
    }

    @Override
    protected void actorMainLoop()
            throws IOException, ClassNotFoundException, UnexpectedMessageException {
        receiveMenu();
        view.updateMenuPanels();
        askNewClientName();
        while(true) {
            Message message = connection.receive();
            if(message.getMessageType() == MessageType.TEXT) {
                informAboutNewText(message.getSecondString());
            } else {
                throw new UnexpectedMessageException(message);
            }
        }
    }

    private void receiveMenu()
            throws IOException, ClassNotFoundException, UnexpectedMessageException {
        Message message = connection.receive();
        if(message.getMessageType() == MessageType.MENU) {
            Map<String, String> needDownloadPaths = new HashMap<>();
            Menu receivedMenu = message.getMenu();
            Menu menu = model.getMenu();

            for(List<Dish> dishes: receivedMenu.getStore().values()) {
                for(Dish dish: dishes) {
                    Dish removedDish = menu.removeDish(dish);

                    String serverImagePath = dish.getServerImagePath();
                    String clientImagePath = createClientImagePath(serverImagePath);

                    dish.setClientImagePath(clientImagePath);
                    menu.add(dish);

                    addToNeedDownloadPathsIfNecessary(
                            needDownloadPaths, removedDish,
                            serverImagePath, clientImagePath);
                }
            }

            receiveImages(needDownloadPaths);
            model.serializeMenu();
        } else {
            throw new UnexpectedMessageException(message);
        }
    }

    private String createClientImagePath(String receivedImagePath) {
        int indexSubstringFrom = Math.max(
                receivedImagePath.lastIndexOf('/'), receivedImagePath.lastIndexOf('\\')) + 1;
        String imageName = receivedImagePath.substring(indexSubstringFrom);
        return DOWNLOAD_IMAGES_PATH + imageName;
    }

    private void addToNeedDownloadPathsIfNecessary(
            Map<String, String> needDownloadPaths, Dish removedDish,
            String serverImagePath, String clientImagePath) {
        if(removedDish != null) {
            String oldServerImagePath = removedDish.getServerImagePath();
            if(!oldServerImagePath.equals(serverImagePath)) {
                needDownloadPaths.put(serverImagePath, clientImagePath);
            }
        } else {
            needDownloadPaths.put(serverImagePath, clientImagePath);
        }
    }

    private void receiveImages(Map<String, String> needDownloadPaths) throws IOException {
        connection.send(new Message(
                MessageType.IMAGES_COUNT, needDownloadPaths.size()));

        for(Map.Entry<String, String> pair: needDownloadPaths.entrySet()) {
            connection.send(new Message(
                    MessageType.IMAGE_REQEUST, pair.getKey()));
            connection.receiveImage(pair.getValue());
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
