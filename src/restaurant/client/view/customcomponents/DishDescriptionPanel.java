package restaurant.client.view.customcomponents;

import restaurant.kitchen.Dish;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 22.03.2016.
 */
public class DishDescriptionPanel extends JPanel {
    private final int HEIGHT = 100;

    public DishDescriptionPanel(Dish dish, int width) {
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
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.PAGE_AXIS));
        setPrefMaxMinSize(descriptionPanel, new Dimension(388, 100));
        descriptionPanel.setOpaque(false);

        JTextArea nameArea = createNameArea(dish);
        JTextArea shortInfoArea = createShortInfoArea(dish);
        descriptionPanel.add(nameArea);
        descriptionPanel.add(shortInfoArea);
        return descriptionPanel;
    }

    private JLabel createPriceLabel(double price) {
        JLabel priceLabel = new JLabel();
//        priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        setPrefMaxMinSize(priceLabel, new Dimension(70, 100));
        priceLabel.setText("$" + price);
        priceLabel.setForeground(Color.decode("0xAFAFAF"));
        priceLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        return priceLabel;
    }

    private JTextArea createShortInfoArea(Dish dish) {
        JTextArea shortInfoArea = new JTextArea(dish.getShortDescription());
        shortInfoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        shortInfoArea.setRows(2);
        shortInfoArea.setLineWrap(true);
        shortInfoArea.setWrapStyleWord(true);
        shortInfoArea.setForeground(Color.decode("0xAFAFAF"));
        shortInfoArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        setPrefMaxMinSize(shortInfoArea, new Dimension(388, 64));
        shortInfoArea.setOpaque(false);
        shortInfoArea.setEditable(false);
        return shortInfoArea;
    }

    private JTextArea createNameArea(Dish dish) {
        JTextArea nameArea = new JTextArea(dish.getName());
        nameArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nameArea.setRows(1);
        nameArea.setForeground(Color.decode("0xF0F0F0"));
        nameArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        setPrefMaxMinSize(nameArea, new Dimension(388, 36));
        nameArea.setOpaque(false);
        nameArea.setEditable(false);
        return nameArea;
    }

    private static void setPrefMaxMinSize(Component component, Dimension dimension) {
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
    }
}
