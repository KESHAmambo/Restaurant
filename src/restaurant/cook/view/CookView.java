package restaurant.cook.view;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.cook.CookController;
import restaurant.cook.CookModel;
import restaurant.kitchen.Dish;
import restaurant.kitchen.Order;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by Аркадий on 16.03.2016.
 */
public class CookView {
    private JFrame frame;
    private CookController controller;
    private CookModel model;

    private JPanel panel1;
    private JTextArea currentOrderTextArea;
    private JButton orderIsReadyButton;
    private JTextArea previousOrdersTextArea;
    private JLabel currentOrderLable;

    public CookView(final CookController controller, CookModel model) {
        this.controller = controller;
        this.model = model;

        orderIsReadyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderIsReadyButton.setEnabled(false);
                Message readyMessage = new Message(MessageType.ORDER_IS_READY, CookView.this.model.getCurrentOrder());
                controller.sendMessage(readyMessage);
                refreshPreviousOrders();
            }
        });
    }

    public void initView() {
        setNimbusLookAndFeel();
        frame = new JFrame("Brutz");
        frame.setResizable(false);
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {// If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {} // not worth my time
        }
    }

    public void refreshCurrentOrder() {
        Order order = controller.getModel().getCurrentOrder();
        currentOrderTextArea.setText("");
        for(Dish dish: order.getDishes()) {
            currentOrderTextArea.insert(dish.getName() + "\n", 0);
        }
        orderIsReadyButton.setEnabled(true);
    }

    public void refreshPreviousOrders() {
        Order previousOrder = controller.getModel().getCurrentOrder();
        for(Dish dish: previousOrder.getDishes()) {
            previousOrdersTextArea.insert(dish.getName() + "\n", 0);
        }
        previousOrdersTextArea.insert("Order:\n", 0);
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
}
