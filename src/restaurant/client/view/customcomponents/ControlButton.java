package restaurant.client.view.customcomponents;

import javax.swing.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ControlButton extends ImageButton {
    public ControlButton(String name, int width, int height) {
        super(name);
        image = new ImageIcon(
                "src/restaurant/client/view/resources/controlbuttons/" + name + ".jpg").getImage();
        setProperties(width, height);
    }
}
