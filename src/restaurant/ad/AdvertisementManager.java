package restaurant.ad;

import restaurant.ConsoleHelper;
import restaurant.statistic.StatisticEventManager;
import restaurant.statistic.event.VideoSelectedEventDataRow;

import java.util.*;

/**
 * Created by Аркадий on 01.02.2016.
 */
public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();

    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        if(storage.list().isEmpty()) {
            //StatisticEventManager.getInstance().register(new NoAvailableVideoEventDataRow(timeSeconds));
            throw new NoVideoAvailableException();
        }
        List<List<Advertisement>> allVariants = powerLists(storage.list());
        List<Advertisement> bestVariant = getBestVariant(allVariants);

        Collections.sort(bestVariant, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                int result = Long.compare(o1.getAmountPerOneDisplaying(), o2.getAmountPerOneDisplaying());
                if(result != 0) {
                    return -result;
                }

                long secondCost1 = o1.getAmountPerOneDisplaying() * 1000 / o1.getDuration();
                long secondCost2 = o2.getAmountPerOneDisplaying() * 1000 / o2.getDuration();
                return Long.compare(secondCost1, secondCost2);
            }
        });


        int timeLeft = timeSeconds;
        for(Advertisement advertisement: bestVariant) {
            if(advertisement.getDuration() <= timeLeft) {
                timeLeft -= advertisement.getDuration();
                advertisement.revalidate();
                ConsoleHelper.writeMessage(String.format("%s is displaying... %d, %d",
                        advertisement.getName(), advertisement.getAmountPerOneDisplaying(),
                        advertisement.getAmountPerOneDisplaying() * 1000 / advertisement.getDuration()));
            }
        }

        //replaced, was above upper block
        if(!bestVariant.isEmpty()) {
            StatisticEventManager.getInstance().register(new VideoSelectedEventDataRow(bestVariant,
                    getProfit(bestVariant), getTime(bestVariant)));
        }

        if(timeLeft == timeSeconds) {
            //StatisticEventManager.getInstance().register(new NoAvailableVideoEventDataRow(timeSeconds));
            throw new NoVideoAvailableException();
        }
    }

    private <Advetisement> List<List<Advetisement>> powerLists(List<Advetisement> originalSet) {
        //
        originalSet = new ArrayList<>(originalSet);
        Iterator<Advetisement> iterator = originalSet.iterator();
        while(iterator.hasNext()) {
            if(((Advertisement) iterator.next()).getHits() <= 0) {
                iterator.remove();
            }
        }
        List<List<Advetisement>> lists = new ArrayList<List<Advetisement>>();
        if (originalSet.isEmpty()) {
            lists.add(new ArrayList<Advetisement>());
            return lists;
        }
        List<Advetisement> list = new ArrayList<Advetisement>(originalSet);
        Advetisement head = list.get(0);
        List<Advetisement> rest = new ArrayList<Advetisement>(list.subList(1, list.size()));
        for (List<Advetisement> set : powerLists(rest)) {
            List<Advetisement> newSet = new ArrayList<Advetisement>();
            newSet.add(head);
            newSet.addAll(set);
            lists.add(newSet);
            lists.add(set);
        }
        return lists;
    }

    private List<Advertisement> getBestVariant(List<List<Advertisement>> allVariants) {
        List<Advertisement> bestList = new ArrayList<>();
        int bestProfit = 0;
        int bestTime = 0;
        for(List<Advertisement> list: allVariants) {
            boolean hasHits = true;
            for(Advertisement advertisement: list) {
                if(advertisement.getHits() <= 0) {
                    hasHits = false;
                }
            }
            if(!hasHits) {
                continue;
            }

            int time = getTime(list);
            if(bestList.isEmpty()) {
                if(time <= timeSeconds) {
                    bestList = list;
                    bestTime = time;
                    bestProfit = getProfit(list);
                }
            } else {
                if(time <= timeSeconds) {
                    int profit = getProfit(list);
                    if(profit > bestProfit) {
                        bestList = list;
                        bestTime = time;
                        bestProfit = profit;
                    } else if(profit ==  bestProfit) {
                        if(time > bestTime) {
                            bestList = list;
                            bestTime = time;
                        } else if(time == bestTime) {
                            if(list.size() < bestList.size()) {
                                bestList = list;
                            }
                        }
                    }
                }
            }
        }
        return bestList;
    }

    private int getTime(List<Advertisement> list) {
        int time = 0;
        for(Advertisement advertisement: list) {
            time += advertisement.getDuration();
        }
        return time;
    }

    private int getProfit(List<Advertisement> list) {
        int profit = 0;
        for(Advertisement advertisement: list) {
            profit += advertisement.getAmountPerOneDisplaying();
        }
        return profit;
    }

    /*
    private List<List<Advertisement>> getVariants(
            List<List<Advertisement>> allVariants,
            int remainingVideosCount) {
        if (remainingVideosCount == 1) {
            int size = allVariants.size();
            for (Advertisement advertisement : storage.list()) {
                for (int i = 0; i < size; i++) {
                    List<Advertisement> list = new ArrayList<>(allVariants.get(i));
                    if(!list.contains(advertisement)) {
                        list.add(advertisement);
                        allVariants.add(list);
                    }
                }
            }

            for(int i = 0; i < allVariants.size(); i++) {
                List<Advertisement> curList = allVariants.get(i);
                for(int j = i; j < allVariants.size(); j++) {
                    if(i != j) {
                        List<Advertisement> list = allVariants.get(j);
                        if(list.size() == curList.size() && curList.containsAll(list)) {
                            allVariants.remove(j);
                            j--;
                        }
                    }
                }
            }
            return allVariants;
        } else {
            if (allVariants.isEmpty()) {
                for (Advertisement advertisement : storage.list()) {
                    List<Advertisement> list = new ArrayList<>();
                    list.add(advertisement);
                    allVariants.add(list);
                }
                remainingVideosCount--;
                return getVariants(allVariants, remainingVideosCount);
            } else {
                int size = allVariants.size();
                for (Advertisement advertisement : storage.list()) {
                    for (int i = 0; i < size; i++) {
                        List<Advertisement> list = new ArrayList<>(allVariants.get(i));
                        if(!list.contains(advertisement)) {
                            list.add(advertisement);
                            allVariants.add(list);
                        }
                    }
                }
                remainingVideosCount--;
                return getVariants(allVariants, remainingVideosCount);
            }
        }
    }
    */
}
