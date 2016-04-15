package restaurant.kitchen;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Аркадий on 31.01.2016.
 */
public class Order implements Serializable {
    private ArrayList<Dish> dishes = new ArrayList<>();
    private int tableNumber;
    private String waiter;
    private String cook;
    private String clientName;

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

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

    public Map<Dish, Integer> getDifferentDishesCount() {
        Map<Dish, Integer> resultMap = new HashMap<>();
        for(Dish dish: dishes) {
            if(resultMap.containsKey(dish)) {
                resultMap.put(dish, resultMap.get(dish) + 1);
            } else {
                resultMap.put(dish, 1);
            }
        }
        return resultMap;
    }
}
