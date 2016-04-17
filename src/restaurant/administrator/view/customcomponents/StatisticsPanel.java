package restaurant.administrator.view.customcomponents;

import restaurant.administrator.model.QueryType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static restaurant.kitchen.SwingHelper.*;

/**
 * Created by Аркадий on 09.04.2016.
 */
public class StatisticsPanel extends QueryPanel {
    private final JTextArea textArea;

    public StatisticsPanel(ActionListener buttonListener) {
        super(buttonListener,
                QueryType.DISHES, QueryType.ORDERS,
                QueryType.COOKS, QueryType.WAITERS,
                QueryType.TABLES);

        textArea = createTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        setPrefMaxMinSizes(scrollPane, new Dimension(700, 380));

        add(scrollPane);
    }
    public void updateText(String text) {
        textArea.setText(text);
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setForeground(Color.DARK_GRAY);
        textArea.setFont(new Font("monospaced", Font.BOLD, 14));
        textArea.setWrapStyleWord(true);
        return textArea;
    }
}
