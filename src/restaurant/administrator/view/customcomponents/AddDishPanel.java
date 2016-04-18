package restaurant.administrator.view.customcomponents;

import restaurant.kitchen.SwingHelper;
import restaurant.client.view.customcomponents.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static restaurant.kitchen.SwingHelper.*;

/**
 * Created by Аркадий on 01.04.2016.
 */
public class AddDishPanel extends JPanel {
    public static final int IMAGE_WIDTH = 290;
    public static final int IMAGE_HEIGHT = 200;
    private final int MAX_WIDTH = 800;
    private final int MAX_HEIGHT = 600;
    private final int TEXT_WIDTH = MAX_WIDTH / 2 - 30;

//    private final AdminView adminView;

    private JButton showImageButton;
    private JButton addOrEditDishButton;
    private JTextField imagePathField;
    private ImagePanel dishImagePanel;
    private JTextField nameField;
    private JTextArea shortDescArea;
    private JTextArea fullDescArea;
    private JComboBox<String> typesBox;
    private JTextField priceField;
    private JCheckBox imageCheckBox;

    public AddDishPanel(ActionListener addOrEditButtonListener) {
//        this.adminView = adminView;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftPanel = createLeftPanel(addOrEditButtonListener);
        JPanel rightPanel = createRightPanel();

        add(leftPanel);
        add(rightPanel);
    }

    public JCheckBox getImageCheckBox() {
        return imageCheckBox;
    }

    public JTextField getImagePathField() {
        return imagePathField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextArea getShortDescArea() {
        return shortDescArea;
    }

    public JTextArea getFullDescArea() {
        return fullDescArea;
    }

    public JComboBox<String> getTypesBox() {
        return typesBox;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public int getImageWidth() {
        return IMAGE_WIDTH;
    }

    public int getImageHeight() {
        return IMAGE_HEIGHT;
    }

    public ImagePanel getDishImagePanel() {
        return dishImagePanel;
    }

    private JPanel createRightPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(MAX_WIDTH / 2, MAX_HEIGHT));
        resultPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        resultPanel.setBackground(Color.decode("0x4AD878"));

        List<Component> components = createComponentsForRightPanel();
        showImageButton = SwingHelper.createSimpleButton("SHOW IMAGE",
                createListenerForShowImageButton(), new Dimension(200, 50));

        addComponents(resultPanel, components);
        resultPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        resultPanel.add(showImageButton);

        return resultPanel;
    }

    private ActionListener createListenerForShowImageButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imagePath = imagePathField.getText();
                imagePath = imagePath.trim();
                if(!"".equals(imagePath)) {
                    updateDishImagePanel(imagePath);
                }
            }

            private void updateDishImagePanel(String imagePath) {
                Image image = new ImageIcon(imagePath).getImage();
                Graphics g = dishImagePanel.getGraphics();
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                g.drawImage(image, 0, 0, dishImagePanel);
            }
        };
    }

    private List<Component> createComponentsForRightPanel() {
        List<Component> components = new ArrayList<>();

        components.add(createLabel("Price:"));
        priceField = createField();
        components.add(priceField);

        components.add(createLabel("Filepath to image:"));
        imagePathField = createField();
        components.add(imagePathField);

        imageCheckBox = createImageCheckBox();
        components.add(imageCheckBox);

        dishImagePanel = createDishImagePanel();
        components.add(dishImagePanel);

        return components;
    }

    private JCheckBox createImageCheckBox() {
        JCheckBox resultCheckBox = new JCheckBox("Set image?");
        setPrefMaxMinSizes(resultCheckBox, new Dimension(200, 40));
        resultCheckBox.setFont(new Font("Dialog", Font.PLAIN, 20));
        resultCheckBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        resultCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        resultCheckBox.setOpaque(false);
        return resultCheckBox;
    }

    private ImagePanel createDishImagePanel() {
        ImagePanel resultPanel = new ImagePanel(null, 0, 0);
        setPrefMaxMinSizes(resultPanel, new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        resultPanel.setBackground(Color.GRAY);
        return resultPanel;
    }

    private JPanel createLeftPanel(ActionListener addOrEditButtonListener) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(MAX_WIDTH / 2, MAX_HEIGHT));
        resultPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        resultPanel.setBackground(Color.decode("0x4DED8A"));

        List<Component> components = createComponentsForLeftPanel(addOrEditButtonListener);

        addComponents(resultPanel, components);

        return resultPanel;
    }

    private List<Component> createComponentsForLeftPanel(ActionListener addOrEditButtonListener) {
        List<Component> components = new ArrayList<>();

        components.add(createLabel("Dish type:"));
        typesBox = createTypesBox();
        components.add(typesBox);

        components.add(createLabel("Dish name:"));
        nameField = createField();
        components.add(nameField);

        components.add(createLabel("Short description:"));
        shortDescArea = createTextArea();
        JScrollPane shortDescScrollPane = new JScrollPane(shortDescArea);
        setPrefMaxMinSizes(shortDescScrollPane, new Dimension(TEXT_WIDTH, 50));
        components.add(shortDescScrollPane);

        components.add(createLabel("Full description"));
        fullDescArea = createTextArea();
        JScrollPane fullDescPane = new JScrollPane(fullDescArea);
        setPrefMaxMinSizes(fullDescPane, new Dimension(TEXT_WIDTH, 150));
        components.add(fullDescPane);

        addOrEditDishButton = SwingHelper.createSimpleButton("ADD/EDIT DISH",
                addOrEditButtonListener, new Dimension(200, 50));
        components.add(addOrEditDishButton);

        return components;
    }

    private JComboBox<String> createTypesBox() {
        JComboBox<String> resultBox = new JComboBox<>();
        resultBox.setFont(new Font("Dialog", Font.PLAIN, 20));
        setPrefMaxMinSizes(resultBox, new Dimension(200, 40));
        resultBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        resultBox.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        resultBox.setOpaque(false);

        for(String type: restaurant.kitchen.Menu.getTypes()) {
            resultBox.addItem(type);
        }

        return resultBox;
    }

    private void addComponents(JPanel panel, List<Component> components) {
        int componentsSize = components.size();
        for(int i = 0; i < componentsSize; i++) {
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(components.get(i++));
            if(componentsSize == i) break;
            panel.add(components.get(i));
        }
    }

    private JTextArea createTextArea() {
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        return resultArea;
    }

    private JTextField createField() {
        JTextField resultField = new JTextField();
        setPrefMaxMinSizes(resultField, new Dimension(TEXT_WIDTH, 40));
        resultField.setFont(new Font("Dialog", Font.PLAIN, 20));
        return resultField;
    }

    private JLabel createLabel(String text) {
        JLabel resultLabel = new JLabel(text);
        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        setPrefMaxMinSizes(resultLabel, new Dimension(200, 30));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        resultLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return resultLabel;
    }

    public void setButtonsNotEnable() {
        addOrEditDishButton.setEnabled(false);
        showImageButton.setEnabled(false);
    }
}
