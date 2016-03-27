package restaurant.kitchen;


import java.io.Serializable;

/**
 * Created by Eduard 16.03.2016
 */
public class Dish implements Serializable {
    private String type;
    private String name;
    private String fullDescription;
    private String shortDescription;
    private String imagePath;
    private double price = 0;

    //Constructors

    public Dish(String type, String name, String fullDescription,
                String shortDescription, String imagePath, double price) {
        this.type = type;
        this.name = name;
        this.fullDescription = fullDescription;
        this.shortDescription = shortDescription;
        this.imagePath = imagePath;
        this.price = price;
    }

    //Setters

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //Getters

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getPrice() {
        return price;
    }

}
