package com.example.shwiper;

public class ItemModel {
    private String image;
    private String title, description, location, price, url;

    public ItemModel() {
    }

    public ItemModel(String image, String title, String price, String location, String description, String url) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.location = location;
        this.price = price;
        this.url = url;
    }

    public String getImage() {
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

    public String getUrl(){
        return url;
    }
}
