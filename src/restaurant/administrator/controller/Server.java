package restaurant.administrator.controller;

import restaurant.common.*;
import restaurant.administrator.controller.handlers.Handler;
import restaurant.administrator.controller.handlers.HandlerFactory;

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
    private static Menu menu;

    private Server() {}

    public static void start(AdminController controller, String address, int port, Menu menu)
            throws IOException, ClassNotFoundException {
        Server.controller = controller;
        Server.menu = menu;

        try(ServerSocket serverSocket =
                    new ServerSocket(port, 100, InetAddress.getByName(address))) {
            updateConnectionsInfo("Server is started.");
            Executor executor = Executors.newCachedThreadPool();
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    Message connectionTypeMessage = connection.receive();
                    Handler handler = HandlerFactory.byMessage(connectionTypeMessage, connection);
                    executor.execute(handler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Menu getMenu() {
        return menu;
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
