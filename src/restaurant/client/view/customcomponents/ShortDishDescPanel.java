package restaurant.client.view.customcomponents;

import restaurant.kitchen.Dish;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 22.03.2016.
 */
public class ShortDishDescPanel extends JPanel {
    private final int HEIGHT = 100;

    public ShortDishDescPanel(Dish dish, int width) {
        setName(dish.getName());
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPrefMaxMinSize(this, new Dimension(width, HEIGHT));
        setBackground(Color.decode("0x232323"));
        setBorder(BorderFactory.createLineBorder(Color.decode("0x2F2F2F")));

        JPanel descriptionPanel = createDescriptionPanel(dish);
        JLabel priceLabel = createPriceLabel(dish.getPrice());

        add(descriptionPanel);
        add(priceLabel);
    }

    private JPanel createDescriptionPanel(Dish dish) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        setPrefMaxMinSize(resultPanel, new Dimension(388, 100));
        resultPanel.setOpaque(false);

        JTextArea nameArea = createNameArea(dish);
        JTextArea shortInfoArea = createShortInfoArea(dish);
        resultPanel.add(nameArea);
        resultPanel.add(shortInfoArea);
        return resultPanel;
    }

    private JLabel createPriceLabel(double price) {
        JLabel resultLabel = new JLabel();
        setPrefMaxMinSize(resultLabel, new Dimension(70, 100));
        resultLabel.setText("$" + price);
        resultLabel.setForeground(Color.decode("0xAFAFAF"));
        resultLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        return resultLabel;
    }

    private JTextArea createShortInfoArea(Dish dish) {
        JTextArea resultTextArea = new JTextArea(dish.getShortDesc());
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultTextArea.setRows(2);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setForeground(Color.decode("0xAFAFAF"));
        resultTextArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        setPrefMaxMinSize(resultTextArea, new Dimension(388, 64));
        resultTextArea.setOpaque(false);
        resultTextArea.setEditable(false);
        return resultTextArea;
    }

    private JTextArea createNameArea(Dish dish) {
        JTextArea resultTextArea = new JTextArea(dish.getName());
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultTextArea.setRows(1);
        resultTextArea.setForeground(Color.decode("0xF0F0F0"));
        resultTextArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        setPrefMaxMinSize(resultTextArea, new Dimension(388, 36));
        resultTextArea.setOpaque(false);
        resultTextArea.setEditable(false);
        return resultTextArea;
    }

    private static void setPrefMaxMinSize(Component component, Dimension dimension) {
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
    }
}
