package restaurant.common;

import java.io.IOException;

/**
 * Created by Аркадий on 21.04.2016.
 */
public class UnexpectedMessageException extends Exception {
    public UnexpectedMessageException(Message message) {
        super(message.toString());
    }
}
