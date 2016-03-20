package restaurant.client.view.customcomponents;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ImageButton extends JButton {
    protected Image image;

    public ImageButton(int width, int height, Image image){
        this.image = image;
        setProperties(width, height);
    }

    public ImageButton(String text) {
        super(text);
        setName(text);
    }

    protected void setProperties(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
