package restaurant.waiter.view;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.kitchen.Order;
import restaurant.waiter.WaiterController;
import restaurant.waiter.WaiterModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by Anatoly on 18.03.2016.
 */
public class WaiterView {
    private JFrame frame;
    private WaiterController controller;
    private WaiterModel model;

    private JPanel mainPanel;
    private JPanel leftBackPanel;
    private JPanel cardPanel;
    private JPanel buttonsPanel;
    /*private JButton addNewClientButton;
    private JButton informAboutTextButton;
    private JButton addClientAnatolyButton;
    private JButton informAboutEndMealButton;*/

    public WaiterView(WaiterController controller, WaiterModel model) {
        this.controller = controller;
        this.model = model;
    }

    public void initView() {
        setNimbusLookAndFeel();
        frame = new JFrame("Brutz");
        frame.setResizable(false);
        frame.setContentPane(this.mainPanel);
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
        }catch(Exception e) {// If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {} // not worth my time
        }
    }

    private void createUIComponents() {
        cardPanel = new JPanel();
        cardPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    public void notifyConnectionStatusChanged(boolean actorConnected) {
        if(actorConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Connection to the server is established.",
                    "Brutz",
                    JOptionPane.INFORMATION_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Client isn't connected to the server!",
                    "Brutz",
                    JOptionPane.ERROR_MESSAGE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public int askServerPort() {
        while(true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Enter server port:",
                    "Brutz",
                    JOptionPane.QUESTION_MESSAGE);
            try{
                return Integer.parseInt(port.trim());
            }catch (Exception e) {
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
        String clientName = newClient.getName();
        JPanel dialogPanel = createPanelForDialogPanel();
        createNorthPartOfDialogPanel(clientName, dialogPanel);
        JTextArea messagesArea = createCenterPartOfDialogPanel(dialogPanel);
        createSouthPartOfDialogPanel(clientName, messagesArea, dialogPanel);
        cardPanel.add(dialogPanel, clientName);
        JPanel forButtonPanel = createPanelForChoosingDialog(clientName);
        addNecessaryFieldsToClient(newClient, dialogPanel, messagesArea, forButtonPanel);
    }

    private JPanel createPanelForDialogPanel() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout());
        dialogPanel.setSize(400, 600);
        dialogPanel.setOpaque(false);
        return dialogPanel;
    }

    private void createNorthPartOfDialogPanel(String clientName, JPanel dialogPanel) {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createLineBorder(Color.white));

        JLabel clientNameLabel = createClientNameLabel(clientName);

        northPanel.add(Box.createRigidArea(new Dimension(0, 65)));
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(clientNameLabel);
        northPanel.add(Box.createHorizontalGlue());

        dialogPanel.add(northPanel, BorderLayout.NORTH);
    }

    private JLabel createClientNameLabel(String clientName) {
        JLabel clientNameLabel = new JLabel(clientName);
        clientNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clientNameLabel.setFont(new Font("Ar Cena", Font.PLAIN, 20));
        clientNameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        return clientNameLabel;
    }

    private JTextArea createCenterPartOfDialogPanel(JPanel dialogPanel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout());

        JTextArea messagesArea = createMessagesArea();
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);

        centerPanel.add(messagesScrollPane);
        dialogPanel.add(centerPanel, BorderLayout.CENTER);
        return messagesArea;
    }

    private JTextArea createMessagesArea() {
        JTextArea messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        return messagesArea;
    }

    private void createSouthPartOfDialogPanel(
            String clientName, JTextArea messagesArea,
            JPanel dialogPanel) {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
        southPanel.setOpaque(false);
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));

        JTextField sendingMessageField = createSendingMessageField();
        Button sendButton = createSendButton(clientName, messagesArea, sendingMessageField);

        southPanel.add(sendingMessageField);
        southPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        southPanel.add(sendButton);
        dialogPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private Button createSendButton(
            String clientName, JTextArea messagesArea,
            JTextField sendingMessageField) {
        Button sendButton = new Button("SEND");
        sendButton.setPreferredSize(new Dimension(380, 40));
        sendButton.setMaximumSize(new Dimension(380, 40));
        sendButton.addActionListener(createListenerForSendButton(
                clientName, messagesArea, sendingMessageField));
        return sendButton;
    }

    private ActionListener createListenerForSendButton(
            final String clientName, final JTextArea messagesArea,
            final JTextField sendingMessageField) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = sendingMessageField.getText();
                if (!"".equals(text.trim())) {
                    sendingMessageField.setText("");
                    messagesArea.insert("You: " + text + "\n", 0);
                    controller.sendMessage(new Message(MessageType.TEXT, clientName, text));
                }
            }
        };
    }

    private JTextField createSendingMessageField() {
        JTextField sendingMessageField = new JTextField();
        sendingMessageField.setPreferredSize(new Dimension(380, 30));
        sendingMessageField.setMaximumSize(new Dimension(380, 30));
        return sendingMessageField;
    }

    private JPanel createPanelForChoosingDialog(String clientName) {
        JPanel forButtonPanel = new JPanel();
        forButtonPanel.setLayout(new BoxLayout(forButtonPanel, BoxLayout.LINE_AXIS));
        forButtonPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        forButtonPanel.setOpaque(false);

        createToDialogButton(clientName, forButtonPanel);

        buttonsPanel.add(forButtonPanel);
        updateButtonsPanel();
        return forButtonPanel;
    }

    private void updateButtonsPanel() {
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    private void createToDialogButton(String clientName, JPanel forButtonPanel) {
        JButton toDialogButton = new JButton(clientName);
        toDialogButton.setPreferredSize(new Dimension(150, 32));
        toDialogButton.setMaximumSize(new Dimension(150, 32));
        toDialogButton.addActionListener(createListenerForToDialogButton(
                clientName, forButtonPanel));
        forButtonPanel.add(toDialogButton);
    }

    private ActionListener createListenerForToDialogButton(
            final String clientName, final JPanel forButtonPanel) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, clientName);
                forButtonPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                forButtonPanel.setOpaque(false);
                updateButtonsPanel();
            }
        };
    }

    /**Setting necessary fields for newClient to have access to
     * dialog messages area and button panel in future
     */
    private void addNecessaryFieldsToClient(WaiterModel.Client newClient, JPanel dialogPanel,
                                            JTextArea messagesArea, JPanel forButtonPanel) {
        newClient.setDialogPanel(dialogPanel);
        newClient.setButtonPanel(forButtonPanel);
        newClient.setMessagesArea(messagesArea);
    }

    public void informAboutReadyOrder(Order order) {
        String clientName = order.getClientName();
        controller.sendMessage(new Message(MessageType.TEXT, clientName, "Your order is ready!"));
        WaiterModel.Client client = model.getClientByName(clientName);
        JTextArea messagesArea = client.getMessagesArea();
        messagesArea.insert("You: Your order is ready!\n", 0);

        JOptionPane.showMessageDialog(
                frame,
                "Order for " + clientName + " is ready.\n" +
                        "Get it from " + order.getCook(),
                "Brutz",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void informAboutNewText(String clientName, String text) {
        WaiterModel.Client client = model.getClientByName(clientName);
        JTextArea messagesArea = client.getMessagesArea();
        messagesArea.insert(clientName + ":  " + text + "\n", 0);
        JPanel forButtonPanel = client.getButtonPanel();
        forButtonPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.orange));
        forButtonPanel.setOpaque(false);
        updateButtonsPanel();
    }

    public void informAboutEndMeal(String clientName, double bill) {
        WaiterModel.Client client = model.getClientByName(clientName);
        JOptionPane.showMessageDialog(
                frame,
                String.format("Client %s is ready to pay.\n" +
                        "Bill: %.2f", clientName, bill),
                "Brutz",
                JOptionPane.INFORMATION_MESSAGE);
        cardPanel.remove(client.getDialogPanel());
        cardPanel.revalidate();
        cardPanel.repaint();
        buttonsPanel.remove(client.getButtonPanel());
        updateButtonsPanel();
    }
}
