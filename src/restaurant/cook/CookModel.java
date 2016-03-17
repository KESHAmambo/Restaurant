package restaurant.cook;

import restaurant.kitchen.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Аркадий on 14.03.2016.
 */
public class CookModel {
    private Order currentOrder;

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
