package restaurant.cook.model;

import restaurant.common.Order;

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
