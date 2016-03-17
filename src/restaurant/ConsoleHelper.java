package restaurant;

import restaurant.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Аркадий on 31.01.2016.
 */
public class ConsoleHelper {
    private static  BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException
    {
        List<Dish> l = new ArrayList<>();
        String str;
        writeMessage(null);
        str = readString();
        while (!"exit".equals(str))
        {
            str = readString();
        }
        return l;
    }
}
