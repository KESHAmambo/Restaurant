package restaurant.client.view.customcomponents;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ImagePanel extends JPanel {
    private Image image;
    private int x;
    private int y;

    public ImagePanel(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, x, y, this);
    }
}
