package restaurant.waiter.view;

import restaurant.waiter.WaiterModel;

/**
 * Created by Аркадий on 18.03.2016.
 */
public class TestView {
    public static void main(String[] args) {
        new WaiterView(null, new WaiterModel()).initView();
    }
}
