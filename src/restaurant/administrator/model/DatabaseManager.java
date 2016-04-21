package restaurant.administrator.model;

import restaurant.common.Dish;
import restaurant.common.Order;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Аркадий on 09.04.2016.
 */
class DatabaseManager {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private final String ERROR_OCCURRED = "Error occurred!";

    private final Connection connection;

    //writing statements
    private final PreparedStatement orderInsertStmt;
    private final PreparedStatement dishInsertStmt;

    // statistics statements
    private final PreparedStatement commonDishesSelectStmt;
    private final PreparedStatement allDishesSelectStmt;
    private final PreparedStatement commonOrdersSelectStmt;
    private final PreparedStatement allOrdersSelectStmt;
    private final PreparedStatement orderDishesSelectStmt;
    private final PreparedStatement allCooksSelectStmt;
    private final PreparedStatement allWaitersSelectStmt;
    private final PreparedStatement allTablesSelectStmt;

    // infographics statements
    private final PreparedStatement incomeByDaySelectStmt;
    private final PreparedStatement incomeByMonthSelectStmt;
    private final PreparedStatement dishesByDaySelectStmt;
    private final PreparedStatement dishesByMonthSelectStmt;
    private final PreparedStatement ordersByDaySelectStmt;
    private final PreparedStatement ordersByMonthSelectStmt;
    private final PreparedStatement dishesTypesSelectStmt;

    DatabaseManager() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:restaurant", "hr", "hr");

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
                "from " +
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

        // INCOME_BY_DAY queryType

        incomeByDaySelectStmt = connection.prepareStatement(
                "select trunc(received, 'DDD'), sum(bill) " +
                "from " +
                "  (select o1.order_id id, received, sum(d.price) bill " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    and received > ? and received < ? " +
                "    group by o1.order_id, received) " +
                "group by trunc(received, 'DDD')"
        );

        // INCOME_BY_MONTH queryType

        incomeByMonthSelectStmt = connection.prepareStatement(
                "select trunc(received, 'MONTH'), sum(bill) " +
                "from " +
                "  (select o1.order_id id, received, sum(d.price) bill " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    and received > ? and received < ? " +
                "    group by o1.order_id, received) " +
                "group by trunc(received, 'MONTH')"
        );

        // DISHES_BY_DAY queryType

        dishesByDaySelectStmt = connection.prepareStatement(
                "select trunc(received, 'DDD'), sum(dishes_number) " +
                "from " +
                "  (select o1.order_id id, received, count(*) dishes_number " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    and received > ? and received < ? " +
                "    group by o1.order_id, received) " +
                "group by trunc(received, 'DDD')"
        );

        // DISHES_BY_MONTH queryType

        dishesByMonthSelectStmt = connection.prepareStatement(
                "select trunc(received, 'MONTH'), sum(dishes_number) " +
                "from " +
                "  (select o1.order_id id, received, count(*) dishes_number " +
                "    from orders o1, dishes d " +
                "    where o1.order_id = d.order_id " +
                "    and received > ? and received < ? " +
                "    group by o1.order_id, received) " +
                "group by trunc(received, 'MONTH')"
        );

        // ORDERS_BY_DAY queryType

        ordersByDaySelectStmt = connection.prepareStatement(
                "select trunc(received, 'DDD'), count(*) " +
                "from orders " +
                "where received > ? and received < ? " +
                "group by trunc(received, 'DDD')"
        );

        // ORDERS_BY_MONTH queryType

        ordersByMonthSelectStmt = connection.prepareStatement(
                "select trunc(received, 'MONTH'), count(*) " +
                "from orders " +
                "where received > ? and received < ? " +
                "group by trunc(received, 'MONTH')"
        );

        // DISHES_TYPES queryType

        dishesTypesSelectStmt = connection.prepareStatement(
                "select d.type, count(*) " +
                "from dishes d, orders o " +
                "where d.order_id = o.order_id " +
                "and o.received > ? and o.received < ? " +
                "group by d.type"
        );
    }

    void close() {
        close(orderInsertStmt, dishInsertStmt,
                commonDishesSelectStmt, allDishesSelectStmt,
                commonOrdersSelectStmt, allOrdersSelectStmt, orderDishesSelectStmt,
                allCooksSelectStmt, allWaitersSelectStmt, allTablesSelectStmt,
                incomeByDaySelectStmt,incomeByMonthSelectStmt,
                dishesByDaySelectStmt, dishesByMonthSelectStmt,
                ordersByDaySelectStmt, ordersByMonthSelectStmt,
                dishesTypesSelectStmt,
                connection);
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

    void write(Order order) {
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

    String processQuery(QueryType queryType, Date fromDate, Date toDate) {
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
                    "Count:", commonRS.getInt(1),
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

    TreeMap<Date, Double> processBarInfographQuery(QueryType queryType, Date fromDate, Date toDate) {
        ResultSet infographRS = null;
        TreeMap<Date, Double> resultMap = new TreeMap<>();
        try {
            infographRS = initInfographRS(queryType, fromDate, toDate);
            while(infographRS.next()) {
                resultMap.put(infographRS.getDate(1), infographRS.getDouble(2));
            }
            complementWithEmptyPeriods(resultMap, queryType, fromDate, toDate);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(infographRS);
        }
        return resultMap;
    }

    Map<String, Integer> processPieInfopraphQuery(QueryType queryType, Date fromDate, Date toDate) {
        ResultSet infographRS = null;
        Map<String, Integer> resultMap = new HashMap<>();
        try {
            infographRS = initInfographRS(queryType, fromDate, toDate);
            while(infographRS.next()) {
                resultMap.put(infographRS.getString(1), infographRS.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(infographRS);
        }
        return resultMap;
    }

    private ResultSet initInfographRS(
            QueryType queryType, Date fromDate, Date toDate)
            throws SQLException {
        switch(queryType) {
            case INCOME_BY_DAY:
                return executeQuery(incomeByDaySelectStmt, fromDate, toDate);
            case INCOME_BY_MONTH:
                return executeQuery(incomeByMonthSelectStmt, fromDate, toDate);
            case DISHES_BY_DAY:
                return executeQuery(dishesByDaySelectStmt, fromDate, toDate);
            case DISHES_BY_MONTH:
                return executeQuery(dishesByMonthSelectStmt, fromDate, toDate);
            case ORDERS_BY_DAY:
                return executeQuery(ordersByDaySelectStmt, fromDate, toDate);
            case ORDERS_BY_MONTH:
                return executeQuery(ordersByMonthSelectStmt, fromDate, toDate);
            case DISHES_TYPES:
                return executeQuery(dishesTypesSelectStmt, fromDate, toDate);
            default:
                throw new SQLException("Invalid queryType");
        }
    }

    private ResultSet executeQuery(PreparedStatement stmt, Date fromDate, Date toDate) throws SQLException {
        setDatesAtFirstPositionsOfStmt(stmt, fromDate, toDate);
        return stmt.executeQuery();
    }

    private void setDatesAtFirstPositionsOfStmt(PreparedStatement stmt, Date fromDate, Date toDate) throws SQLException {
        stmt.setDate(1, fromDate);
        stmt.setDate(2, toDate);
    }

    private void complementWithEmptyPeriods(
            TreeMap<Date, Double> resultMap, QueryType queryType,
            Date fromDate, Date toDate) {
        Calendar toCalendar = new GregorianCalendar();
        toCalendar.setTime(toDate);
        Calendar changingCalendar = new GregorianCalendar();
        changingCalendar.setTime(fromDate);

        switch (queryType) {
            case ORDERS_BY_DAY:
            case DISHES_BY_DAY:
            case INCOME_BY_DAY:
                addMissedPeriods(Calendar.DATE, resultMap,
                        toCalendar, changingCalendar);
                break;
            case ORDERS_BY_MONTH:
            case DISHES_BY_MONTH:
            case INCOME_BY_MONTH:
                changingCalendar.set(Calendar.DATE, 1);
                addMissedPeriods(Calendar.MONTH, resultMap,
                        toCalendar, changingCalendar);
                break;
        }
    }

    private void addMissedPeriods(
            int period, TreeMap<Date, Double> resultMap,
            Calendar toCalendar, Calendar changingCalendar) {
        while(changingCalendar.compareTo(toCalendar) < 0) {
            Date date = new Date(changingCalendar.getTime().getTime());
            if(!resultMap.containsKey(date)) {
                resultMap.put(date, 0d);
            }
            changingCalendar.add(period, 1);
        }
    }
}
