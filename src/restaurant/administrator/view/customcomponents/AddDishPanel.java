package restaurant.administrator.view.customcomponents;

import restaurant.SwingHelper;
import restaurant.administrator.view.AdminView;
import restaurant.client.view.customcomponents.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static restaurant.SwingHelper.*;

/**
 * Created by Аркадий on 01.04.2016.
 */
public class AddDishPanel extends JPanel {
    private final int MAX_WIDTH = 800;
    private final int MAX_HEIGHT = 600;
    private final int TEXT_WIDTH = MAX_WIDTH / 2 - 30;
    private final int IMAGE_WIDTH = 290;
    private final int IMAGE_HEIGHT = 200;

    private final AdminView adminView;

    private JButton showImageButton;
    private JButton addOrEditDishButton;
    private JTextField imagePathField;
    private ImagePanel dishImagePanel;
    private JTextField nameField;
    private JTextArea shortDescArea;
    private JTextArea fullDescArea;
    private JComboBox<String> typesBox;
    private JTextField priceField;

    public AddDishPanel(AdminView adminView) {
        this.adminView = adminView;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        add(leftPanel);
        add(rightPanel);
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

        dishImagePanel = createDishImagePanel();
        components.add(dishImagePanel);

        return components;
    }

    private ImagePanel createDishImagePanel() {
        ImagePanel resultPanel = new ImagePanel(null, 0, 0);
        setPrefMaxMinSizes(resultPanel, new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        resultPanel.setBackground(Color.GRAY);
        return resultPanel;
    }

    private JPanel createLeftPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        setPrefMaxMinSizes(resultPanel, new Dimension(MAX_WIDTH / 2, MAX_HEIGHT));
        resultPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        resultPanel.setBackground(Color.decode("0x4DED8A"));

        List<Component> components = createComponentsForLeftPanel();

        addComponents(resultPanel, components);

        return resultPanel;
    }

    private List<Component> createComponentsForLeftPanel() {
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
                createListenerForAddButton(), new Dimension(200, 50));
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

    private ActionListener createListenerForAddButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) typesBox.getSelectedItem();
                String name = nameField.getText().trim();
                String shortDesc = shortDescArea.getText().trim();
                String fullDesc = fullDescArea.getText().trim();
                String imagePath = imagePathField.getText().trim();
                String priceString = priceField.getText().trim();

                boolean validTexts = checkValidityOfTexts(name, shortDesc, fullDesc, imagePath);
                if(!validTexts) return;

                double price = checkAndGetPrice(priceString);
                if(price == -1) return;

                boolean added = adminView.addOrEditDish(type, name, shortDesc, fullDesc, imagePath, price);
                if(added) {
                    showSuccessDialog(name, "added");
                    adminView.updateMenuPanel();
                } else {
                    showSuccessDialog(name, "edited");
                }
                clearForm();
            }

            private double checkAndGetPrice(String priceString) {
                try {
                    double price = Double.parseDouble(priceString);
                    if(price <= 0) throw new NumberFormatException();
                    return price;
                } catch (NumberFormatException e) {
                    showErrorDialog("Invalid price!");
                    return -1;
                }
            }

            private void showSuccessDialog(String name, String operationName) {
                JOptionPane.showMessageDialog(
                        adminView.getFrame(),
                        String.format("Dish \"%s\" was successfully %s!", name, operationName),
                        "Brutz",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            private void showErrorDialog(String text) {
                JOptionPane.showMessageDialog(
                        adminView.getFrame(),
                        text,
                        "Brutz",
                        JOptionPane.ERROR_MESSAGE);
            }

            private void clearForm() {
                nameField.setText("");
                shortDescArea.setText("");
                fullDescArea.setText("");
                imagePathField.setText("");
                priceField.setText("");
                Graphics g = dishImagePanel.getGraphics();
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
            }

            private boolean checkValidityOfTexts(
                    String name, String shortDesc, String fullDesc, String imagePath) {

                boolean validRowsNumber = checkRowsNumber(shortDesc, fullDesc);
                if(!validRowsNumber) {
                    return false;
                }

                boolean validTextsLength = checkTextsLength(name, shortDesc, fullDesc, imagePath);
                if(!validTextsLength) {
                    return false;
                }

                return true;
            }

            private boolean checkTextsLength(String name, String shortDesc, String fullDesc, String imagePath) {
                if(name.length() > 34 || name.length() < 3) {
                    showErrorDialog("Dish name must be between 3 and 34 characters!");
                    return false;
                } else if(shortDesc.length() > 105) {
                    showErrorDialog("Dish short description must be up to 105 characters!");
                    return false;
                } else if(fullDesc.length() > 370) {
                    showErrorDialog("Dish full description must be up to 370 characters!");
                    return false;
                } else if(imagePath.length() < 1) {
                    showErrorDialog("Filepath to image must not be empty.\n" +
                            "Enter \"no image\" if you don't want to change image\n" +
                            "of editing dish, or set any image for new dish.");
                    return false;
                }
                return true;
            }

            private boolean checkRowsNumber(String shortDesc, String fullDesc) {
                if(countSeparators(shortDesc) > 1) {
                    showErrorDialog("Short description must not contain more then 2 rows");
                    return false;
                } else if(countSeparators(fullDesc) > 9) {
                    showErrorDialog("Full description must not contain more then 9 rows");
                    return false;
                }
                return true;
            }

            private int countSeparators(String text) {
                return text.length() - text.replace("\n", "").length();
            }
        };
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
        JTextField nameField = new JTextField();
        setPrefMaxMinSizes(nameField, new Dimension(TEXT_WIDTH, 40));
        nameField.setFont(new Font("Dialog", Font.PLAIN, 20));
        return nameField;
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
