package restaurant.administrator.model;

import restaurant.kitchen.Dish;
import restaurant.kitchen.Menu;
import restaurant.kitchen.Order;

import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            return new Menu();
        }
    }

    public boolean changeDishStatus(String dishName, boolean deleted) {
        Dish dish = oldMenu.setDeletedAndGetDishByName(dishName, deleted);
        if(dish != null) {
            statusChangedDishes.remove(dish);
            statusChangedDishes.add(dish);
            return true;
        } else {
            return setDeletedInNewAddedDishes(dishName, deleted);
        }
    }

    /**
     * @param dishName
     * @param deleted
     * @return true if newDishes contained dish with dishName
     * and deleted field was changed; false, if it didn't contain
     * such dish.
     */
    private boolean setDeletedInNewAddedDishes(String dishName, boolean deleted) {
        for(Dish dish: needImageDishes) {
            if(dish.getName().equals(dishName)) {
                dish.setDeleted(deleted);
                return true;
            }
        }
        for(Dish dish: notNeedImageDishes) {
            if(dish.getName().equals(dishName)) {
                dish.setDeleted(deleted);
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if dish is new and was added to newMenu,
     * false if dish is already exists and was edited
     */
    public boolean addOrEditDish(
            String type, String name, String shortDesc,
            String fullDesc, String imagePath, double price) {
        Dish existedDish = oldMenu.getDishByTypeAndName(type, name);
        if(existedDish != null) {
            processExistedDish(existedDish, shortDesc, fullDesc, imagePath, price);
            return false;
        } else {
            return processNewDish(type, name, shortDesc, fullDesc, imagePath, price);
        }
        //TODO
    }

    /**
     * @param type
     * @param name
     * @param shortDesc
     * @param fullDesc
     * @param imagePath
     * @param price
     * @return true if dish wasn't added earlier,
     * false in another case
     */
    private boolean processNewDish(
            String type, String name, String shortDesc,
            String fullDesc, String imagePath, double price) {
        Dish newDish = new Dish(type, name, fullDesc, shortDesc, imagePath, price);

        boolean alreadyAdded = newMenu.removeDish(newDish);
        if(alreadyAdded) {
            notNeedImageDishes.remove(newDish);
            needImageDishes.remove(newDish);
        }

        newMenu.addDish(newDish);
        if("no image".equals(imagePath)) {
            notNeedImageDishes.add(newDish);
        } else {
            needImageDishes.add(newDish);
        }

        return !alreadyAdded;
    }

    private void processExistedDish(Dish existedDish, String shortDesc, String fullDesc, String imagePath, double price) {
        existedDish.setFullDesc(fullDesc);
        existedDish.setShortDesc(shortDesc);
        existedDish.setPrice(price);

        if("no image".equals(imagePath)) {
            notNeedImageDishes.add(existedDish);
        } else {
            existedDish.setImagePath(imagePath);
            needImageDishes.add(existedDish);
        }

        newMenu.removeDish(existedDish);
        newMenu.addDish(existedDish);
    }

    public void serializeNewMenu() {
        updateOldDishesInNewMenu();
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

    private void updateOldDishesInNewMenu() {
        for(Dish changedDish: statusChangedDishes) {
            Dish dish = newMenu.getDishByTypeAndName(
                    changedDish.getType(), changedDish.getName());
            dish.setDeleted(changedDish.isDeleted());
        }
    }
    public void writeOrderToDatabase(Order order) {
        dbManager.write(order);
    }

    public void close() {
        dbManager.close();
    }

    public String processQuery(QueryType queryType, String name, java.util.Date fromDate, java.util.Date toDate) {
        return dbManager.processQuery(queryType, name,
                new Date(fromDate.getTime()), new Date(toDate.getTime()));
    }
}
