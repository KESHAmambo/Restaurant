package restaurant.administator.handlers;

import restaurant.administator.Connection;

/**
 * Created by Аркадий on 13.03.2016.
 */
public abstract class Handler implements Runnable {
    protected final Connection connection;
    protected final String actorName;
    public Handler(Connection connection, String actorName) {
        this.connection = connection;
        this.actorName = actorName;
    }
}
