package restaurant.kitchen;

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

    /**
     * @param dishName
     * @param deleted
     * @return dish, which deleted field was changed,
     * null if dish was't found
     */
    public Dish setDeletedAndGetDishByName(String dishName, boolean deleted) {
        for(List<Dish> dishes: store.values()) {
            for(Dish dish: dishes) {
                if(dish.getName().equals(dishName)) {
                    dish.setDeleted(deleted);
                    return dish;
                }
            }
        }
        return null;
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

    public Dish getDishByTypeAndName(String type, String name) {
            for(Dish dish: store.get(type)) {
                if(dish.getName().equals(name)) {
                    return dish;
                }
            }
        return null;
    }

    public void addDish(Dish dish) {
        store.get(dish.getType()).add(dish);
    }

    @Override
    public Menu clone() throws CloneNotSupportedException {
        Menu newMenu = new Menu();
        for(Map.Entry<String, List<Dish>> pair: store.entrySet()) {
            for(Dish dish: pair.getValue()) {
                newMenu.addDish(dish);
            }
        }
        return newMenu;
    }

    public boolean removeDish(Dish newDish) {
        for(Map.Entry<String, List<Dish>> pair: store.entrySet()) {
            if(pair.getValue().remove(newDish)) {
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
