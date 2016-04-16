package restaurant.administrator.view.customcomponents;

import restaurant.SwingHelper;
import restaurant.kitchen.Dish;
import restaurant.kitchen.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.List;

import static restaurant.SwingHelper.setPrefMaxMinSizes;

/**
 * Created by Аркадий on 01.04.2016.
 */
public class MenuPanel extends JPanel {
    private JTextArea menuTextArea;
    private JTextArea connectionsTextArea;
    private JButton startButton;
    private JButton exitButton;

    public MenuPanel(Menu menu, ActionListener startButtonListener, ActionListener stopButtonListener) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        menuTextArea = createTextArea(Color.decode("0x2798AA"));
        JScrollPane scrollPane = new JScrollPane(menuTextArea);
        setPrefMaxMinSizes(scrollPane, new Dimension(700, 400));
        updateMenuTextArea(menu);
        JPanel southPanel = createSouthPanel(startButtonListener, stopButtonListener);
        JLabel label = createLabel("Connections:");

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(label);
        add(southPanel);
    }

    private JTextArea createTextArea(Color fontColor) {
        JTextArea textArea = new JTextArea();
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setForeground(fontColor);
        textArea.setFont(new Font("Dialog", Font.BOLD, 14));
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    public void updateMenuTextArea(Menu menu) {
        menuTextArea.setText("");

        StringBuilder resultText = new StringBuilder();
        for(Map.Entry<String, List<Dish>> pair: menu.getStore().entrySet()) {
            resultText.append(pair.getKey().toUpperCase());
            resultText.append(":\n");

            for(Dish dish: pair.getValue()) {
                if(!dish.isDeleted()) {
                    resultText.append("\t");
                    resultText.append(dish.getName());
                    resultText.append("\n");
                }
            }
            for(Dish dish: pair.getValue()) {
                if(dish.isDeleted()) {
                    resultText.append("\t");
                    resultText.append("deleted\t");
                    resultText.append(dish.getName());
                    resultText.append("\n");
                }
            }

            resultText.append("\n");
        }

        menuTextArea.setText(resultText.toString());
    }

    private JPanel createSouthPanel(ActionListener startButtonListener, ActionListener stopButtonListener) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(700, 100));
        resultPanel.setOpaque(false);

        connectionsTextArea = createTextArea(Color.decode("0x187DAA"));
        JScrollPane scrollPane = new JScrollPane(connectionsTextArea);
        setPrefMaxMinSizes(scrollPane, new Dimension(350, 100));
        startButton = SwingHelper.createSimpleButton(
                "<html><b><font size=+2><center>START<br>SERVER"
                , startButtonListener, new Dimension(150, 100));
        exitButton = SwingHelper.createSimpleButton(
                "EXIT", stopButtonListener, new Dimension(120, 70));
        exitButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        exitButton.setEnabled(false);

        resultPanel.add(scrollPane);
        resultPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        resultPanel.add(startButton);
        resultPanel.add(Box.createHorizontalGlue());
        resultPanel.add(exitButton);

        return resultPanel;
    }

    public void updateConnectionsTextArea(String text) {
        connectionsTextArea.append(text + "\n");
    }

    private JLabel createLabel(String text) {
        JLabel resultLabel = new JLabel(text);
        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        setPrefMaxMinSizes(resultLabel, new Dimension(700, 30));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return resultLabel;
    }

    public void setStartButtonNotEnable() {
        startButton.setEnabled(false);
//        exitButton.setEnabled(true);
    }
}
