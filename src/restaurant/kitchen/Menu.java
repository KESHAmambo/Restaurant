package restaurant.kitchen;

import java.util.*;

/**
 * Created by Владелец on 16.03.2016.
 */
public class Menu {
    private Map<String, List<Dish>> store = new LinkedHashMap<>();

    public Menu() {
        store.put("burgers", new ArrayList<Dish>());
        store.put("hotDogs", new ArrayList<Dish>());
        store.put("mac", new ArrayList<Dish>());
        store.put("pasta", new ArrayList<Dish>());
        store.put("salads", new ArrayList<Dish>());
        store.put("sandwiches", new ArrayList<Dish>());
        store.put("sides", new ArrayList<Dish>());
        store.put("beverages", new ArrayList<Dish>());
        store.put("coffee", new ArrayList<Dish>());
        store.put("sweets", new ArrayList<Dish>());
    }

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
