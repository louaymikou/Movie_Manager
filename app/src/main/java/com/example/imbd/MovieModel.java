package com.example.imbd;

public class MovieModel {
    private String id;
    private String title;
    private String imageUrl;
    private String rating;

    public MovieModel(String id, String title, String imageUrl, String rating) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getRating() { return rating; }

}
