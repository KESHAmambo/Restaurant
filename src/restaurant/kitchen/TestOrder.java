package restaurant.kitchen;

import restaurant.Tablet;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Аркадий on 05.02.2016.
 */
public class TestOrder extends Order {
    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    protected void initDishes() {
        dishes = new ArrayList<>();
        Dish[] array = Dish.values();
        for(int i = 0; i < 5; i++) {
            dishes.add(array[(int) (Math.random() * array.length)]);
        }
    }
}
