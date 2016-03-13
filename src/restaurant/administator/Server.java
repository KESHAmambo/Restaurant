package restaurant.administator;

import restaurant.administator.handlers.*;
import restaurant.Message;
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
    private static BlockingQueue<Order> readyOrders = new LinkedBlockingQueue<>();
    private static BlockingQueue<Connection> waiters = new LinkedBlockingQueue<>();
    private static Map<String, Connection> clientNameToConnectionLinks = Collections.synchronizedMap(new HashMap<String, Connection>());
    private static List<String> actorNames = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String[] args) {
        try {
            String address = ConsoleHelper.readString();
            int serverPort = ConsoleHelper.readInt();
            InetAddress inetAddress = InetAddress.getByName(address);
            ServerSocket serverSocket = new ServerSocket(serverPort, 50, inetAddress);
            ConsoleHelper.writeMessage("Server is started.");
            Executor executor = Executors.newCachedThreadPool();
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    Message handshakeMessage = connection.receive();
                    Handler handler = HandlerFactory.newHandler(handshakeMessage, connection);
                    if(handler != null) {
                        executor.execute(handler);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BlockingQueue<Order> getWaitingOrders() {
        return waitingOrders;
    }

    public static BlockingQueue<Order> getReadyOrders() {
        return readyOrders;
    }

    public static BlockingQueue<Connection> getWaiters() {
        return waiters;
    }

    public static Map<String, Connection> getClientNameToConnectionLinks() {
        return clientNameToConnectionLinks;
    }

    public static List<String> getActorNames() {
        return actorNames;
    }

    public static void showWarningMessage(String s) {
        //TODO
    }

    public static void addOrderToCooksStatisticBase(Order order, String actorName) {
        //TODO
    }

    public static void addOrderToClientStatisticsBase(Order order, String actorName) {
        //TODO
    }

}
