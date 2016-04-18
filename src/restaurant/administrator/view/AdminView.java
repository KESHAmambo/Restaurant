package restaurant.administrator.view;

import restaurant.administrator.controller.AdminController;
import restaurant.administrator.model.AdminModel;
import restaurant.administrator.model.QueryType;
import restaurant.administrator.view.customcomponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Аркадий on 31.03.2016.
 */
public class AdminView {
    private final SimpleDateFormat dateFormat;

    private AdminModel model;
    private AdminController controller;

    private JFrame frame;

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private AddDishPanel addDishPanel;
    private ChangeDishStatusPanel deleteDishPanel;
    private ChangeDishStatusPanel restoreDishPanel;
    private MenuPanel menuPanel;
    private StatisticsPanel statisticsPanel;
    private InfographicsPanel infographicsPanel;

    public AdminView(AdminController controller, AdminModel model) {
        this.controller = controller;
        this.model = model;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public JFrame getFrame() {
        return frame;
    }

    public void initView() {
        frame = new JFrame("Brutz");
        frame.setResizable(false);
        frame.setContentPane(this.mainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        addDishPanel = new AddDishPanel(createListenerForAddOrEditButton());
        deleteDishPanel = new ChangeDishStatusPanel(
                "Enter name of dish you want to delete:",
                "DELETE DISH", createListenerForDeleteButton());
        restoreDishPanel = new ChangeDishStatusPanel(
                "Enter name of dish you want to restore:",
                "RESTORE DISH" , createListenerForRestoreButton());
        menuPanel = new MenuPanel(model.getNewMenu(),
                createListenerForStartButton(), createListenerForExitButton());
        statisticsPanel = new StatisticsPanel(createListenerForStatisticsButton());
        infographicsPanel = new InfographicsPanel(createListenerForInfographicsButton());
    }

    private ActionListener createListenerForInfographicsButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    QueryType queryType = infographicsPanel.getSelectedQueryType();
                    Date fromDate = convertDate(infographicsPanel.getFromDateString());
                    Date toDate = convertDate(infographicsPanel.getToDateString());

                    if(queryType == QueryType.DISHES_TYPES) {
                        processPieInfographQuery(queryType, fromDate, toDate);
                    } else {
                        processBarInfographQuery(queryType, fromDate, toDate);
                    }
                } catch (ParseException e1) {
                    showWarningDialog("Invalid date format!");
                }
            }

            private void processPieInfographQuery(QueryType queryType, Date fromDate, Date toDate) {
                Map<String, Integer> data = model.processPieInfographQuery(queryType, fromDate, toDate);
                if (!data.isEmpty()) {
                    infographicsPanel.drawPieChart(data);
                } else {
                    showWarningDialog("No statistics for specified period.");
                }
            }

            private void processBarInfographQuery(QueryType queryType, Date fromDate, Date toDate) {
                TreeMap<java.sql.Date, Double> data = model.processBarInfographQuery(queryType, fromDate, toDate);
                if (!data.isEmpty()) {
                    infographicsPanel.drawBarChart(data);
                } else {
                    showWarningDialog("No statistics for specified period.");
                }
            }
        };
    }

    private ActionListener createListenerForStatisticsButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    QueryType queryType = statisticsPanel.getSelectedQueryType();
                    Date fromDate = convertDate(statisticsPanel.getFromDateString());
                    Date toDate = convertDate(statisticsPanel.getToDateString());

                    String resultText = model.processQuery(queryType, fromDate, toDate);
                    statisticsPanel.updateText(resultText);
                } catch (NumberFormatException e1) {
                    showWarningDialog("Invalid days number! Must be between 0 and 30000.");
                } catch (ParseException e1) {
                    showWarningDialog("Invalid date format!");
                }
            }
        };
    }

    private Date convertDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    private ActionListener createListenerForExitButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int stop = showConfirmDialog("Do you really want to EXIT?");
                if(stop == 0) {
                    controller.stopServer();
                }
            }
        };
    }

    private ActionListener createListenerForStartButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int start = showConfirmDialog("Do you really want to START server?");
                if(start == 0) {
                    controller.startServer();
                    setButtonsEnable(e);
                }
            }

            private void setButtonsEnable(ActionEvent e) {
                menuPanel.setStartButtonNotEnable();
                deleteDishPanel.setButtonNotEnable();
                restoreDishPanel.setButtonNotEnable();
                addDishPanel.setButtonsNotEnable();
            }
        };
    }

    private ActionListener createListenerForDeleteButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMenu(deleteDishPanel.getTextField(), "deleted");
            }
        };
    }

    private ActionListener createListenerForRestoreButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMenu(restoreDishPanel.getTextField(), "restored");
            }
        };
    }

    private void editMenu(JTextField textField, String operationName) {
        String dishName = textField.getText();
        dishName = dishName.trim();
        if(!"".equals(dishName)) {
            boolean statusChanged = false;
            switch(operationName) {
                case "restored":
                    statusChanged = model.changeDishStatus(dishName, false);
                    break;
                case "deleted":
                    statusChanged = model.changeDishStatus(dishName, true);
            }

            showStatusChangedResult(dishName, statusChanged, operationName);
            if(statusChanged) {
                textField.setText("");
                updateMenuPanel();
            }
        }
    }

    public void updateMenuPanel() {
        menuPanel.updateMenuTextArea(model.getNewMenu());
    }

    private void showStatusChangedResult(String dishName, boolean statusChanged, String operationName) {
        if (statusChanged) {
            showInformDialog(String.format("Dish \"%s\" was %s!", dishName, operationName.toUpperCase()));
        } else {
            showWarningDialog("Dish with this name isn't in menu!");
        }
    }

    private ActionListener createListenerForAddOrEditButton() {
        return new ActionListener() {
            JComboBox<String> typesBox = null;
            JTextField nameField = null;
            JTextArea shortDescArea = null;
            JTextArea fullDescArea = null;
            JTextField imagePathField = null;
            JTextField priceField = null;
            JPanel dishImagePanel = null;
            JCheckBox imageCheckBox = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                initComponents();

                String type = (String) typesBox.getSelectedItem();
                String name = nameField.getText().trim();
                String shortDesc = shortDescArea.getText().trim();
                String fullDesc = fullDescArea.getText().trim();
                String imagePath = imagePathField.getText().trim();
                String priceString = priceField.getText().trim();
                boolean needImage = imageCheckBox.isSelected();

                boolean validTexts = checkValidityOfTexts(name, shortDesc, fullDesc, imagePath);
                if(!validTexts) return;

                double price = checkAndGetPrice(priceString);
                if(price == -1) return;

                boolean added = model.addOrEditDish(
                        type, name, shortDesc, fullDesc,
                        imagePath, needImage, price);
                if(added) {
                    showInformDialog(String.format("Dish \"%s\" was successfully added!", name));
                    updateMenuPanel();
                } else {
                    showInformDialog(String.format("Dish \"%s\" was successfully edited!", name));
                }
                clearForm();
            }

            private void initComponents() {
                if(typesBox == null) {
                    typesBox = addDishPanel.getTypesBox();
                }
                if(nameField == null) {
                    nameField = addDishPanel.getNameField();
                }
                if(shortDescArea == null) {
                    shortDescArea = addDishPanel.getShortDescArea();
                }
                if(fullDescArea == null) {
                    fullDescArea = addDishPanel.getFullDescArea();
                }
                if(imagePathField == null) {
                    imagePathField = addDishPanel.getImagePathField();
                }
                if(priceField == null) {
                    priceField = addDishPanel.getPriceField();
                }
                if(dishImagePanel == null) {
                    dishImagePanel = addDishPanel.getDishImagePanel();
                }
                if(imageCheckBox == null) {
                    imageCheckBox = addDishPanel.getImageCheckBox();
                }
            }

            private double checkAndGetPrice(String priceString) {
                try {
                    double price = Double.parseDouble(priceString);
                    if(price <= 0) throw new NumberFormatException();
                    return price;
                } catch (NumberFormatException e) {
                    showWarningDialog("Invalid price!");
                    return -1;
                }
            }

            private void clearForm() {
                nameField.setText("");
                shortDescArea.setText("");
                fullDescArea.setText("");
                imagePathField.setText("");
                priceField.setText("");
                Graphics g = dishImagePanel.getGraphics();
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, AddDishPanel.IMAGE_WIDTH, AddDishPanel.IMAGE_HEIGHT);
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
                    showWarningDialog("Dish name must be between 3 and 34 characters!");
                    return false;
                } else if(shortDesc.length() > 105) {
                    showWarningDialog("Dish short description must be up to 105 characters!");
                    return false;
                } else if(fullDesc.length() > 370) {
                    showWarningDialog("Dish full description must be up to 370 characters!");
                    return false;
                }
                return true;
            }

            private boolean checkRowsNumber(String shortDesc, String fullDesc) {
                if(countSeparators(shortDesc) > 1) {
                    showWarningDialog("Short description must not contain more then 2 rows");
                    return false;
                } else if(countSeparators(fullDesc) > 9) {
                    showWarningDialog("Full description must not contain more then 9 rows");
                    return false;
                }
                return true;
            }

            private int countSeparators(String text) {
                return text.length() - text.replace("\n", "").length();
            }
        };
    }

    //---------------------------------------------------------------------------------------

    public String askServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter server address:",
                "Brutz",
                JOptionPane.QUESTION_MESSAGE);
    }

    public int askServerPort() {
        while(true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Enter server port:",
                    "Brutz",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(port.trim());
            }catch (Exception e) {
                showWarningDialog("Incorrect port was entered, try again.");
            }
        }
    }

    private void showInformDialog(String text) {
        JOptionPane.showMessageDialog(
                frame,
                text,
                "Brutz",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarningDialog(String text) {
        JOptionPane.showMessageDialog(
                frame,
                text,
                "Brutz",
                JOptionPane.ERROR_MESSAGE);
    }

    private int showConfirmDialog(String text) {
        return JOptionPane.showConfirmDialog(
                frame,
                text,
                "Brutz",
                JOptionPane.YES_NO_OPTION);
    }

    public void updateConnectionsInfo(String text) {
        menuPanel.updateConnectionsTextArea(text);
    }
}
