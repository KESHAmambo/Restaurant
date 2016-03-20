package restaurant.client.view.customcomponents;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ImageLabel extends JLabel {
    Image image;
    public ImageLabel(Image image){
        this.image = image;
    }

    public ImageLabel(String text, Image image) {
        super(text);
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setText("New TEXT");
        g.drawImage(image, 0, 0, this);
    }
}
