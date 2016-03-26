package restaurant.client.view.customcomponents;

import java.awt.*;

/**
 * Created by Аркадий on 26.03.2016.
 */
public class NotificationButton extends ImageButton {
    private Image notificationImage;
    private boolean notificationOn = false;

    public NotificationButton(int width, int height, Image standardImage, Image notificationImage) {
        super(width, height, standardImage);
        this.notificationImage = notificationImage;
    }

    public boolean isNotificationOn() {
        return notificationOn;
    }

    public void setNotificationOn(boolean notificationOn) {
        this.notificationOn = notificationOn;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!notificationOn) {
            g.drawImage(image, 0, 0, this);
        } else {
            g.drawImage(notificationImage, 0, 0, this);
        }
    }
}
