package restaurant;

/**
 * Created by Аркадий on 13.03.2016.
 */
public enum MessageType {
    COOK_CONNECTION,
    WAITER_CONNECTION,
    CLIENT_CONNECTION,
    NAME_REQUEST,
    ACTOR_NAME,
    NAME_ACCEPTED,
    ORDER,
    NEW_CLIENT,
    ORDER_IS_READY,
    END_MEAL,
    TEXT,
    PING,
    WARNING;
}
