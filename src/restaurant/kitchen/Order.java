package restaurant.kitchen;

import restaurant.ConsoleHelper;
import restaurant.administator.Connection;

import java.io.IOException;
import java.util.*;

/**
 * Created by Аркадий on 31.01.2016.
 */
public class Order {
    private List<Dish> dishes = new ArrayList<>();
    private Connection waiter;
    private int tableNumber;
    private String cook;
    private String clientName;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public List<Dish> getDishes() {
        return dishes;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public Connection getWaiter() {
        return waiter;
    }

    public void setWaiter(Connection waiter) {
        this.waiter = waiter;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        Iterator<Dish> iterator = dishes.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().getName().equals(dish.getName())) {
                iterator.remove();
                break;
            }
        }
    }

    public Set<Dish> getDifferentDishes() {
        return new HashSet<>(dishes);
    }
}
