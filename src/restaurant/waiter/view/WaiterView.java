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
import java.util.Random;

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

    public WaiterView(final WaiterController controller, WaiterModel model) {
        this.controller = controller;
        this.model = model;

        /*// test buttons
        addNewClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                addNewClientDialog(WaiterView.this.model.new Client("Alex" + random.nextInt(10)));
            }
        });
        informAboutTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.informAboutNewText("Anatoly", "Hello, guy!");
            }
        });
        addClientAnatolyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.informAboutNewClient("Anatoly");
            }
        });
        informAboutEndMealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.informAboutEndMeal("Anatoly");
            }
        });*/
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
        JPanel forButtonPanel = createButtonForChoosingDialog(clientName);
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
        JLabel label = new JLabel(clientName);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font labelFont = new Font("Ar Cena", Font.PLAIN, 20);
        label.setFont(labelFont);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        northPanel.add(Box.createRigidArea(new Dimension(0, 65)));
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(label);
        northPanel.add(Box.createHorizontalGlue());
        dialogPanel.add(northPanel, BorderLayout.NORTH);
    }

    private JTextArea createCenterPartOfDialogPanel(JPanel dialogPanel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout());
        final JTextArea messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);
        centerPanel.add(messagesScrollPane);
        dialogPanel.add(centerPanel, BorderLayout.CENTER);
        return messagesArea;
    }

    private void createSouthPartOfDialogPanel(final String clientName, final JTextArea messagesArea, JPanel dialogPanel) {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
        southPanel.setOpaque(false);
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
        final JTextField messageToSendField = new JTextField();
        messageToSendField.setPreferredSize(new Dimension(380, 30));
        messageToSendField.setMaximumSize(new Dimension(380, 30));
        Button sendButton = new Button("SEND");
        sendButton.setPreferredSize(new Dimension(380, 40));
        sendButton.setMaximumSize(new Dimension(380, 40));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageToSendField.getText();
                if (!"".equals(text.trim())) {
                    messageToSendField.setText("");
                    messagesArea.insert("You: " + text + "\n", 0);
                    controller.sendMessage(new Message(MessageType.TEXT, clientName, text));
                }
            }
        });
        southPanel.add(messageToSendField);
        southPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        southPanel.add(sendButton);
        dialogPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonForChoosingDialog(final String clientName) {
        final JPanel forButtonPanel = new JPanel();
        forButtonPanel.setLayout(new BoxLayout(forButtonPanel, BoxLayout.LINE_AXIS));
        forButtonPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        forButtonPanel.setOpaque(false);
        JButton toDialogButton = new JButton(clientName);
        toDialogButton.setPreferredSize(new Dimension(150, 32));
        toDialogButton.setMaximumSize(new Dimension(150, 32));
        toDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, clientName);
                forButtonPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                forButtonPanel.setOpaque(false);
                buttonsPanel.revalidate();
                buttonsPanel.repaint();
            }
        });
        forButtonPanel.add(toDialogButton);
        buttonsPanel.add(forButtonPanel);
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        return forButtonPanel;
    }

    /**Setting necessary fields to newClient to have access to
     * dialog messages area and button panel background in future
     */
    private void addNecessaryFieldsToClient(WaiterModel.Client newClient, JPanel dialogPanel,
                                            JTextArea messagesArea, JPanel forButtonPanel) {
        newClient.setDialogPanel(dialogPanel);
        newClient.setButtonPanel(forButtonPanel);
        newClient.setMessagesArea(messagesArea);
    }

    public void informAboutReadyOrder(Order order) {
        JOptionPane.showMessageDialog(
                frame,
                "Order for " + order.getClientName() +
                        " is ready. Get it from " + order.getCook(),
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
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    public void informAboutEndMeal(String clientName) {
        WaiterModel.Client client = model.getClientByName(clientName);
        JOptionPane.showMessageDialog(
                frame,
                "Client " + clientName + " is ready to pay.\nBill: " + client.getBill(),
                "Brutz",
                JOptionPane.INFORMATION_MESSAGE);
        cardPanel.remove(client.getDialogPanel());
        cardPanel.revalidate();
        cardPanel.repaint();
        buttonsPanel.remove(client.getButtonPanel());
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }
}
