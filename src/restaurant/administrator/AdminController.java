package restaurant.administrator;

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
        model.serializeNewMenu();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        askLoginInfoAndStart();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    view.showWarningDialog("SERVER CRASHED!");
                    stopServer();
                }
            }

            private void askLoginInfoAndStart() throws IOException, ClassNotFoundException {
                try {
                    String address = view.askServerAddress();
                    int port = view.askServerPort();
                    Server.start(AdminController.this, address, port, model.getNeedImageDishes(),
                            model.getNotNeedImageDishes(), model.getStatusChangedDishes());
                } catch(UnknownHostException e) {
                    view.showWarningDialog("Invalid server address!");
                } catch(IllegalArgumentException e) {
                    view.showWarningDialog("Invalid port!");
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

    public void stopServer() {
        model.close();
        System.exit(0);
    }
}
