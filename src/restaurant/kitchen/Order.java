package restaurant.kitchen;

import restaurant.ConsoleHelper;
import restaurant.Tablet;

import java.io.IOException;
import java.util.List;

/**
 * Created by Аркадий on 31.01.2016.
 */
public class Order {
    private Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        initDishes();
    }

    public int getTotalCookingTime() {
        int result = 0;
        for(Dish dish: dishes) {
            result += dish.getDuration();
        }
        return result;
    }

    public boolean isEmpty() {
        return dishes.isEmpty();
    }

    @Override
    public String toString() {
        if (dishes.isEmpty()) {
            return "";
        } else {
            return String.format("Your order: %s of %s", dishes, tablet);
        }
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public Tablet getTablet() {
        return tablet;
    }

    protected void initDishes() throws IOException {
        dishes = ConsoleHelper.getAllDishesForOrder();
    }
}
