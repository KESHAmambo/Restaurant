package restaurant.cook;

/**
 * Created by Аркадий on 14.03.2016.
 */
public class OutdatedCookView {
    CookController controller;

    public OutdatedCookView(CookController controller) {
        this.controller = controller;
    }

    public void refreshOrder() {

    }

    public int askServerPort() {
        return 0;
    }

    public String askServerAddress() {
        return null;
    }

    public String askName() {
        return null;
    }

    public void notifyConnectionStatusChanged(boolean connectionStatus) {

    }
}
