package restaurant.administrator.view;

import restaurant.administrator.AdminController;
import restaurant.administrator.model.AdminModel;
import restaurant.administrator.model.QueryType;
import restaurant.administrator.view.customcomponents.AddDishPanel;
import restaurant.administrator.view.customcomponents.ChangeDishStatusPanel;
import restaurant.administrator.view.customcomponents.MenuPanel;
import restaurant.administrator.view.customcomponents.StatisticsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        addDishPanel = new AddDishPanel(this);
        deleteDishPanel = new ChangeDishStatusPanel(
                "Enter name of dish you want to delete:",
                "DELETE DISH", createListenerForDeleteButton());
        restoreDishPanel = new ChangeDishStatusPanel(
                "Enter name of dish you want to restore:",
                "RESTORE DISH" , createListenerForRestoreButton());
        menuPanel = new MenuPanel(model.getNewMenu(),
                createListenerForStartButton(), createListenerForExitButton());
        statisticsPanel = new StatisticsPanel(createListenerForQueryButton());
    }

    private ActionListener createListenerForQueryButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JComboBox<QueryType> queryBox = statisticsPanel.getQueryBox();
                    JTextField nameField = statisticsPanel.getNameField();
                    JTextField fromDateField = statisticsPanel.getFromDateField();
                    JTextField toDateField = statisticsPanel.getToDateField();

                    QueryType queryType = (QueryType) queryBox.getSelectedItem();
                    String name = nameField.getText().trim();
                    Date fromDate = convertDate(fromDateField.getText().trim());
                    Date toDate = convertDate(toDateField.getText().trim());

                    String resultText = model.processQuery(queryType, name, fromDate, toDate);
                    statisticsPanel.updateText(resultText);
                } catch (NumberFormatException e1) {
                    showWarningDialog("Invalid days number! Must be between 0 and 30000.");
                } catch (ParseException e1) {
                    showWarningDialog("Invalid date format!");
                }

            }

            private Date convertDate(String date) throws ParseException {
                return dateFormat.parse(date);
            }
        };
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
            showInformDialog("Dish with this name isn't in menu!");
        }
    }

    public boolean addOrEditDish(
            String type, String name, String shortDesc,
            String fullDesc, String imagePath, double price) {
        return model.addOrEditDish(type, name, shortDesc, fullDesc, imagePath, price);
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
