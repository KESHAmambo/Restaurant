package restaurant.administrator.view;

import restaurant.administrator.model.AdminModel;

public class TestView {
    public static void main(String[] args) {
        new AdminView(null, new AdminModel()).initView();
    }
}
