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
    private static BlockingQueue<Connection> waiters = new LinkedBlockingQueue<>();
    private static Map<String, Connection> clientsNameToConnectionLinks = Collections.synchronizedMap(new HashMap<String, Connection>());
    private static List<String> actorsNames = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String[] args) {
        String address = ConsoleHelper.readString();
        int serverPort = ConsoleHelper.readInt();
        try(ServerSocket serverSocket = new ServerSocket(serverPort, 50, InetAddress.getByName(address))){
            ConsoleHelper.writeMessage("Server is started.");
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

    public static BlockingQueue<Connection> getWaiters() {
        return waiters;
    }

    public static Map<String, Connection> getClientsNameToConnectionLinks() {
        return clientsNameToConnectionLinks;
    }

    public static List<String> getActorsNames() {
        return actorsNames;
    }

    public static void showWarningMessage(String s) {
        ConsoleHelper.writeMessage(s);
        //TODO
    }

    public static void addOrderToCooksStatisticsBase(Order order, String actorName) {
        //TODO
    }

    public static void addOrderToClientsStatisticsBase(Order order, String actorName) {
        //TODO
    }

    public static void showNewConnectionMessage(String s) {
        ConsoleHelper.writeMessage(s);
        //TODO
    }
}
