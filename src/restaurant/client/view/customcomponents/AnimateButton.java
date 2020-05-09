package restaurant.client.view.customcomponents;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Аркадий on 06.04.2016.
 */
public class AnimateButton extends ImageButton {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private final Image staticImage;
    private final Image onActionImage;

    public AnimateButton(int width, int height, Image staticImage, Image onActionImage) {
        super(width, height, staticImage);
        this.staticImage = staticImage;
        this.onActionImage = onActionImage;
    }

    public void startAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                image = onActionImage;
                repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                image = staticImage;
                repaint();
            }
        }).start();
    }
}
