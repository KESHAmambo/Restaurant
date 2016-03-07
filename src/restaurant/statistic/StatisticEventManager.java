package restaurant.statistic;

import restaurant.statistic.event.CookedOrderEventDataRow;
import restaurant.statistic.event.EventDataRow;
import restaurant.statistic.event.EventType;
import restaurant.statistic.event.VideoSelectedEventDataRow;

import java.util.*;

/**
 * Created by Аркадий on 03.02.2016.
 */
public class StatisticEventManager {
    private static StatisticEventManager instance = new StatisticEventManager();
    private static StatisticStorage statisticStorage = getInstance().new StatisticStorage();

    public static StatisticEventManager getInstance() {
        return instance;
    }

    private StatisticEventManager() {}

    public void register(EventDataRow data) {
        if(data != null) {
            statisticStorage.put(data);
        }
    }

    public Map<Date, Long> getDateAdvertisementProfit() {
        Map<Date, Long> result = new TreeMap<>(Collections.reverseOrder());
        for(EventDataRow eventDataRow: statisticStorage.getMap().get(EventType.SELECTED_VIDEOS)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(eventDataRow.getDate());
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            Long profit = ((VideoSelectedEventDataRow) eventDataRow).getAmount();
            Date date = calendar.getTime();
            if(!result.containsKey(date)) {
                result.put(date, profit);
            } else {
                result.put(date, result.get(date) + profit);
            }
        }
        return result;
    }

    public Map<Date, Map<String, Integer>> getDateCookTime() {
        Map<Date, Map<String, Integer>> result = new TreeMap<>(Collections.reverseOrder());
        for(EventDataRow eventDataRow: statisticStorage.getMap().get(EventType.COOKED_ORDER)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(eventDataRow.getDate());
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            Date date = calendar.getTime();
            if(!result.containsKey(date)) {
                Map<String, Integer> map = new TreeMap<>();
                CookedOrderEventDataRow coed = (CookedOrderEventDataRow) eventDataRow;
                map.put(coed.getCookName(), coed.getTime());
                result.put(date, map);
            } else {
                Map<String, Integer> map = result.get(date);
                CookedOrderEventDataRow coed = (CookedOrderEventDataRow) eventDataRow;
                String cookName = coed.getCookName();
                if(!map.containsKey(cookName)) {
                    map.put(cookName, coed.getTime());
                } else {
                    map.put(cookName, map.get(cookName) + coed.getTime());
                }
            }
        }
        return result;
    }

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> map = new HashMap<>();

        public StatisticStorage() {
            for(EventType eventType: EventType.values()) {
                map.put(eventType, new ArrayList<EventDataRow>());
            }
        }

        private void put(EventDataRow data) {
            map.get(data.getType()).add(data);
        }

        public Map<EventType, List<EventDataRow>> getMap() {
            return map;
        }
    }
}
