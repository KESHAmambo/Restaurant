package restaurant.administrator.view.customcomponents;

import restaurant.administrator.model.QueryType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static restaurant.SwingHelper.*;

/**
 * Created by Аркадий on 09.04.2016.
 */
public class StatisticsPanel extends JPanel {
    private final JTextArea textArea;
    private JComboBox<QueryType> queryBox;
    private JTextField fromDateField;
    private JTextField nameField;
    private JTextField toDateField;

    public StatisticsPanel(ActionListener statisticsButtonListener) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel northPanel = createNorthPanel(statisticsButtonListener);
        textArea = createTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        setPrefMaxMinSizes(scrollPane, new Dimension(700, 340));

        add(northPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(scrollPane);
    }

    public JComboBox<QueryType> getQueryBox() {
        return queryBox;
    }

    public JTextField getFromDateField() {
        return fromDateField;
    }

    public JTextField getToDateField() {
        return toDateField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    private JPanel createNorthPanel(ActionListener statisticsButtonListener) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));

        JPanel queryPanel = createQueryPanel();
        nameField = createTextField();
        JPanel namePanel = createInputPanel("Name(for single choice):", nameField);
        fromDateField = createTextField();
        JPanel fromDatePanel = createInputPanel("From date(dd.mm.yyyy):", fromDateField);
        toDateField = createTextField();
        JPanel toDatePanel = createInputPanel("To date(dd.mm.yyyy):", toDateField);
        JButton queryButton = createSimpleButton(
                "EXECUTE QUERY", statisticsButtonListener, new Dimension(300, 40));

        resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        resultPanel.add(queryPanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(namePanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(fromDatePanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(toDatePanel);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(queryButton);

        return resultPanel;
    }

    private JPanel createQueryPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(700, 30));

        JLabel queryLabel = createLabel(
                "Query:");
        queryBox = createQueryBox();

        resultPanel.add(queryLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        resultPanel.add(queryBox);

        return resultPanel;
    }

    private JComboBox<QueryType> createQueryBox() {
        JComboBox<QueryType> resultBox = new JComboBox<>();
        setPrefMaxMinSizes(resultBox, new Dimension(300, 30));
        resultBox.setFont(new Font("Dialog", Font.PLAIN, 15));

        for(QueryType queryType: QueryType.values()) {
            resultBox.addItem(queryType);
        }

        return resultBox;
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.LINE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(700, 30));

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

    public void updateText(String text) {
        textArea.setText(text);
        //TODO: write resultText into textArea
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
