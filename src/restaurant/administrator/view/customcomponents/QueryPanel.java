package restaurant.administrator.view.customcomponents;

import restaurant.administrator.model.QueryType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static restaurant.kitchen.SwingHelper.createSimpleButton;
import static restaurant.kitchen.SwingHelper.setPrefMaxMinSizes;

/**
 * Created by Аркадий on 16.04.2016.
 */
public class QueryPanel extends JPanel {
    private JComboBox<QueryType> queryBox;
    private JTextField fromDateField;
    private JTextField toDateField;

    public QueryPanel(ActionListener buttonListener, QueryType... queryTypes) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel northPanel = createNorthPanel(buttonListener, queryTypes);

        add(northPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
    }

    public QueryType getSelectedQueryType() {
        return (QueryType) queryBox.getSelectedItem();
    }

    public String getFromDateString() {
        return fromDateField.getText().trim();
    }

    public String getToDateString() {
        return toDateField.getText().trim();
    }

    private JPanel createNorthPanel(ActionListener buttonListener, QueryType... queryTypes) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        resultPanel.setOpaque(false);

        JPanel queryPanel = createQueryPanel(queryTypes);
        fromDateField = createTextField();
        JPanel fromDatePanel = createInputPanel("From date(dd.mm.yyyy):", fromDateField);
        toDateField = createTextField();
        JPanel toDatePanel = createInputPanel("To date(dd.mm.yyyy):", toDateField);
        JButton queryButton = createSimpleButton(
                "EXECUTE QUERY", buttonListener, new Dimension(300, 40));

        resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        resultPanel.add(queryPanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(fromDatePanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(toDatePanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(queryButton);

        return resultPanel;
    }

    private JPanel createQueryPanel(QueryType... queryTypes) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(700, 30));
        resultPanel.setOpaque(false);

        JLabel queryLabel = createLabel(
                "Query:");
        queryBox = createQueryBox(queryTypes);

        resultPanel.add(queryLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        resultPanel.add(queryBox);

        return resultPanel;
    }

    private JComboBox<QueryType> createQueryBox(QueryType... queryTypes) {
        JComboBox<QueryType> resultBox = new JComboBox<>();
        setPrefMaxMinSizes(resultBox, new Dimension(300, 30));
        resultBox.setFont(new Font("Dialog", Font.PLAIN, 15));

        for(QueryType queryType: queryTypes) {
            resultBox.addItem(queryType);
        }

        return resultBox;
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(700, 30));
        resultPanel.setOpaque(false);

        JLabel label = createLabel(labelText);

        resultPanel.add(label);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        resultPanel.add(textField);

        return resultPanel;
    }

    private JTextField createTextField() {
        JTextField nameField = new JTextField();
        setPrefMaxMinSizes(nameField, new Dimension(300, 30));
        nameField.setFont(new Font("Dialog", Font.PLAIN, 15));
        return nameField;
    }

    private JLabel createLabel(String text) {
        JLabel resultLabel = new JLabel(text);
        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
        setPrefMaxMinSizes(resultLabel, new Dimension(170, 30));
        return resultLabel;
    }
}
