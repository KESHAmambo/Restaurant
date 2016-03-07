package restaurant.kitchen;


import restaurant.ConsoleHelper;
import restaurant.statistic.StatisticEventManager;
import restaurant.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Аркадий on 01.02.2016.
 */
public class Cook extends Observable implements Runnable {
    private String name;
    private boolean busy;
    private LinkedBlockingQueue<Order> queue;

    public Cook(String name) {
        this.name = name;
    }

    public synchronized void startCookingOrder(Order order) {
        busy = true;
        ConsoleHelper.writeMessage(String.format("Start cooking - %s, cooking time %dmin",
                order, order.getTotalCookingTime()));
        try {
            Thread.sleep(order.getTotalCookingTime() * 10);
        } catch (InterruptedException ignore) {}
        StatisticEventManager.getInstance().register(new CookedOrderEventDataRow(order.getTablet().toString(), name,
                order.getTotalCookingTime() * 60, order.getDishes()));
        setChanged();
        notifyObservers(order);
        busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Order order = queue.poll();
                if (order != null) {
                    startCookingOrder(order);
                } else {
                    Thread.sleep(10);
                }
            }
        } catch(InterruptedException ignore) {}
    }

    @Override
    public String toString() {
        return name;
    }

}
