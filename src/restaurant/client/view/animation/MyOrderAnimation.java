package restaurant.client.view.animation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 21.03.2016.
 */
public class MyOrderAnimation implements Runnable {
    private final int ONE_STEP = 8;
    private final int MAX_WIDTH = 768;
    private final int MIN_WIDTH = 0;
    private final int MAX_HEIGHT = 550;
    private static boolean myOrderPanelSlideToLeft = true;
    private JPanel cardPanel;
    private JPanel myOrderPanel;
    private JPanel boxPanel;

    public MyOrderAnimation(JPanel boxPanel, JPanel cardPanel, JPanel myOrderPanel) {
        this.cardPanel = cardPanel;
        this.myOrderPanel = myOrderPanel;
        this.boxPanel = boxPanel;
    }

    public static boolean isMyOrderPanelSlideToLeft() {
        return myOrderPanelSlideToLeft;
    }

    @Override
    public void run() {
        try {
            if(myOrderPanelSlideToLeft) {
                while(true) {
                    Dimension cardDim = cardPanel.getPreferredSize();
                    if (cardDim.getWidth() <= MIN_WIDTH) break;
                    cardDim.setSize(cardDim.getWidth() - ONE_STEP, MAX_HEIGHT);
                    cardPanel.setPreferredSize(cardDim);
                    cardPanel.setMaximumSize(cardDim);
                    cardPanel.setMinimumSize(cardDim);

                    Dimension myOrderDim = myOrderPanel.getPreferredSize();
                    myOrderDim.setSize(myOrderDim.getWidth() + ONE_STEP, MAX_HEIGHT);
                    myOrderPanel.setPreferredSize(myOrderDim);
                    myOrderPanel.setMaximumSize(myOrderDim);
                    myOrderPanel.setMinimumSize(myOrderDim);

                    boxPanel.revalidate();
                    boxPanel.repaint();
                    Thread.sleep(1);
                }
            } else {
                while(true) {
                    Dimension cardDim = cardPanel.getPreferredSize();
                    if (cardDim.getWidth() >= MAX_WIDTH) break;
                    cardDim.setSize(cardDim.getWidth() + ONE_STEP, MAX_HEIGHT);
                    cardPanel.setPreferredSize(cardDim);
                    cardPanel.setMaximumSize(cardDim);
                    cardPanel.setMinimumSize(cardDim);

                    Dimension myOrderDim = myOrderPanel.getPreferredSize();
                    myOrderDim.setSize(myOrderDim.getWidth() - ONE_STEP, MAX_HEIGHT);
                    myOrderPanel.setPreferredSize(myOrderDim);
                    myOrderPanel.setMaximumSize(myOrderDim);
                    myOrderPanel.setMinimumSize(myOrderDim);

                    boxPanel.revalidate();
                    boxPanel.repaint();
                    Thread.sleep(1);
                }
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        myOrderPanelSlideToLeft = !myOrderPanelSlideToLeft;
    }
}
