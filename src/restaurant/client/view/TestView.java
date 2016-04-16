package restaurant.client.view;

import restaurant.client.ClientModel;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class TestView {
    public static void main(String[] args) {
        new ClientView(null, new ClientModel(null)).initView();
    }
}