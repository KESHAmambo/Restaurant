package restaurant.administrator.model;

/**
 * Created by Аркадий on 09.04.2016.
 */
public enum QueryType {
    // for statisticsPanel:
    DISHES,
    TABLES,
    WAITERS,
    COOKS,
    ORDERS,

    // for infographicsPanel:
    DISHES_BY_DAY,
    DISHES_BY_MONTH,
    INCOME_BY_DAY,
    INCOME_BY_MONTH,
    ORDERS_BY_DAY,
    ORDERS_BY_MONTH;
}
