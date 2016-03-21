package restaurant.client.view;

import restaurant.Message;
import restaurant.MessageType;
import restaurant.client.ClientController;
import restaurant.client.ClientModel;
import restaurant.client.view.customcomponents.ImageButton;
import restaurant.client.view.customcomponents.ImagePanel;
import restaurant.client.view.customcomponents.TypeButton;
import restaurant.client.view.resources.animation.MyOrderAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientView {
    private final int MAX_BOX_WIDTH = 768;
    private final int MAX_BOX_HEIGHT = 550;

    private JFrame frame;
    private ClientController controller;
    private ClientModel model;

    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel typeButtonsPanel;
    private JPanel cardPanel;
    private JPanel boxPanel;
    private JPanel centerPanel;
    private JPanel myOrderPanel;
    private JPanel backOrderPanel;
    private JPanel currentOrderPanel;
    private JPanel toKitchenPanel;
    private JPanel saverPanel;

    private JLabel totalLabel;
    private JButton myOrderButton;
    private JButton assistanceButton;
    private JButton payButton;
    private JButton sendToKitchenButton;
    private JButton exitMyOrderButton;

    private List<JButton> typeButtons;
    private JButton burgersButton;
    private JButton hotDogsButton;
    private JButton sandwichesButton;
    private JButton pastaButton;
    private JButton macButton;
    private JButton sidesButton;
    private JButton saladsButton;
    private JButton coffeeButton;
    private JButton beveragesButton;
    private JButton sweetsButton;

    public ClientView(ClientController controller, ClientModel model) {
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

    private void createUIComponents() {
        northPanel = createNorthPanel();
        southPanel = createSouthPanel();
        typeButtons = createTypeButtons();
        boxPanel = createBoxPanel();
        addListenersForTypeButtons(typeButtons);
        typeButtonsPanel = createTypeButtonsPanel();
    }

    private JPanel createBoxPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        cardPanel = createCardPanel();
        myOrderPanel = createMyOrderPanel();
        resultPanel.add(cardPanel);
        resultPanel.add(myOrderPanel);
        return resultPanel;
    }

    private JPanel createMyOrderPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        resultPanel.setBackground(Color.decode("0x3C4147"));
        resultPanel.setPreferredSize(new Dimension(0, MAX_BOX_HEIGHT));
        resultPanel.setMaximumSize(new Dimension(0, MAX_BOX_HEIGHT));

        exitMyOrderButton = createExitMyOrderButton();
        backOrderPanel = createBackOrderPanel();

        resultPanel.add(exitMyOrderButton);
        resultPanel.add(backOrderPanel);
        return resultPanel;
    }

    private JButton createExitMyOrderButton() {
        JButton resultButton = new ImageButton(81, MAX_BOX_HEIGHT, new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/" +
                        "exitMyOrderButton.jpg").getImage());
        resultButton.addActionListener(createListenerForExitMyOrderButton());
        return resultButton;
    }

    private ActionListener createListenerForExitMyOrderButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flickOffMyOrderPanel();
            }
        };
    }

    private void flickOffMyOrderPanel() {
        if(!MyOrderAnimation.isMyOrderPanelSlideLeft()) {
            MyOrderAnimation myOrderAnimation = new MyOrderAnimation(
                    boxPanel, cardPanel, myOrderPanel);
            new Thread(myOrderAnimation).start();
        }
    }

    private JPanel createBackOrderPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));

        currentOrderPanel = new JPanel();
        currentOrderPanel.setLayout(new BoxLayout(currentOrderPanel, BoxLayout.PAGE_AXIS));
        currentOrderPanel.setBackground(Color.BLACK);
        currentOrderPanel.setPreferredSize(new Dimension(MAX_BOX_WIDTH - 81, MAX_BOX_HEIGHT - 50));
        currentOrderPanel.setMaximumSize(new Dimension(MAX_BOX_WIDTH - 81, MAX_BOX_HEIGHT - 50));
        currentOrderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO

        toKitchenPanel = createToKitchenPanel();
        resultPanel.add(currentOrderPanel);
        resultPanel.add(toKitchenPanel);
        return resultPanel;
    }

    private JPanel createToKitchenPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        resultPanel.setPreferredSize(new Dimension(MAX_BOX_WIDTH - 81, 50));
        resultPanel.setMaximumSize(new Dimension(MAX_BOX_WIDTH - 81, 50));
        resultPanel.setBackground(Color.decode("0x303030"));
        sendToKitchenButton = createSendToKitchenButton();
        resultPanel.add(Box.createHorizontalGlue());
        resultPanel.add(sendToKitchenButton);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        return resultPanel;
    }

    private JButton createSendToKitchenButton() {
        JButton resultButton = new ImageButton(200, 40, new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/" +
                        "sendToKitchen.png").getImage());
        resultButton.addActionListener(createListenerForSendToKitchenButton());
        return resultButton;
    }

    private ActionListener createListenerForSendToKitchenButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        };
    }

    private JPanel createCardPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new CardLayout());
        resultPanel.setBackground(Color.green);
        resultPanel.setPreferredSize(new Dimension(MAX_BOX_WIDTH, MAX_BOX_HEIGHT));
        resultPanel.setMaximumSize(new Dimension(MAX_BOX_WIDTH, MAX_BOX_HEIGHT));

        saverPanel = new ImagePanel(new ImageIcon(
                "src/restaurant/client/view/resources/panels/saver.jpg").getImage());
        resultPanel.add(saverPanel, "saver");

        for(JButton typeButton: typeButtons) {
            JPanel typePanel = new ImagePanel(new ImageIcon(
                    "src/restaurant/client/view/resources/typeimages/"
                            + typeButton.getName() + "Image.jpg").getImage());
            //TODO
            resultPanel.add(typePanel, typeButton.getName());
        }

        return resultPanel;
    }

    private List<JButton> createTypeButtons() {
        burgersButton = new TypeButton("burgers");
        hotDogsButton = new TypeButton("hotDogs");
        sandwichesButton = new TypeButton("sandwiches");
        pastaButton = new TypeButton("pasta");
        macButton = new TypeButton("mac");
        sidesButton = new TypeButton("sides");
        saladsButton = new TypeButton("salads");
        coffeeButton = new TypeButton("coffee");
        beveragesButton = new TypeButton("beverages");
        sweetsButton = new TypeButton("sweets");
        return combineButtonGroup();
    }

    private void addListenersForTypeButtons(final List<JButton> typeButtons) {
        for(JButton typeButton: typeButtons) {
            typeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton currentButton = (JButton) e.getSource();
                    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                    cardLayout.show(cardPanel, currentButton.getName());
                    setNotSelectedOtherButtons(currentButton, typeButtons);
                    flickOffMyOrderPanel();
                }
            });
        }
    }

    private void setNotSelectedOtherButtons(JButton setSelectedButton, List<JButton> Buttons) {
        for (JButton button: Buttons) {
            if(button != setSelectedButton) {
                button.setSelected(false);
            } else {
                setSelectedButton.setSelected(true);
            }
            button.repaint();
        }
    }

    private JPanel createTypeButtonsPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        resultPanel.add(burgersButton);
        resultPanel.add(hotDogsButton);
        resultPanel.add(sandwichesButton);
        resultPanel.add(pastaButton);
        resultPanel.add(macButton);
        resultPanel.add(sidesButton);
        resultPanel.add(saladsButton);
        resultPanel.add(coffeeButton);
        resultPanel.add(beveragesButton);
        resultPanel.add(sweetsButton);
        return resultPanel;
    }

    private List<JButton> combineButtonGroup() {
        List<JButton> resultGroup = new ArrayList<>();
        resultGroup.add(burgersButton);
        resultGroup.add(hotDogsButton);
        resultGroup.add(sandwichesButton);
        resultGroup.add(pastaButton);
        resultGroup.add(macButton);
        resultGroup.add(sidesButton);
        resultGroup.add(saladsButton);
        resultGroup.add(coffeeButton);
        resultGroup.add(beveragesButton);
        resultGroup.add(sweetsButton);
        return resultGroup;
    }

    private JPanel createSouthPanel() {
        JPanel resultPanel = new ImagePanel(new ImageIcon(
                "src/restaurant/client/view/resources/panels/southPanel.jpg").getImage());
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        assistanceButton = createAssistanceButton();
        payButton = createPayButton();
        resultPanel.add(Box.createHorizontalGlue());
        resultPanel.add(assistanceButton);
        resultPanel.add(payButton);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        return resultPanel;
    }

    private JButton createPayButton() {
        JButton resultButton = new ImageButton(105, 50, new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/pay.jpg").getImage());
        resultButton.addActionListener(createListenerForPayButton());
        return resultButton;
    }

    private ActionListener createListenerForPayButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                showSaverPanel();
                askCurrentClientName();
                System.out.println("pay");
            }
        };
    }

    private JButton createAssistanceButton() {
        JButton resultButton = new ImageButton(133, 50, new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/assistance.jpg").getImage());
        resultButton.addActionListener(createListenerForAssistanceButton());
        return resultButton;
    }

    private ActionListener createListenerForAssistanceButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSaverPanel();
            }
        };
    }

    private void showSaverPanel() {
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, "saver");
        flickOffMyOrderPanel();
        setNotSelectedOtherButtons(null, typeButtons);
    }

    private JPanel createNorthPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        totalLabel = createTotalLabel();
        myOrderButton = createMyOrderButton();
        myOrderButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        resultPanel.add(Box.createHorizontalGlue());
        resultPanel.add(totalLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        resultPanel.add(myOrderButton);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        return resultPanel;
    }

    private JLabel createTotalLabel() {
        JLabel resultLabel = new JLabel("TOTAL: $0.00");
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        return resultLabel;
    }

    private JButton createMyOrderButton() {
        JButton resultButton = new ImageButton(200, 40, new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/myOrder.png").getImage());
        resultButton.addActionListener(createListenerForMyOrderButton());
        return resultButton;
    }

    private ActionListener createListenerForMyOrderButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyOrderAnimation myOrderAnimation = new MyOrderAnimation(
                        boxPanel, cardPanel, myOrderPanel);
                new Thread(myOrderAnimation).start();
            }
        };
    }

    // -----------------------------------------------------------------------------

    public void informAboutNewText(String text) {
        //TODO
        System.out.println(model.getCurrentClientName() + " got message!");
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
        while(true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Enter server port:",
                    "Brutz",
                    JOptionPane.QUESTION_MESSAGE);
            try {
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

    public String askTableNumber() {
        while(true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Enter table number:",
                    "Brutz",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                int tableNumber = Integer.parseInt(port.trim());
                model.setTableNumber(tableNumber);
                return "Table " + tableNumber;
            }catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Incorrect table number was entered, try again.",
                        "Brutz",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void askCurrentClientName() {
        String currentName;
        while (true) {
            currentName = JOptionPane.showInputDialog(
                    frame,
                    "Enter your name, please:",
                    "Brutz",
                    JOptionPane.QUESTION_MESSAGE);
            if(currentName != null && !"".equals(currentName.trim())) {
                break;
            }
        }
        model.setCurrentClientName(String.format("(%d) %s", model.getTableNumber(), currentName.trim()));
        controller.sendMessage(new Message(MessageType.NEW_CLIENT, model.getCurrentClientName()));
    }

    private void setNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) { // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }catch (Exception ex) {} // not worth my time
        }
    }
}
