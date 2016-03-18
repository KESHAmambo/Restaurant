package restaurant.waiter.view;

import restaurant.cook.CookController;
import restaurant.kitchen.Order;
import restaurant.waiter.WaiterController;
import restaurant.waiter.WaiterModel;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by Anatoly on 18.03.2016.
 */
public class WaiterView {
    private JFrame frame;
    private WaiterController controller;
    private WaiterModel model;

    public WaiterView(WaiterController controller, WaiterModel model) {
        this.controller = controller;
        this.model = model;
    }

    public void initView() {
        //TODO
    }

    public void notifyConnectionStatusChanged(boolean actorConnected) {
        if (actorConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Connection to the server is established.",
                    "Brutz",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Client isn't connected to the server!",
                    "Brutz",
                    JOptionPane.ERROR_MESSAGE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public int askServerPort() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Enter server port:",
                    "Brutz",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Incorrect port was entered, try again.",
                        "Brutz",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String askServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter server address:",
                "Brutz",
                JOptionPane.QUESTION_MESSAGE);
    }

    public String askName() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter your name:",
                "Brutz",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void addNewClientDialog(WaiterModel.Client newClient) {
        //TODO
    }

    public void informAboutReadyOrder(Order order) {
        //TODO
    }

    public void informAboutNewText(String clientName, String text) {
        //TODO
    }

    public void informAboutEndMeal(String clientName) {
        //TODO
    }
}
