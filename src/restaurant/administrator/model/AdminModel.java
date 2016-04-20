package restaurant.administrator.model;

import restaurant.kitchen.Menu;
import restaurant.kitchen.Order;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Аркадий on 31.03.2016.
 */
public class AdminModel {
    private final String MENU_PATH = "src/restaurant/administrator/menu.txt";

    private DatabaseManager dbManager;
    private Menu menu;

    public AdminModel() {
        menu = initMenu();
        try {
            dbManager = new DatabaseManager();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    private Menu initMenu() {
        try(FileInputStream fileIS = new FileInputStream(MENU_PATH);
            ObjectInputStream objectIS = new ObjectInputStream(fileIS)) {
            Menu menu = (Menu) objectIS.readObject();
            fileIS.close();
            return menu;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Menu();
        }
    }

    public boolean changeDishDeletedStatus(String dishType, String dishName, boolean deleted) {
        return menu.changeDishDeletedStatus(dishType, dishName, deleted);
    }

    public boolean addOrEditDish(
            String type, String name, String shortDesc, String fullDesc,
            String serverImagePath, double price) {
        return menu.addOrEditDish(
                type, name, shortDesc, fullDesc,
                serverImagePath, price);
    }

    public void serializeMenu() {
        try {
            FileOutputStream fileOS = new FileOutputStream(MENU_PATH);
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);
            objectOS.writeObject(menu);
            objectOS.flush();
            fileOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOrderToDatabase(Order order) {
        dbManager.write(order);
    }

    public String processQuery(QueryType queryType, java.util.Date fromDate, java.util.Date toDate) {
        return dbManager.processQuery(queryType,
                new Date(fromDate.getTime()), new Date(toDate.getTime()));
    }

    public TreeMap<Date, Double> processBarInfographQuery(
            QueryType queryType, java.util.Date fromDate, java.util.Date toDate) {
        return dbManager.processBarInfographQuery(queryType,
                new Date(fromDate.getTime()), new Date(toDate.getTime()));
    }

    public Map<String, Integer> processPieInfographQuery(
            QueryType queryType, java.util.Date fromDate, java.util.Date toDate) {
        return dbManager.processPieInfopraphQuery(queryType,
                new Date(fromDate.getTime()), new Date(toDate.getTime()));
    }

    public void close() {
        dbManager.close();
    }
}
