package restaurant.administrator;

import restaurant.administrator.handlers.*;
import restaurant.Message;
import restaurant.kitchen.Dish;
import restaurant.kitchen.Order;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class Server {
    private static BlockingQueue<Order> waitingOrders = new LinkedBlockingQueue<>();
    private static BlockingQueue<String> waiters = new LinkedBlockingQueue<>();
    private static Map<String, Connection> clientsLinksFromNameToConnection =
            Collections.synchronizedMap(new HashMap<String, Connection>());
    private static List<String> actorsNames = Collections.synchronizedList(new ArrayList<String>());
    private static Map<String, Connection> waitersLinksFromNameToConnection =
            Collections.synchronizedMap(new HashMap<String, Connection>());

    private static AdminController controller;
    private static List<Dish> needImageDishes;
    private static List<Dish> notNeedImageDishes;
    private static List<Dish> statusChangedDishes;

    private Server() {}

    public static void start(
            AdminController controller, String address, int port,
            List<Dish> needImageDishes, List<Dish> notNeedImageDishes,
            List<Dish> statusChangedDishes)
            throws IOException, ClassNotFoundException {
        Server.controller = controller;
        Server.needImageDishes = needImageDishes;
        Server.notNeedImageDishes = notNeedImageDishes;
        Server.statusChangedDishes = statusChangedDishes;

        try(ServerSocket serverSocket =
                    new ServerSocket(port, 100, InetAddress.getByName(address))) {
            updateConnectionsInfo("Server is started.");
            Executor executor = Executors.newCachedThreadPool();
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    Message handshakeMessage = connection.receive();
                    Handler handler = HandlerFactory.byMessage(handshakeMessage, connection);
                    if(handler != null) {
                        executor.execute(handler);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Dish> getNotNeedImageDishes() {
        return notNeedImageDishes;
    }

    public static List<Dish> getStatusChangedDishes() {
        return statusChangedDishes;
    }

    public static List<Dish> getNeedImageDishes() {
        return needImageDishes;
    }

    public static BlockingQueue<Order> getWaitingOrders() {
        return waitingOrders;
    }

    public static BlockingQueue<String> getWaiters() {
        return waiters;
    }

    public static Map<String, Connection> getWaitersLinksFromNameToConnection() {
        return waitersLinksFromNameToConnection;
    }

    public static Map<String, Connection> getClientsLinksFromNameToConnection() {
        return clientsLinksFromNameToConnection;
    }

    public static List<String> getActorsNames() {
        return actorsNames;
    }

    public static void writeOrderToDatabase(Order order) {
        controller.writeOrderToDatabase(order);
    }

    public static void updateConnectionsInfo(String s) {
        controller.updateConnectionsInfo(s);
    }
}
