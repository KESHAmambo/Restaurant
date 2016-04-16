package restaurant.kitchen;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Аркадий on 31.01.2016.
 */
public class Order implements Serializable {
    private ArrayList<Dish> dishes = new ArrayList<>();
    private String waiterName;
    private String cookName;
    private String clientName;
    private int tableNumber;
    private Date receivedTime;
    private Date startedCookTime;
    private Date readyTime;

    public long getReceivedTime() {
        return receivedTime.getTime();
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getStartedCookTime() {
        return startedCookTime.getTime();
    }

    public void setStartedCookTime(Date startedCookTime) {
        this.startedCookTime = startedCookTime;
    }

    public long getReadyTime() {
        return readyTime.getTime();
    }

    public void setReadyTime(Date readyTime) {
        this.readyTime = readyTime;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public List<Dish> getDishes() {
        return dishes;
    }

    public String getCookName() {
        return cookName;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        Iterator<Dish> iterator = dishes.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().getName().equals(dish.getName())) {
                iterator.remove();
                break;
            }
        }
    }

    public Set<Dish> getDifferentDishes() {
        return new HashSet<>(dishes);
    }

    public Map<Dish, Integer> getDifferentDishesCount() {
        Map<Dish, Integer> resultMap = new HashMap<>();
        for(Dish dish: dishes) {
            if(resultMap.containsKey(dish)) {
                resultMap.put(dish, resultMap.get(dish) + 1);
            } else {
                resultMap.put(dish, 1);
            }
        }
        return resultMap;
    }

    @Override
    public String toString() {
        return "Order{" +
                "waiterName='" + waiterName + '\'' +
                ", cookName='" + cookName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", receivedTime=" + receivedTime +
                ", startedCookTime=" + startedCookTime +
                ", readyTime=" + readyTime +
                '}';
    }
}
