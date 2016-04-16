package restaurant.client.view.customcomponents;

import restaurant.kitchen.Dish;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 29.03.2016.
 */
public class DishPanelForMenu extends JPanel {
    private final int WIDTH = 588;
    private final int HEIGHT = 100;
    private Dish dish;

    public DishPanelForMenu(Dish dish) {
        this.dish = dish;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPrefMaxMinSize(this, new Dimension(WIDTH, HEIGHT));
    }

    public void collect(ShortDishDescPanel shortDescriptionPanel) {
        JPanel fullDescriptionPanel = createFullDescriptionPanel(dish);

        add(shortDescriptionPanel);
        add(fullDescriptionPanel);
        revalidate();
        repaint();
    }

    private JPanel createFullDescriptionPanel(Dish dish) {
        JPanel resultPanel = new ImagePanel(
                new ImageIcon(dish.getImagePath()).getImage(),
                WIDTH - 290, 0);
        resultPanel.setName("fullDescriptionPanel");
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        resultPanel.setBackground(Color.decode("0x242424"));
        setPrefMaxMinSize(resultPanel, new Dimension(WIDTH, 200));
        resultPanel.setBorder(BorderFactory.createLineBorder(Color.decode("0x2F2F2F")));

        JTextArea descriptionTextArea = createDescriptionTextArea(dish);

        resultPanel.add(descriptionTextArea);
        return resultPanel;
    }

    private JTextArea createDescriptionTextArea(Dish dish) {
        JTextArea resultTextArea = new JTextArea(dish.getFullDesc());
        setPrefMaxMinSize(resultTextArea, new Dimension(WIDTH / 2, 200));
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultTextArea.setOpaque(false);
        resultTextArea.setRows(10);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setForeground(Color.decode("0xAFAFAF"));
        resultTextArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        resultTextArea.setEditable(false);
        return resultTextArea;
    }

    private static void setPrefMaxMinSize(Component component, Dimension dimension) {
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
    }
}
