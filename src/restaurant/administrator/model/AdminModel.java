package restaurant.administrator.model;

import restaurant.kitchen.Dish;
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
    private Menu oldMenu;
    private Menu newMenu;
    private List<Dish> needImageDishes = new ArrayList<>();
    private List<Dish> notNeedImageDishes = new ArrayList<>();
    private List<Dish> statusChangedDishes = new ArrayList<>();

    public AdminModel() {
        oldMenu = initMenu();
        try {
            newMenu = oldMenu.clone();
            dbManager = new DatabaseManager();
        } catch (CloneNotSupportedException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Dish> getNeedImageDishes() {
        return needImageDishes;
    }

    public List<Dish> getNotNeedImageDishes() {
        return notNeedImageDishes;
    }

    public List<Dish> getStatusChangedDishes() {
        return statusChangedDishes;
    }

    public Menu getNewMenu() {
        return newMenu;
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

    public boolean changeDishStatus(String dishName, boolean deleted) {
        //TODO
        return false;
    }

    /**
     * @return true if dish is new and was added to newMenu,
     * false if dish is already exists and was edited
     */
    public boolean addOrEditDish(
            String type, String name, String shortDesc,
            String fullDesc, String imagePath, boolean needImage, double price) {
        //TODO
        return false;
    }

    public void serializeMenu() {
        try {
            FileOutputStream fileOS = new FileOutputStream(MENU_PATH);
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);
            objectOS.writeObject(newMenu);
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
