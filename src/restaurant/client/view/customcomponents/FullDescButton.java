package restaurant.client.view.customcomponents;

import java.awt.*;

/**
 * Created by Аркадий on 29.03.2016.
 */
public class FullDescButton extends ImageButton {
    private Image openDescImage;

    public FullDescButton(int width, int height, Image closeDescImage, Image openDescImage) {
        super(width, height, closeDescImage);
        this.openDescImage = openDescImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!isSelected()) {
            g.drawImage(image, 0, 0, this);
        } else {
            g.setColor(Color.decode("0x232323"));
            g.fillRect(0, 0, width, height);
            g.drawImage(openDescImage, 0, 0, this);
        }
    }
}
