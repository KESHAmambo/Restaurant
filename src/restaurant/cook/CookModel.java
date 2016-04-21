package restaurant.cook;

import restaurant.common.Order;

/**
 * Created by Аркадий on 14.03.2016.
 */
public class CookModel {
    private Order currentOrder;

    public Order getCurrentOrder() {
        return currentOrder;
    }

    void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
