package restaurant.client;

/**
 * Created by Аркадий on 20.03.2016.
 */
public class ClientModel {
    private int tableNumber;
    private String currentClientName;

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCurrentClientName() {
        return currentClientName;
    }

    public void setCurrentClientName(String currentClientName) {
        this.currentClientName = currentClientName;
    }
}
