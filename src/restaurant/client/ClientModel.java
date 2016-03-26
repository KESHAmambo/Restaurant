package restaurant.client;

import restaurant.kitchen.Dish;
import restaurant.kitchen.Menu;
import restaurant.kitchen.Order;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientModel {
    private Menu menu;
    private Order order;
    private int tableNumber;
    private String currentClientName;
    private double currentBill;
    private double finalBill;

    public double getFinalBill() {
        return finalBill;
    }

    public void setFinalBill(double finalBill) {
        this.finalBill = finalBill;
    }

    public double getCurrentBill() {
        return currentBill;
    }

    public void setCurrentBill(double currentBill) {
        this.currentBill = currentBill;
        if(currentBill < 0) {
            this.currentBill = 0;
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCurrentClientName() {
        return currentClientName;
    }

    public void setCurrentClientName(String currentClientName) {
        this.currentClientName = currentClientName;
    }

    public Menu getTestMenu() {
        Menu menu = new Menu();
        Dish dish1 = new Dish("burgers", "EXTRA LONG BUTTERY CHEESEBURGER", null,
                "The Extra Long Buttery Cheeseburger features two beef patties " +
                        "topped with freshly cut onions, crisp iceberg lettuce, ketchup",
                null, 1.6);
        Dish dish2 = new Dish("burgers", "BACON & CHEESE WHOPPER® SANDWICH", null,
                "Our Bacon and Cheese WHOPPER® Sandwich is a ¼ lb.* of savory " +
                        "flame-grilled beef topped with thick-cut smoked bacon",
                null, 2);
        Dish dish3 = new Dish("burgers", "EXTRA LONG CHEESEBURGER", null,
                "Our Extra Long Cheeseburger features two beef patties topped with " +
                        "freshly cut onions, crisp iceburg lettuce, ketchup, melted",
                null, 1.5);
        Dish dish4 = new Dish("burgers", "WHOPPER® SANDWICH", null,
                "Our WHOPPER® Sandwich is a ¼ lb* of savory flame-grilled beef " +
                        "topped with juicy tomatoes, fresh lettuce, creamy ",
                null, 2.3);
        Dish dish5 = new Dish("burgers", "TRIPLE WHOPPER® SANDWICH", null,
                "Our TRIPLE WHOPPER® Sandwich boasts three ¼ lb* savory " +
                        "flame-grilled beef patties topped with juicy tomatoes, fresh ",
                null, 3);
        Dish dish6 = new Dish("burgers", "BIG KING™ SANDWICH", null,
                "Our BIG KING™ Sandwich features two savory flame-grilled beef patties" +
                        ", topped with, melted American cheese, fresh cut ",
                null, 3);
        Dish dish7 = new Dish("beverages", "SPRITE®", null,
                "Let Sprite® refresh your day with the great taste of lemon-lime.",
                null, 0.5);
        menu.addDishByType("burgers", dish1);
        menu.addDishByType("burgers", dish2);
        menu.addDishByType("burgers", dish3);
        menu.addDishByType("burgers", dish4);
        menu.addDishByType("burgers", dish5);
        menu.addDishByType("burgers", dish6);
        menu.addDishByType("beverages", dish7);
        return menu;
    }

    public void addDishToOrder(Dish dish) {
        if(order == null){
            order = new Order();
            order.setClientName(currentClientName);
        }
        order.addDish(dish);
    }
}
