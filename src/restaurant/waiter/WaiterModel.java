package restaurant.waiter;

import restaurant.kitchen.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anatoly on 18.03.2016.
 */
public class WaiterModel {
    private List<Client> clients = new ArrayList<>();

    public Client addNewClient(String clientName) {
        Client client = new Client(clientName);
        clients.add(client);
        return client;
    }

    public void addOrderToClientBill(Order order) {
        //TODO
    }

    public void deleteClient(String clientName) {
        //TODO
    }

    public static Client getClientByName(String clientName) {
        return null;
    }

    public class Client {
        private String name;
        private JPanel dialogPanel;
        private JTextArea messagesArea;
        private JPanel buttonPanel;
        private double bill;

        public Client(String name) {
            this.name = name;
        }

        public JPanel getDialogPanel() {
            return dialogPanel;
        }

        public void setDialogPanel(JPanel dialogPanel) {
            this.dialogPanel = dialogPanel;
        }

        public double getBill() {
            return bill;
        }

        public void setBill(double bill) {
            this.bill = bill;
        }

        public JTextArea getMessagesArea() {
            return messagesArea;
        }

        public void setMessagesArea(JTextArea messagesArea) {
            this.messagesArea = messagesArea;
        }

        public JPanel getButtonPanel() {
            return buttonPanel;
        }

        public void setButtonPanel(JPanel buttonPanel) {
            this.buttonPanel = buttonPanel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
