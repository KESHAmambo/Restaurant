package restaurant.client.view.animation;

import restaurant.client.view.customcomponents.DishPanelForMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 29.03.2016.
 */
public class FullDescAnimation implements Runnable {
    private final int ONE_STEP = 5;
    private final int WIDTH = 588;
    private final int MIN_HEIGHT = 100;
    private final int MAX_HEIGHT = 300;
    private boolean openFullDesc = true;
    private JPanel menuPanel;
    private DishPanelForMenu dishPanel;

    public FullDescAnimation(JPanel menuPanel, DishPanelForMenu dishPanel) {
        this.menuPanel = menuPanel;
        this.dishPanel = dishPanel;
    }

    @Override
    public void run() {
        try {
            openOrCloseDesc();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        openFullDesc = !openFullDesc;
    }

    private void openOrCloseDesc() throws InterruptedException {
        while(true) {
            Dimension dishDim = dishPanel.getPreferredSize();
            if(openFullDesc) {
                if (dishDim.getHeight() >= MAX_HEIGHT) break;
                dishDim.setSize(WIDTH, dishDim.getHeight() + ONE_STEP);
            } else {
                if(dishDim.getHeight() <= MIN_HEIGHT) break;
                dishDim.setSize(WIDTH, dishDim.getHeight() - ONE_STEP);
            }
            setPrefMaxMinSize(dishPanel, dishDim);
            changeMenuHeight();
            Thread.sleep(2);
        }
    }

    private void changeMenuHeight() {
        Dimension menuDim =  menuPanel.getPreferredSize();
        int heightChange;
        if(openFullDesc) {
            heightChange = ONE_STEP;
        } else {
            heightChange = -ONE_STEP;
        }
        menuDim.setSize(WIDTH, menuDim.getHeight() + heightChange);
        setPrefMaxMinSize(menuPanel, menuDim);
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void setPrefMaxMinSize(JComponent component, Dimension cardDim) {
        component.setPreferredSize(cardDim);
        component.setMaximumSize(cardDim);
        component.setMinimumSize(cardDim);
    }
}
