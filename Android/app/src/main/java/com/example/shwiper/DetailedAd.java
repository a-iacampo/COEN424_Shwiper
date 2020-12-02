package com.example.shwiper;

public class DetailedAd {
    private String title;
    private String description;
    private String images;
    private String price;
    private String location;
    private String size;

    public DetailedAd() {}

    public DetailedAd(String title, String description, String images, String price, String location, String size) {
        this.title = title;
        this.description = description;
        this.images = images;
        this.price = price;
        this.location = location;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
