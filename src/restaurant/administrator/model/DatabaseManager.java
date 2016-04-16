package restaurant.administrator.model;

import restaurant.kitchen.Dish;
import restaurant.kitchen.Order;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by Аркадий on 09.04.2016.
 */
public class DatabaseManager {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private final String ERROR_OCCURRED = "Error occurred!";
    private final PreparedStatement commonDishesSelectStmt;
    private final PreparedStatement allDishesSelectStmt;
    private final PreparedStatement commonOrdersSelectStmt;
    private final PreparedStatement allOrdersSelectStmt;
    private final PreparedStatement orderDishesSelectStmt;
    private final PreparedStatement allCooksSelectStmt;
    private final PreparedStatement allWaitersSelectStmt;
    private final PreparedStatement allTablesSelectStmt;
    private Connection connection;
    private final Statement stmt;
    private PreparedStatement orderInsertStmt;
    private PreparedStatement dishInsertStmt;

    public DatabaseManager() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:restaurant", "hr", "hr");

        stmt = connection.createStatement();

        orderInsertStmt = connection.prepareStatement(
                "insert into orders (order_id, table_id, cook_name, waiter_name, received, started_cook, ready) " +
                "values (order_id_seq.nextval, ?, ?, ?, ?, ?, ?)");

        dishInsertStmt = connection.prepareStatement(
                "insert into dishes (order_id, name, price, type) " +
                "values (order_id_seq.currval, ?, ?, ?)");

        // DISHES queryType:

        commonDishesSelectStmt = connection.prepareStatement(
                "select count(d.name), sum(d.price), trunc(avg(d.price), 3) " +
                "from dishes d, orders o " +
                "where d.order_id = o.order_id " +
                "and o.received > ? and o.received < ?"
        );

        allDishesSelectStmt = connection.prepareStatement(
                "select d.name, d.type, count(*), sum(d.price) " +
                "from dishes d, orders o " +
                "where d.order_id = o.order_id " +
                "and o.received > ? and o.received < ? " +
                "group by d.name, d.type " +
                "order by 2, 3 desc, 4 desc"
        );

        // ORDERS queryType:

        commonOrdersSelectStmt = connection.prepareStatement(
                "select count(*), sum(bill), avg(bill), " +
                "  avg(extract (hour from time_waiting) *60*60 + " +
                "    extract (minute from time_waiting) * 60 + " +
                "    extract (second from time_waiting)) " +
                "    time_waiting, " +
                "  avg(extract (hour from time_cooking) *60*60 + " +
                "    extract (minute from time_cooking) * 60 + " +
                "    extract (second from time_cooking)) " +
                "    time_cooking, " +
                "  avg(extract (hour from time_in_queue) *60*60 + " +
                "    extract (minute from time_in_queue) * 60 + " +
                "    extract (second from time_in_queue)) " +
                "    time_in_queue " +
                "from orders o, " +
                "  (select  " +
                "    o1.order_id id1, sum(d.price) bill " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    group by o1.order_id), " +
                "  (select " +
                "    order_id id2, " +
                "    to_timestamp(to_char(ready, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') - " +
                "    to_timestamp(to_char(received, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') " +
                "    time_waiting, " +
                "    to_timestamp(to_char(ready, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') - " +
                "    to_timestamp(to_char(started_cook, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') " +
                "    time_cooking, " +
                "    to_timestamp(to_char(started_cook, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') - " +
                "    to_timestamp(to_char(received, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') " +
                "    time_in_queue " +
                "    from orders) " +
                "where o.order_id = id1 and o.order_id = id2 " +
                "and o.received > ? and o.received < ?"
        );

        allOrdersSelectStmt = connection.prepareStatement(
                "select order_id, bill, received, table_id, cook_name, waiter_name " +
                "from orders, " +
                "  (select order_id id, sum(price) bill " +
                "    from dishes " +
                "    group by order_id) " +
                "where order_id = id " +
                "and received > ? and received < ? " +
                "order by received desc"
        );

        orderDishesSelectStmt = connection.prepareStatement(
                "select name " +
                "from dishes " +
                "where order_id = ?"
        );

        // COOKS queryType:

        allCooksSelectStmt = connection.prepareStatement(
                "select cook_name, sum(dishes_count), sum(bill), " +
                "  trunc(sum(extract (hour from time_cooking) *60*60 + " +
                "  extract (minute from time_cooking)*60 + " +
                "  extract (second from time_cooking))/60, 0) " +
                "from" +
                "  (select order_id, cook_name, " +
                "      to_timestamp(to_char(ready, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') - " +
                "      to_timestamp(to_char(started_cook, 'DD.MM.YYYY:HH24:MI:SS'), 'DD.MM.YYYY:HH24:MI:SS') " +
                "      time_cooking, " +
                "      dishes_count , bill " +
                "    from orders, " +
                "      (select order_id id, count(*) dishes_count, sum(price) bill " +
                "      from dishes " +
                "      group by order_id) " +
                "    where order_id = id" +
                "    and received > ? and received < ?) " +
                "group by cook_name " +
                "order by 3 desc"
        );

        // WAITERS queryType:

        allWaitersSelectStmt = connection.prepareStatement(
                "select o.waiter_name, sum(bill), count(*) " +
                "from orders o, " +
                "  (select o1.order_id id, sum(d.price) bill " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    group by o1.order_id) " +
                "where o.order_id = id " +
                "and o.received > ? and o.received < ? " +
                "group by o.waiter_name " +
                "order by 2 desc"
        );

        // TABLES queryType

        allTablesSelectStmt = connection.prepareStatement(
                "select table_id, count(*), avg(bill) " +
                "from orders, " +
                "  (select o1.order_id id, sum(d.price) bill " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    group by o1.order_id) " +
                "where order_id = id " +
                "and received > ? and received < ? " +
                "group by table_id"
        );
    }

    public void close() {
        close(stmt, orderInsertStmt, dishInsertStmt,
                commonDishesSelectStmt, allDishesSelectStmt,
                commonOrdersSelectStmt, allOrdersSelectStmt, orderDishesSelectStmt,
                allCooksSelectStmt, allWaitersSelectStmt, allTablesSelectStmt);
    }

    private void close(AutoCloseable... autoCloseables) {
        for(AutoCloseable closeable: autoCloseables) {
            if(closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void write(Order order) {
        try {
            writeOrder(order);
            writeDishes(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void writeOrder(Order order) throws SQLException {
        orderInsertStmt.setInt(1, order.getTableNumber());
        orderInsertStmt.setString(2, order.getCookName());
        orderInsertStmt.setString(3, order.getWaiterName());
        orderInsertStmt.setDate(4, new Date(order.getReceivedTime()));
        orderInsertStmt.setDate(5, new Date(order.getStartedCookTime()));
        orderInsertStmt.setDate(6, new Date(order.getReadyTime()));
        orderInsertStmt.execute();
    }

    private void writeDishes(Order order) throws SQLException {
        for(Dish dish: order.getDishes()) {
            dishInsertStmt.setString(1, dish.getName());
            dishInsertStmt.setDouble(2, dish.getPrice());
            dishInsertStmt.setString(3, dish.getType());
            dishInsertStmt.execute();
        }
    }

    public String processQuery(QueryType queryType, String name, Date fromDate, Date toDate) {
        //TODO
        switch(queryType) {
            case DISHES:
                return processDishesQuery(fromDate, toDate);
            case ORDERS:
                return processOrdersQuery(fromDate, toDate);
            case COOKS:
                return processCooksQuery(fromDate, toDate);
            case WAITERS:
                return processWaitersQuery(fromDate, toDate);
            case TABLES:
                return processTablesQuery(fromDate, toDate);
        }
        return "Invalid query type!";
    }

    private String processTablesQuery(Date fromDate, Date toDate) {
        ResultSet allTablesRS = null;
        try {
            allTablesSelectStmt.setDate(1, fromDate);
            allTablesSelectStmt.setDate(2, toDate);
            allTablesRS = allTablesSelectStmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("TABLES from %s to %s:\n\n", fromDate, toDate));
            sb.append("------------------------------------------------------\n");
            sb.append(String.format(
                    "%-20s %10s %15s\n",
                    "TABLE", "ORDERS", "AVERAGE BILL"));
            sb.append("------------------------------------------------------\n");
            while(allTablesRS.next()) {
                sb.append(String.format(
                        "%-20s %10d %15.2f\n",
                        allTablesRS.getString(1), allTablesRS.getInt(2), allTablesRS.getDouble(3)));
            }

            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(allTablesRS);
        }
        return ERROR_OCCURRED;
    }

    private String processWaitersQuery(Date fromDate, Date toDate) {
        ResultSet allWaitersRS = null;
        try {
            allWaitersSelectStmt.setDate(1, fromDate);
            allWaitersSelectStmt.setDate(2, toDate);
            allWaitersRS = allWaitersSelectStmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("WAITERS from %s to %s:\n\n", fromDate, toDate));
            sb.append("------------------------------------------------------\n");
            sb.append(String.format(
                    "%-20s %10s %10s\n",
                    "NAME", "SUM", "ORDERS"));
            sb.append("------------------------------------------------------\n");
            while(allWaitersRS.next()) {
                sb.append(String.format(
                        "%-20s %10.2f %10d\n",
                        allWaitersRS.getString(1), allWaitersRS.getDouble(2), allWaitersRS.getInt(3)));
            }

            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(allWaitersRS);
        }
        return ERROR_OCCURRED;
    }

    private String processCooksQuery(Date fromDate, Date toDate) {
        ResultSet allCooksRS = null;
        try {
            allCooksSelectStmt.setDate(1, fromDate);
            allCooksSelectStmt.setDate(2, toDate);
            allCooksRS = allCooksSelectStmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("COOKS from %s to %s:\n\n", fromDate, toDate));
            sb.append("--------------------------------------------------------------------------------\n");
            sb.append(String.format(
                    "%-20s %10s %10s %10s %10s\n",
                    "NAME", "DISHES", "SUM", "HOURS", "MINUTES"));
            sb.append("--------------------------------------------------------------------------------\n");
            while(allCooksRS.next()) {
                int minutes = allCooksRS.getInt(4);
                sb.append(String.format(
                        "%-20s %10d %10.2f %10d %10d\n",
                        allCooksRS.getString(1), allCooksRS.getInt(2), allCooksRS.getDouble(3),
                        minutes/60, minutes%60));
            }

            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(allCooksRS);
        }
        return ERROR_OCCURRED;
    }

    private String processOrdersQuery(Date fromDate, Date toDate) {
        ResultSet commonRS = null;
        ResultSet allOrdersRS = null;
        try {
            commonOrdersSelectStmt.setDate(1, fromDate);
            commonOrdersSelectStmt.setDate(2, toDate);
            allOrdersSelectStmt.setDate(1, fromDate);
            allOrdersSelectStmt.setDate(2, toDate);
            commonRS = commonOrdersSelectStmt.executeQuery();
            allOrdersRS = allOrdersSelectStmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("ORDERS from %s to %s:\n", fromDate, toDate));
            commonRS.next();
            sb.append(String.format(
                    "%-20s%d\n%-20s%.2f\n%-20s%.2f\n%-20s%.2f sec\n%-20s%.2f sec\n%-20s%.2f sec\n\n",
                    "Count", commonRS.getInt(1),
                    "Sum:" , commonRS.getDouble(2),
                    "Average bill:", commonRS.getDouble(3),
                    "Time waiting:", commonRS.getDouble(4),
                    "Time cooking:", commonRS.getDouble(5),
                    "Time in queue:", commonRS.getDouble(6)));

            while(allOrdersRS.next()) {
                int orderID = allOrdersRS.getInt(1);
                sb.append(String.format(
                        "%-7s %-10s %-20s %-7s %-15s %-15s\n" +
                        "%-7d %-10.2f %-20s %-7d %-15s %-15s\n",
                        "ID", "Bill", "Received time", "Table", "Cook", "Waiter",
                        orderID, allOrdersRS.getDouble(2), dateFormat.format(allOrdersRS.getDate(3)),
                        allOrdersRS.getInt(4), allOrdersRS.getString(5), allOrdersRS.getString(6)));
                appendDishesForOrder(sb, allOrdersRS);
                sb.append('\n');
            }

            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(commonRS, allOrdersRS);
        }
        return ERROR_OCCURRED;
    }

    private void appendDishesForOrder(StringBuilder sb, ResultSet allOrdersRS) throws SQLException {
        orderDishesSelectStmt.setInt(1, allOrdersRS.getInt(1));
        ResultSet orderDishesRS = null;
        try {
            orderDishesRS = orderDishesSelectStmt.executeQuery();
            while(orderDishesRS.next()) {
                sb.append(orderDishesRS.getString(1));
                sb.append('\n');
            }
        } finally {
            close(orderDishesRS);
        }
    }

    private String processDishesQuery(Date fromDate, Date toDate) {
        ResultSet commonRS = null;
        ResultSet allDishesRS = null;
        try {
            commonDishesSelectStmt.setDate(1, fromDate);
            commonDishesSelectStmt.setDate(2, toDate);
            allDishesSelectStmt.setDate(1, fromDate);
            allDishesSelectStmt.setDate(2, toDate);
            commonRS = commonDishesSelectStmt.executeQuery();
            allDishesRS = allDishesSelectStmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("DISHES from %s to %s:\n", fromDate, toDate));
            commonRS.next();
            sb.append(String.format("%-20s%d\n%-20s%.2f\n%-20s%.3f\n\n",
                    "Count:", commonRS.getInt(1),
                    "Sum:", commonRS.getDouble(2),
                    "Average price:", commonRS.getDouble(3)));

            String previousDishType = "";
            String dishType;
            sb.append("--------------------------------------------------------------------------------\n");
            sb.append(String.format("%-34s %-15s %10s %10s\n", "DISH", "TYPE", "COUNT", "SUM"));
            sb.append("--------------------------------------------------------------------------------\n");
            while(allDishesRS.next()) {
                dishType = allDishesRS.getString(2);
                if(!previousDishType.equals(dishType)) {
                    sb.append('\n');
                    previousDishType = dishType;
                }
                sb.append(String.format("%-34s %-15s %10d %10.2f\n",
                        allDishesRS.getString(1), allDishesRS.getString(2),
                        allDishesRS.getInt(3), allDishesRS.getDouble(4)));
            }

            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(commonRS, allDishesRS);
        }
        return ERROR_OCCURRED;
    }
}
