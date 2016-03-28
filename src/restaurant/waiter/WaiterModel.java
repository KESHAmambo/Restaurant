package restaurant.waiter;

import restaurant.kitchen.Dish;
import restaurant.kitchen.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
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

    public void deleteClient(String clientName) {
        Iterator<Client> iterator = clients.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().getName().equals(clientName)) {
                iterator.remove();
                break;
            }
        }
    }

    public Client getClientByName(String clientName) {
        for(Client client: clients) {
            if(client.getName().equals(clientName)) {
                return client;
            }
        }
        return null;
    }

    public class Client {
        private String name;
        private JPanel dialogPanel;
        private JTextArea messagesArea;
        private JPanel buttonPanel;

        public Client(String name) {
            this.name = name;
        }

        public JPanel getDialogPanel() {
            return dialogPanel;
        }

        public void setDialogPanel(JPanel dialogPanel) {
            this.dialogPanel = dialogPanel;
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

        @Override
        public String toString() {
            return "Client{" +
                    "name='" + name + '\'' +
                    ", dialogPanel=" + dialogPanel +
                    ", messagesArea=" + messagesArea +
                    ", buttonPanel=" + buttonPanel +
                    '}';
        }
    }
}
