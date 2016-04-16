package restaurant.client.view.customcomponents;

import restaurant.client.view.customcomponents.ImageButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class TypeButton extends ImageButton {
    private static final int WIDTH = 255;
    private static final int HEIGHT = 55;
    private Image imageOn;

    public TypeButton(String typeName) {
        setName(typeName);
        image = new ImageIcon(
                "src/restaurant/client/view/resources/typebuttons/" + typeName + ".jpg").getImage();
        imageOn = new ImageIcon(
                "src/restaurant/client/view/resources/typebuttons/" + typeName + "On.jpg").getImage();
        setProperties(WIDTH, HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!isSelected()) {
            g.drawImage(image, 0, 0, this);
        } else {
            g.drawImage(imageOn, 0, 0, this);
        }
    }
}
