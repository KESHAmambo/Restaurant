package restaurant.administrator.view;

import restaurant.administrator.model.AdminModel;

/**
 * Created by Аркадий on 31.03.2016.
 */
public class TestView {
    public static void main(String[] args) {
        new AdminView(null, new AdminModel()).initView();
    }
}
