package restaurant.kitchen;

import restaurant.ConsoleHelper;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Аркадий on 01.02.2016.
 */
public class Waitor implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        ConsoleHelper.writeMessage(arg + " was cooked by " + o);
    }
}
