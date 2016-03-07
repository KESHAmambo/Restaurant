package restaurant.statistic.event;

import java.util.Date;

/**
 * Created by Аркадий on 03.02.2016.
 */
public interface EventDataRow {
    EventType getType();
    Date getDate();
    int getTime();
}
