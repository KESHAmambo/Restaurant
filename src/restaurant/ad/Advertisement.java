package restaurant.ad;

/**
 * Created by Аркадий on 01.02.2016.
 */
public class Advertisement {
    private Object content;
    private String name;
    private long initialAmount;
    private int hits;
    private int duration;
    private long amountPerOneDisplaying;
    private long balance;

    public Advertisement(Object content, String name, long initialAmount, int hits, int duration) {
        this.content = content;
        this.name = name;
        this.initialAmount = initialAmount;
        this.hits = hits;
        this.duration = duration;
        amountPerOneDisplaying = initialAmount / hits;
        balance = initialAmount;
    }

    public String getName() {
        return name;
    }

    public int getHits() {
        return hits;
    }

    public int getDuration() {
        return duration;
    }

    public long getAmountPerOneDisplaying() {
        return amountPerOneDisplaying;
    }

    public void revalidate() {
        amountPerOneDisplaying = balance / hits;
        balance -= amountPerOneDisplaying;
        if(hits <= 0) {
            throw new UnsupportedOperationException();
        }
        hits--;
    }
}
