package restaurant.ad;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Аркадий on 05.02.2016.
 */
public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager ourInstance = new StatisticAdvertisementManager();
    private AdvertisementStorage advertisementStorage = AdvertisementStorage.getInstance();

    public static StatisticAdvertisementManager getInstance() {
        return ourInstance;
    }

    private StatisticAdvertisementManager() {
    }

    public Set<Advertisement> getVideoSet() {
        Set<Advertisement> result = new TreeSet<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Advertisement) o1).getName().compareToIgnoreCase(((Advertisement) o2).getName());
            }
        });
        for(Advertisement advertisement: advertisementStorage.list()) {
            result.add(advertisement);
        }
        return result;
    }
}
