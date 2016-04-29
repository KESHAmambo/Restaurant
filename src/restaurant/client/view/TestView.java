package restaurant.client.view;

import restaurant.client.model.ClientModel;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class TestView {
    public static void main(String[] args) {
        new ClientView(null, new ClientModel()).initView();
    }
}