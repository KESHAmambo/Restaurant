package restaurant.client.view;

import restaurant.kitchen.Message;
import restaurant.kitchen.MessageType;
import restaurant.client.ClientController;
import restaurant.client.ClientModel;
import restaurant.client.view.animation.FullDescAnimation;
import restaurant.client.view.customcomponents.*;
import restaurant.client.view.animation.MyOrderAnimation;
import restaurant.kitchen.*;
import restaurant.kitchen.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientView {
    /**
     * It is very important to care about pref, max, min sizes
     * of all components. Without min size you'll get some not
     * understandable graphical bugs like borders and shrunk
     * elements, although they have place for widening in BoxLayout.
     */

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
    private NotificationButton assistanceButton;
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
    private JTextArea messagesTextArea;
    private JButton sendMessageButton;

    private List<JPanel> menuPanels;

    public ClientView(ClientController controller, ClientModel model) {
        this.controller = controller;
        this.model = model;
    }

    public void initView() {
        frame = new JFrame("Brutz");
        frame.setResizable(false);
        frame.setContentPane(this.mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

//        removePieceOfMyOrderPanel();
    }

    /**
     * FIXED: with setting min size of myOrderPanel this method isn't
     * useful now.
     *
     * This method is a crutch. Cause of creating this method is
     * mentioned in method createBackOrderPanel()
     */
    private void removePieceOfMyOrderPanel() {
        Dimension cardDim = new Dimension(MAX_BOX_WIDTH, MAX_BOX_HEIGHT);
        setPrefMaxMinSizes(cardPanel, cardDim);
        Dimension myOrderDim = new Dimension(MAX_BOX_WIDTH, MAX_BOX_HEIGHT);
        setPrefMaxMinSizes(myOrderPanel, myOrderDim);
        boxPanel.revalidate();
        boxPanel.repaint();
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
        setPrefMaxMinSizes(resultPanel, new Dimension(0, MAX_BOX_HEIGHT));
        exitMyOrderButton = createExitMyOrderButton();
        backOrderPanel = createBackOrderPanel();
        resultPanel.add(exitMyOrderButton);
        resultPanel.add(backOrderPanel);
        return resultPanel;
    }

    private void setPrefMaxMinSizes(Component component, Dimension preferredSize) {
        component.setPreferredSize(preferredSize);
        component.setMaximumSize(preferredSize);
        component.setMinimumSize(preferredSize);
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
        if(!MyOrderAnimation.isSlideToLeft()) {
            MyOrderAnimation myOrderAnimation = new MyOrderAnimation(
                    boxPanel, cardPanel, myOrderPanel);
            new Thread(myOrderAnimation).start();
//            myOrderButton.setNotificationOn(false);
        }
    }

    private JPanel createBackOrderPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        currentOrderPanel = createCurrentOrderPanel();
        JScrollPane scrollPane = surroundWithScrollPane(currentOrderPanel);
        setPrefMaxMinSizes(scrollPane, new Dimension(MAX_BOX_WIDTH - 81, MAX_BOX_HEIGHT - 50));
        toKitchenPanel = createToKitchenPanel();

        resultPanel.add(scrollPane);
        resultPanel.add(toKitchenPanel);
        return resultPanel;
    }

    private JPanel createCurrentOrderPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        resultPanel.setBackground(Color.BLACK);
        setPrefMaxMinSizes(resultPanel, new Dimension(MAX_BOX_WIDTH - 81, MAX_BOX_HEIGHT - 50));
        // without minSize there will be strange borders around cardPanel and myOrderPanel
        // but with it a piece of myOrderPanel is showed when you start program
        // @see removePieceOfMyOrderPanel()
        resultPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel labelPanel = createLabelPanelForCurrentOrder();
        resultPanel.add(labelPanel);
        return resultPanel;
    }

    private JPanel createLabelPanelForCurrentOrder() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
        labelPanel.setBackground(Color.decode("0x4B4B4B"));
        setPrefMaxMinSizes(labelPanel, new Dimension(MAX_BOX_WIDTH - 81, 50));
        JLabel label = new JLabel("MY ORDER");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Dialog", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        labelPanel.add(label);
        return labelPanel;
    }

    private JPanel createToKitchenPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(MAX_BOX_WIDTH - 81, 50));
        resultPanel.setBackground(Color.decode("0x303030"));
        sendToKitchenButton = createSendToKitchenButton();
        resultPanel.add(Box.createHorizontalGlue());
        resultPanel.add(sendToKitchenButton);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        return resultPanel;
    }

    private JButton createSendToKitchenButton() {
        AnimateButton resultButton = new AnimateButton(200, 40,
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/sendToKitchen.png").getImage(),
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/smallSendToKitchen.png").getImage());
        resultButton.addActionListener(createListenerForSendToKitchenButton(resultButton));
        return resultButton;
    }

    private ActionListener createListenerForSendToKitchenButton(final AnimateButton sendToKitchenButton) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendToKitchenButton.startAnimation();
                Order orderToSend = model.getOrder();
                if(orderToSend.getDishes().size() != 0) {
                    controller.sendMessage(new Message(MessageType.ORDER, orderToSend));
                    model.setFinalBill(model.getCurrentBill());
                    model.setOrderEmpty();
                    cleanCurrentOrderPanel();
                }
            }
        };
    }

    private JPanel createCardPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new CardLayout());
        resultPanel.setBackground(Color.green);
        setPrefMaxMinSizes(resultPanel, new Dimension(MAX_BOX_WIDTH, MAX_BOX_HEIGHT));

        saverPanel = createSaverPanel();
        resultPanel.add(saverPanel, "saver");

        for(JButton typeButton: typeButtons) {
            JPanel typePanel = createTypePanel(typeButton);
            resultPanel.add(typePanel, typeButton.getName());
        }
        return resultPanel;
    }

    private JPanel createSaverPanel() {
        JPanel resultPanel = new ImagePanel(
                new ImageIcon("src/restaurant/client/view/resources/panels/saver.jpg").getImage(),
                0, 0);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));

        JPanel dialogPanel = createDialogPanel();

        resultPanel.add(Box.createRigidArea(new Dimension(0, 200)));
        resultPanel.add(dialogPanel);
        return resultPanel;
    }

    private JPanel createDialogPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setOpaque(false);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        resultPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setPrefMaxMinSizes(resultPanel, new Dimension(500, 350));

        messagesTextArea = createMessagesTextArea();
        JScrollPane scrollPane = surroundWithScrollPane(messagesTextArea);
        setPrefMaxMinSizes(scrollPane, new Dimension(500, 200));
        JPanel panelForSendingMessages = createPanelForSendingMessages();

        resultPanel.add(scrollPane);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        resultPanel.add(panelForSendingMessages);
        resultPanel.add(Box.createVerticalGlue());
        return resultPanel;
    }

    private JPanel createPanelForSendingMessages() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        resultPanel.setOpaque(false);
        setPrefMaxMinSizes(resultPanel, new Dimension(500, 50));

        final JTextField sendingTextField = createSendingTextField();
        sendMessageButton = createSendMessageButton(sendingTextField);

        resultPanel.add(sendingTextField);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        resultPanel.add(sendMessageButton);
        return resultPanel;
    }

    private JTextField createSendingTextField() {
        final JTextField resultField = new JTextField();
        resultField.setBackground(Color.decode("0x33383E"));
        resultField.setForeground(Color.decode("0xF0F0F0"));
        resultField.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        resultField.setCaretColor(Color.decode("0xF0F0F0"));
        setPrefMaxMinSizes(resultField, new Dimension(350, 30));
        return resultField;
    }

    private JButton createSendMessageButton(final JTextField sendingMessageField) {
        JButton resultButton = new ImageButton(120, 50, new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/send.png").getImage());
        resultButton.addActionListener(createListenerForSendMessageButton(sendingMessageField));
        return resultButton;
    }

    private ActionListener createListenerForSendMessageButton(final JTextField sendingMessageField) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = sendingMessageField.getText();
                if (!"".equals(text.trim())) {
                    sendingMessageField.setText("");
                    messagesTextArea.insert("You: " + text + "\n", 0);
                    controller.sendMessage(new Message(
                            MessageType.TEXT, model.getCurrentClientName(), text));
                }
            }
        };
    }

    private JTextArea createMessagesTextArea() {
        JTextArea resultTextArea = new JTextArea();
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultTextArea.setBackground(Color.decode("0x33383E"));
        resultTextArea.setForeground(Color.decode("0xF0F0F0"));
        resultTextArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        return resultTextArea;
    }

    private JPanel createTypePanel(JButton typeButton) {
        JPanel resultPanel = new ImagePanel(
                new ImageIcon("src/restaurant/client/view/resources/typeimages/"
                        + typeButton.getName() + "Image.jpg").getImage(),
                0, 0);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));

        JPanel menuPanel = createMenuPanel(typeButton.getName());
        JScrollPane scrollPane = surroundWithScrollPane(menuPanel);
        setPrefMaxMinSizes(scrollPane, new Dimension(MAX_BOX_WIDTH - 180, MAX_BOX_HEIGHT));

        resultPanel.add(Box.createRigidArea(new Dimension(180, 0)));
        resultPanel.add(scrollPane);
        return resultPanel;
    }

    private JPanel createMenuPanel(String typeName) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        resultPanel.setBackground(Color.BLACK);
        resultPanel.setName(typeName);

        if(menuPanels == null) {
            menuPanels = new ArrayList<>();
        }
        menuPanels.add(resultPanel);

        return resultPanel;
    }

    public void updateMenuPanels() {
        Menu menu = model.getMenu();
        for(JPanel panel: menuPanels) {
            List<Dish> dishes = menu.getDishesByType(panel.getName());
            for (final Dish dish : dishes) {
                if (!dish.isDeleted()) {
                    DishPanelForMenu dishPanelForMenu = createDishPanelForMenu(panel, dish);
                    panel.add(dishPanelForMenu);
                }
            }

            setPrefMaxMinSizes(panel, new Dimension(MAX_BOX_WIDTH - 180, dishes.size() * 100));
            panel.revalidate();
            panel.repaint();
        }
    }

    private DishPanelForMenu createDishPanelForMenu(JPanel menuPanel, Dish dish) {
        DishPanelForMenu dishPanelForMenu = new DishPanelForMenu(dish);
        ShortDishDescPanel shortDescPanel =
                createShortDishDescPanel(menuPanel, dish, dishPanelForMenu);
        dishPanelForMenu.collect(shortDescPanel);
        return dishPanelForMenu;
    }

    private ShortDishDescPanel createShortDishDescPanel(
            JPanel menuPanel, Dish dish, DishPanelForMenu dishPanelForMenu) {
        ShortDishDescPanel resultPanel = new ShortDishDescPanel(
                dish, MAX_BOX_WIDTH - 180);

        JButton plusButton = createPlusButton(dish);
        JButton fullDescButton = createFullDescButton(menuPanel, dishPanelForMenu);

        resultPanel.add(plusButton);
        resultPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        resultPanel.add(fullDescButton);
        resultPanel.revalidate();
        resultPanel.repaint();
        return resultPanel;
    }

    private JButton createPlusButton(Dish dish) {
        AnimateButton plusButton = new AnimateButton(50, 50,
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/plus.png").getImage(),
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/smallPlus.png").getImage()
        );
        plusButton.addActionListener(createListenerForPlusButton(plusButton, dish));
        return plusButton;
    }

    private ActionListener createListenerForPlusButton(final AnimateButton plusButton, final Dish dish) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plusButton.startAnimation();
                model.addDishToOrder(dish);
                model.setCurrentBill(model.getCurrentBill() + dish.getPrice());
                updateTotalLabel();
                addNewDishToCurrentOrderPanel(dish);
//                myOrderButton.setNotificationOn(true);
            }
        };
    }

    private void addNewDishToCurrentOrderPanel(Dish dish) {
        boolean dishAlreadyOrdered = checkDishAlreadyOrderedAndIncrementCount(dish);
        if (!dishAlreadyOrdered) {
            JPanel dishDescriptionPanel = createNewDishPanelForCurrentOrder(dish);
            currentOrderPanel.add(dishDescriptionPanel);
        }
        resizeAndRepaintCurrentOrderPanel(!dishAlreadyOrdered);
    }

    private JPanel createNewDishPanelForCurrentOrder(Dish dish) {
        JPanel dishDescriptionPanel = new ShortDishDescPanel(dish, MAX_BOX_WIDTH - 81);

        JLabel countLabel = createCountLabel();
        JButton plusButton = createPlusButton(dish);
        JButton binButton = createBinButton(dish);

        dishDescriptionPanel.add(countLabel);
        dishDescriptionPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        dishDescriptionPanel.add(plusButton);
        dishDescriptionPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        dishDescriptionPanel.add(binButton);
        return dishDescriptionPanel;
    }

    private JLabel createCountLabel() {
        JLabel countLabel = new JLabel("X 1");
        setPrefMaxMinSizes(countLabel, new Dimension(50, 100));
        countLabel.setName("countLabel");
        countLabel.setForeground(Color.WHITE);
        countLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        return countLabel;
    }

    private boolean checkDishAlreadyOrderedAndIncrementCount(Dish dish) {
        for(Component component: currentOrderPanel.getComponents()) {
            if(dish.getName().equals(component.getName())) {
                JPanel dishPanel = (JPanel) component;
                incrementCountLabel(dishPanel);
                return true;
            }
        }
        return false;
    }

    private void incrementCountLabel(JPanel panel) {
        for(Component component: panel.getComponents()) {
            if("countLabel".equals(component.getName())) {
                JLabel countLabel = (JLabel) component;
                String text = countLabel.getText();
                int count = Integer.parseInt(text.substring(2));
                count++;
                countLabel.setText("X " + count);
                return;
            }
        }
    }

    private void resizeAndRepaintCurrentOrderPanel(boolean needResize) {
        if (needResize) {
            int orderSize = model.getOrder().getDifferentDishes().size();
            if(orderSize >= 5) {
                setPrefMaxMinSizes(currentOrderPanel,
                        new Dimension(MAX_BOX_WIDTH - 81, orderSize * 100 + 50));
            } else {
                setPrefMaxMinSizes(currentOrderPanel,
                        new Dimension(MAX_BOX_WIDTH - 81, 4 * 100 + 50));
            }
        }
        currentOrderPanel.revalidate();
        currentOrderPanel.repaint();
    }

    private JButton createBinButton(Dish dish) {
        AnimateButton binButton = new AnimateButton(50, 50,
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/bin.png").getImage(),
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/smallBin.png").getImage());
        binButton.addActionListener(createListenerForBinButton(binButton, dish));
        return binButton;
    }

    private ActionListener createListenerForBinButton(final AnimateButton binButton, final Dish dish) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                binButton.startAnimation();
                model.getOrder().removeDish(dish);
                model.setCurrentBill(model.getCurrentBill() - dish.getPrice());
                updateTotalLabel();
                removeDishFromCurrentOrderPanel(dish);
            }
        };
    }

    private void removeDishFromCurrentOrderPanel(Dish dish) {
        for (Component component: currentOrderPanel.getComponents()) {
            if (dish.getName().equals(component.getName())) {
                JPanel dishPanel = (JPanel) component;
                boolean greaterThenOneDish = checkGreaterThenOneDishAndDecrease(dishPanel);
                if (!greaterThenOneDish) {
                    currentOrderPanel.remove(dishPanel);
                }
                resizeAndRepaintCurrentOrderPanel(!greaterThenOneDish);
                return;
            }
        }
    }

    private boolean checkGreaterThenOneDishAndDecrease(JPanel dishPanel) {
        for (Component component: dishPanel.getComponents()) {
            if ("countLabel".equals(component.getName())) {
                JLabel countLabel = (JLabel) component;
                String text = countLabel.getText();
                int count = Integer.parseInt(text.substring(2));
                if (count == 1) {
                    return false;
                }
                count--;
                countLabel.setText("X " + count);
                    return true;
            }
        }
        return false;
    }

    private void updateTotalLabel() {
        totalLabel.setText(String.format("TOTAL: $%.2f", model.getCurrentBill()));
        totalLabel.revalidate();
        totalLabel.repaint();
    }

    private JButton createFullDescButton(
            JPanel menuPanel, DishPanelForMenu dishPanelForMenu) {
        JButton fullDescButton = new FullDescButton(50, 50,
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/fullDescClosed.png").getImage(),
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/arrow.png").getImage());
        fullDescButton.addActionListener(
                createListenerForFullDescButton(menuPanel, dishPanelForMenu));
        return fullDescButton;
    }

    private ActionListener createListenerForFullDescButton(
            final JPanel menuPanel, final DishPanelForMenu dishPanelForMenu) {
        return new ActionListener() {
            FullDescAnimation animation = new FullDescAnimation(menuPanel, dishPanelForMenu);

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(animation).start();
                JButton button = (JButton) e.getSource();
                button.setSelected(!button.isSelected());
                button.repaint();
            }
        };
    }

    private JScrollPane surroundWithScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JScrollBar bar = new JScrollBar();
        bar.setPreferredSize(new Dimension(0, -1));
        bar.setUnitIncrement(25);
        scrollPane.setVerticalScrollBar(bar);
        return scrollPane;
    }

    private List<JButton> createTypeButtons() {
        List<JButton> resultList = new ArrayList<>();
        Set<String> dishTypes = new Menu().getStore().keySet();
        for(String dishType: dishTypes) {
            JButton button = new TypeButton(dishType);
            resultList.add(button);
        }
        return resultList;
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
        for(JButton button: typeButtons) {
            resultPanel.add(button);
        }
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
        JPanel resultPanel = new ImagePanel(
                new ImageIcon("src/restaurant/client/view/resources/panels/southPanel.jpg").getImage(),
                0, 0);
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
                controller.sendMessage(new Message(
                        MessageType.END_MEAL, model.getCurrentClientName(), model.getFinalBill()));
                showNotifyWindow(frame,
                        String.format("Waiter will come to you in a minute.\n" +
                        "Your bill: $%.2f\nThank you! Come again!", model.getFinalBill()),
                        JOptionPane.INFORMATION_MESSAGE);
                removeDataOfPayingClient();

                askNewClientName();
                model.setOrderEmpty();
            }

            private void removeDataOfPayingClient() {
//                myOrderButton.setNotificationOn(false);
                showSaverPanel();
                model.setCurrentBill(0);
                model.setFinalBill(0);
                updateTotalLabel();
                messagesTextArea.setText("");
                cleanCurrentOrderPanel();
            }
        };
    }

    private void cleanCurrentOrderPanel() {
        for(Component component: currentOrderPanel.getComponents()) {
            if(component instanceof ShortDishDescPanel) {
                currentOrderPanel.remove(component);
            }
        }
        resizeAndRepaintCurrentOrderPanel(true);
    }

    private void showNotifyWindow(JFrame frame, String text, int informationMessage) {
        JOptionPane.showMessageDialog(
                frame,
                text,
                "Brutz",
                informationMessage);
    }

    private NotificationButton createAssistanceButton() {
        NotificationButton resultButton = new NotificationButton(133, 50,
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/assistance.jpg").getImage(),
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/assistanceOn.jpg").getImage());
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
        assistanceButton.setNotificationOn(false);
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

    private AnimateButton createMyOrderButton() {
        AnimateButton resultButton = new AnimateButton(200, 40,
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/myOrder.png").getImage(),
                new ImageIcon("src/restaurant/client/view/resources/controlbuttons/smallMyOrder.png").getImage());
        resultButton.addActionListener(createListenerForMyOrderButton(resultButton));
        return resultButton;
    }

    private ActionListener createListenerForMyOrderButton(final AnimateButton myOrderButton) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myOrderButton.startAnimation();
                MyOrderAnimation myOrderAnimation = new MyOrderAnimation(
                        boxPanel, cardPanel, myOrderPanel);
                new Thread(myOrderAnimation).start();
//                myOrderButton.setNotificationOn(false);
            }
        };
    }

    // -----------------------------------------------------------------------------

    public void informAboutNewText(String text) {
        assistanceButton.setNotificationOn(true);
        messagesTextArea.insert("Waiter: " + text + "\n", 0);
    }

    public void notifyConnectionStatusChanged(boolean actorConnected) {
        if (actorConnected) {
            showNotifyWindow(frame, "Connection to the server is established.", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showNotifyWindow(frame, "Client isn't connected to the server!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
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
                showNotifyWindow(frame, "Incorrect port was entered, try again.", JOptionPane.ERROR_MESSAGE);
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
                return "" + tableNumber;
            }catch (Exception e) {
                showNotifyWindow(frame, "Incorrect table number was entered, try again.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void askNewClientName() {
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
}
