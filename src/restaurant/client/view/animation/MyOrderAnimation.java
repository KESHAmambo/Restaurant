package restaurant.client.view.animation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ������� on 21.03.2016.
 */
public class MyOrderAnimation implements Runnable {
    private final int ONE_STEP = 12;
    private final int MAX_WIDTH = 768;
    private final int MIN_WIDTH = 0;
    private final int MAX_HEIGHT = 550;
    private static boolean slideToLeft = true;
    private JPanel cardPanel;
    private JPanel myOrderPanel;
    private JPanel boxPanel;

    public MyOrderAnimation(JPanel boxPanel, JPanel cardPanel, JPanel myOrderPanel) {
        this.cardPanel = cardPanel;
        this.myOrderPanel = myOrderPanel;
        this.boxPanel = boxPanel;
    }

    public static boolean isSlideToLeft() {
        return slideToLeft;
    }

    @Override
    public void run() {
        try {
            slidePanels();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        slideToLeft = !slideToLeft;
    }

    private void slidePanels() throws InterruptedException {
        while(true) {
            Dimension cardDim = cardPanel.getPreferredSize();
            Dimension myOrderDim = myOrderPanel.getPreferredSize();

            if(slideToLeft) {
                if (cardDim.getWidth() <= MIN_WIDTH) break;
                cardDim.setSize(cardDim.getWidth() - ONE_STEP, MAX_HEIGHT);
                myOrderDim.setSize(myOrderDim.getWidth() + ONE_STEP, MAX_HEIGHT);
            } else {
                if (cardDim.getWidth() >= MAX_WIDTH) break;
                cardDim.setSize(cardDim.getWidth() + ONE_STEP, MAX_HEIGHT);
                myOrderDim.setSize(myOrderDim.getWidth() - ONE_STEP, MAX_HEIGHT);
            }

            setPrefMaxMinSize(cardPanel, cardDim);
            setPrefMaxMinSize(myOrderPanel, myOrderDim);

            boxPanel.revalidate();
            boxPanel.repaint();
            Thread.sleep(2);
        }
    }

    private void setPrefMaxMinSize(JComponent component, Dimension cardDim) {
        component.setPreferredSize(cardDim);
        component.setMaximumSize(cardDim);
        component.setMinimumSize(cardDim);
    }
}
