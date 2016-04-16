package restaurant.administrator.view.customcomponents;

import restaurant.SwingHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static restaurant.SwingHelper.*;

/**
 * Created by Аркадий on 01.04.2016.
 */
public class ChangeDishStatusPanel extends JPanel {
    private JButton changeButton;
    private JTextField textField;

    public ChangeDishStatusPanel(String labelText, String buttonText, ActionListener buttonListener) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel label = createLabel(labelText);
        textField =  createTextField();
        changeButton = SwingHelper.createSimpleButton(
                buttonText, buttonListener, new Dimension(200, 50));

        add(Box.createRigidArea(new Dimension(0, 100)));
        add(label);
        add(Box.createRigidArea(new Dimension(0, 80)));
        add(textField);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(changeButton);
    }

    public JTextField getTextField() {
        return textField;
    }

    private JTextField createTextField() {
        JTextField resultField = new JTextField();
        setPrefMaxMinSizes(resultField, new Dimension(500, 40));
        resultField.setFont(new Font("Dialog", Font.PLAIN, 20));
        return resultField;
    }

    private JLabel createLabel(String labelText) {
        JLabel resultLabel = new JLabel(labelText);
        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 26));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return resultLabel;
    }

    public void setButtonNotEnable() {
        changeButton.setEnabled(false);
    }
}
