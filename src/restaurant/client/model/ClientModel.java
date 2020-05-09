package restaurant.client.model;

import restaurant.common.Dish;
import restaurant.common.Menu;
import restaurant.common.Order;

import java.io.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientModel {
    private final String MENU_PATH = "src/restaurant/client/model/menu.txt";

    private Menu menu;
    private Order order;
    private int tableNumber;
    private String currentClientName;
    private double currentBill;
    private double finalBill;

    public ClientModel() {
        try(FileInputStream fileIS = new FileInputStream(MENU_PATH);
            ObjectInputStream objectIS = new ObjectInputStream(fileIS)) {
            menu = (Menu) objectIS.readObject();
            fileIS.close();
        } catch (IOException | ClassNotFoundException e) {
            menu = new Menu();
        }
    }

    public double getFinalBill() {
        return finalBill;
    }

    public void setFinalBill(double finalBill) {
        this.finalBill = finalBill;
    }

    public double getCurrentBill() {
        return currentBill;
    }

    public void setCurrentBill(double currentBill) {
        this.currentBill = currentBill;
        if(currentBill < 0) {
            this.currentBill = 0;
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCurrentClientName() {
        return currentClientName;
    }

    public void setCurrentClientName(String currentClientName) {
        this.currentClientName = currentClientName;
    }

    public void addDishToOrder(Dish dish) {
        if(order == null){
            order = new Order();
            order.setClientName(currentClientName);
        }
        order.addDish(dish);
    }

    public void setOrderEmpty() {
        Order emptyOrder = new Order();
        emptyOrder.setClientName(getCurrentClientName());
        setOrder(emptyOrder);
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
}
