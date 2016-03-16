package restaurant;

import restaurant.kitchen.Dish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Владелец on 16.03.2016.
 */
public class Menu {
    private Map<String, List<Dish>> store = new HashMap<>();

    public Map<String, List<Dish>> getStore() {
        return store;
    }

    public void addDishByType(String type, Dish dish) {
        store.get(type).add(dish);
    }

    public List<Dish> getDishesByType(String type) {
        return store.get(type);
    }
}
