package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Аркадий on 01.04.2016.
 */
public class SwingHelper {
    private SwingHelper() {}

    public static void setPrefMaxMinSizes(Component component, Dimension preferredSize) {
        component.setPreferredSize(preferredSize);
        component.setMaximumSize(preferredSize);
        component.setMinimumSize(preferredSize);
    }

    public static JButton createSimpleButton(
            String buttonText, ActionListener buttonListener, Dimension dimension) {
        JButton resultButton = new JButton(buttonText);
        resultButton.setFont(new Font("Dialog", Font.BOLD, 20));
        setPrefMaxMinSizes(resultButton, dimension);
        resultButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultButton.addActionListener(buttonListener);
        return resultButton;
    }
}
