package com.example.shwiper;

public class Ad {
    private String title;
    private String description;
    private String image;
    private String price;
    private String location;
    private String url;

    public Ad() {
    }

    public Ad(String title, String description, String image, String price, String location, String url) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.location = location;
        this.url = url;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
