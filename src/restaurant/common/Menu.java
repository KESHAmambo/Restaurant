package restaurant.common;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Владелец on 16.03.2016.
 */
public class Menu implements Serializable, Cloneable {
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

    public List<Dish> getDishesByType(String type) {
        return store.get(type);
    }

    public static List<String> getTypes() {
        List<String> types = new ArrayList<>();
        types.add("burgers");
        types.add("hotDogs");
        types.add("mac");
        types.add("pasta");
        types.add("salads");
        types.add("sandwiches");
        types.add("sides");
        types.add("beverages");
        types.add("coffee");
        types.add("sweets");
        return types;
    }

    public Dish getDish(String type, String name) {
        for(Dish dish: store.get(type)) {
            if(dish.getName().equals(name)) {
                return dish;
            }
        }
        return null;
    }

    public void add(Dish dish) {
        store.get(dish.getType()).add(dish);
    }

    /**
     * @return true if dish is new and was added to newMenu,
     * false if dish is already exists and was edited
     */
    public boolean addOrEditDish(
            String type, String name, String shortDesc, String fullDesc,
            String serverImagePath, double price) {
        boolean alreadyExisted = !removeDish(type, name);
        Dish dish = new Dish(type, name, shortDesc, fullDesc, serverImagePath, price);
        add(dish);
        return alreadyExisted;
    }

    private boolean removeDish(String type, String name) {
        List<Dish> typeDishes = store.get(type);
        for(Dish dish: typeDishes) {
            if(dish.getName().equals(name)) {
                typeDishes.remove(dish);
                return true;
            }
        }
        return false;
    }

    public Dish removeDish(Dish dish) {
        for(Map.Entry<String, List<Dish>> pair: store.entrySet()) {
            if(pair.getValue().remove(dish)) {
                return dish;
            }
        }
        return null;
    }

    public boolean changeDishDeletedStatus(String type, String name, boolean deleted) {
        for(Dish dish: store.get(type)) {
            if(dish.getName().equals(name)) {
                dish.setDeleted(deleted);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, List<Dish>> pair: store.entrySet()) {
            sb.append('\n');
            sb.append(pair.getKey());
            sb.append('\n');
            for(Dish dish: pair.getValue()) {
                sb.append(dish);
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
