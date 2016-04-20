package restaurant.administrator.controller;

import restaurant.administrator.model.AdminModel;
import restaurant.administrator.view.AdminView;
import restaurant.kitchen.Order;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Аркадий on 31.03.2016.
 */
public class AdminController {
    private AdminModel model = new AdminModel();
    private AdminView view = new AdminView(this, model);

    public static void main(String[] args) {
        AdminController adminController = new AdminController();
        adminController.view.initView();
    }

    public void startServer() {
        model.serializeMenu();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        askLoginInfoAndStart();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    view.showErrorDialog("SERVER CRASHED!");
                    closeResourcesAndStopServer();
                }
            }

            private void askLoginInfoAndStart() throws IOException, ClassNotFoundException {
                try {
                    String address = view.askServerAddress();
                    int port = view.askServerPort();
                    Server.start(AdminController.this, address, port, model.getMenu());
                } catch(UnknownHostException e) {
                    view.showErrorDialog("Invalid server address!");
                } catch(IllegalArgumentException e) {
                    view.showErrorDialog("Invalid port!");
                }
            }
        }).start();
    }

    public void updateConnectionsInfo(String text) {
        view.updateConnectionsInfo(text);
    }

    public void writeOrderToDatabase(Order order) {
        model.writeOrderToDatabase(order);
    }

    public void closeResourcesAndStopServer() {
        model.close();
        System.exit(0);
    }
}
