package restaurant;

import restaurant.kitchen.Cook;
import restaurant.kitchen.Order;
import restaurant.kitchen.Waitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Аркадий on 31.01.2016.
 */
public class Restaurant {
    private static final int ORDER_CREATING_INTERVAL = 100;
    private static final LinkedBlockingQueue<Order> queue = new LinkedBlockingQueue<>();


    public static void main(String[] args) {
        Cook cook1 = new Cook("Amigo");
        cook1.setQueue(queue);
        Cook cook2 = new Cook("Bob");
        cook2.setQueue(queue);
        Waitor waitor = new Waitor();
        cook1.addObserver(waitor);
        cook2.addObserver(waitor);
        Thread cookThread1 = new Thread(cook1);
        Thread cookThread2 = new Thread(cook2);
        cookThread1.start();
        cookThread2.start();

        List<Tablet> tablets = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            Tablet tablet = new Tablet(i);
            tablet.setQueue(queue);
            tablets.add(tablet);
        }

        Thread thread = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {}
        thread.interrupt();

        DirectorTablet directorTablet = new DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();

        /*
        Cook cook1 = new Cook("Amigo");
        Cook cook2 = new Cook("Bob");
        Waitor waitor = new Waitor();
        cook1.addObserver(waitor);
        cook2.addObserver(waitor);
        StatisticEventManager.getInstance().register(cook1);
        StatisticEventManager.getInstance().register(cook2);

        OrderManager orderManager = new OrderManager();
        List<restaurant.Tablet> tablets = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            restaurant.Tablet tablet = new restaurant.Tablet(i);
            tablet.addObserver(orderManager);
            tablets.add(tablet);
        }

        Thread thread = new Thread(new restaurant.RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {}
        thread.interrupt();

        restaurant.DirectorTablet directorTablet = new restaurant.DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
        */

        /*
        Cook cook = new Cook("Amigo");
        Waitor waitor = new Waitor();
        cook.addObserver(waitor);
        restaurant.Tablet tablet = new restaurant.Tablet(5);
        tablet.addObserver(cook);
        tablet.createOrder();
        restaurant.DirectorTablet directorTablet = new restaurant.DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
        */
        /*
        for(int i = 0; i < 3; i++) {
            Cook cook = new Cook("Amigo");
            Waitor waitor = new Waitor();
            cook.addObserver(waitor);
            restaurant.Tablet tablet = new restaurant.Tablet(5);
            tablet.addObserver(cook);
            tablet.createOrder();
            restaurant.DirectorTablet directorTablet = new restaurant.DirectorTablet();
            directorTablet.printAdvertisementProfit();
            directorTablet.printCookWorkloading();
            directorTablet.printActiveVideoSet();
            directorTablet.printArchivedVideoSet();
        }
        */

        /*
        Cook cook = new Cook("Amigo");
        Waitor waitor = new Waitor();
        cook.addObserver(waitor);
        restaurant.Tablet tablet = new restaurant.Tablet(5);
        tablet.addObserver(cook);
        tablet.createOrder();

        Cook cook1 = new Cook("AAA");
        Waitor waitor1 = new Waitor();
        cook1.addObserver(waitor1);
        restaurant.Tablet tablet1 = new restaurant.Tablet(5);
        tablet1.addObserver(cook1);
        tablet1.createOrder();

        tablet.createOrder();

        tablet.createOrder();

        Cook cook2 = new Cook("Ivan");
        Waitor waitor2 = new Waitor();
        cook1.addObserver(waitor2);
        restaurant.Tablet tablet2 = new restaurant.Tablet(5);
        tablet2.addObserver(cook2);
        tablet2.createOrder();

        Cook cook3 = new Cook("Bob");
        Waitor waitor3 = new Waitor();
        cook1.addObserver(waitor3);
        restaurant.Tablet tablet3 = new restaurant.Tablet(5);
        tablet3.addObserver(cook3);
        tablet3.createOrder();

        restaurant.DirectorTablet directorTablet = new restaurant.DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        */
    }
}
