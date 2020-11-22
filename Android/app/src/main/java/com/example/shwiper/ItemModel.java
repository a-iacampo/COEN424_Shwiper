package com.example.shwiper;

public class ItemModel {
    private int image;
    private String title, description, location, price;

    public ItemModel() {
    }

    public ItemModel(int image, String title, String price, String location, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.location = location;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice(){
        return price;
    }
}
