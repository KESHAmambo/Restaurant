package restaurant;

import restaurant.ad.Advertisement;
import restaurant.ad.StatisticAdvertisementManager;
import restaurant.statistic.StatisticEventManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Аркадий on 03.02.2016.
 */
public class DirectorTablet {
    public void printAdvertisementProfit() {
        Map<Date, Long> map = StatisticEventManager.getInstance().getDateAdvertisementProfit();
        long totalProfit = 0;
        for(Map.Entry<Date, Long> pair: map.entrySet()) {
            totalProfit += pair.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            ConsoleHelper.writeMessage(String.format(Locale.ENGLISH,
                    "%s - %.2f",sdf.format(pair.getKey()), (double) pair.getValue() / 100));
        }
        ConsoleHelper.writeMessage(String.format(Locale.ENGLISH,
                "Total - %.2f", (double) totalProfit / 100));
    }

    public void printCookWorkloading() {
        Map<Date, Map<String, Integer>> map = StatisticEventManager.getInstance().getDateCookTime();
        for(Map.Entry<Date, Map<String, Integer>> pair: map.entrySet()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            ConsoleHelper.writeMessage(sdf.format(pair.getKey()));
            for(Map.Entry<String, Integer> pair1: pair.getValue().entrySet()) {
                ConsoleHelper.writeMessage(String.format("%s - %d min",
                        pair1.getKey(), (int) Math.ceil(((double) pair1.getValue()) / 60)));
            }
            ConsoleHelper.writeMessage("");
        }
    }

    public void printActiveVideoSet() {
        Set<Advertisement> set = StatisticAdvertisementManager.getInstance().getVideoSet();
        for(Advertisement advertisement: set) {
            if(advertisement.getHits() > 0) {
                ConsoleHelper.writeMessage(advertisement.getName() + " - " + advertisement.getHits());
            }
        }
    }

    public void printArchivedVideoSet() {
        Set<Advertisement> set = StatisticAdvertisementManager.getInstance().getVideoSet();
        for(Advertisement advertisement: set) {
            if(advertisement.getHits() <= 0) {
                ConsoleHelper.writeMessage(advertisement.getName());
            }
        }
    }
}
