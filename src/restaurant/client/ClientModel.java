package restaurant.client;

import restaurant.kitchen.Dish;
import restaurant.kitchen.Menu;
import restaurant.kitchen.Order;

import java.io.*;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientModel {
    private final String MENU_PATH = "src/restaurant/client/menu.txt";
    private final String DISH_IMAGES_PATH = "src/restaurant/client/view/resources/dishimages/";

    private ClientController controller;

    private Menu menu;
    private Order order;
    private int tableNumber;
    private String currentClientName;
    private double currentBill;
    private double finalBill;

    public ClientModel(ClientController controller) {
        this.controller = controller;

        try(FileInputStream fileIS = new FileInputStream(MENU_PATH);
            ObjectInputStream objectIS = new ObjectInputStream(fileIS)) {
            menu = (Menu) objectIS.readObject();
            fileIS.close();
        } catch (IOException | ClassNotFoundException e) {
            menu = new Menu();
        }
    }

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

    public void addDishToOrder(Dish dish) {
        if(order == null){
            order = new Order();
            order.setClientName(currentClientName);
        }
        order.addDish(dish);
    }

    public void setOrderEmpty() {
        Order emptyOrder = new Order();
        emptyOrder.setClientName(getCurrentClientName());
        setOrder(emptyOrder);
    }

    public void changeDishStatus(Dish dish) {
        Dish changingDish = menu.getDishByTypeAndName(
                dish.getType(), dish.getName());
        changingDish.setDeleted(dish.isDeleted());
    }

    public void processAddedOrEditedDish(Dish dish, boolean needImage) throws IOException {
        boolean alreadyExisted = processAsExistedDish(dish, needImage);
        if(!alreadyExisted) {
            processAsNewDish(dish, needImage);
        }
    }

    private boolean processAsExistedDish(Dish dish, boolean needImage) throws IOException {
        Dish existedDish = menu.getDishByTypeAndName(
                dish.getType(), dish.getName());
        if(existedDish != null) {
            existedDish.setFullDesc(dish.getFullDesc());
            existedDish.setShortDesc(dish.getShortDesc());
            existedDish.setPrice(dish.getPrice());
            if(needImage) {
                String imagePath = dish.getImagePath();
                String imageName = subImageName(imagePath);
                imagePath = DISH_IMAGES_PATH + imageName;
                existedDish.setImagePath(imagePath);
                controller.downloadImage(imagePath);
            }
            return true;
        }
        return false;
    }

    private String subImageName(String imagePath) {
        String imageName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
        imageName = imageName.substring(imageName.lastIndexOf('\\') + 1);
        return imageName;
    }

    private void processAsNewDish(Dish dish, boolean needImage) throws IOException {
        menu.addDish(dish);
        if(needImage) {
            String imagePath = dish.getImagePath();
            String imageName = subImageName(imagePath);
            dish.setImagePath(DISH_IMAGES_PATH + imageName);
            controller.downloadImage(dish.getImagePath());
        }
    }

    public void serializeMenu() {
        try {
            FileOutputStream fileOS = new FileOutputStream(MENU_PATH);
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);
            objectOS.writeObject(menu);
            objectOS.flush();
            fileOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
