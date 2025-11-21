package com.example.imbd;

public class Movie {
    private String id;
    private String title;
    private String imageUrl;
    private String rating;
    private String genre;       // Optional, helpful for Watchlist
    private String description; // Optional, helpful for Watchlist
    private String year;        // Optional, helpful for Watchlist

    // Constructor for API Lists (Simple)
    public Movie(String id, String title, String imageUrl, String rating) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    // Full Constructor (for DB or Details if needed later)
    public Movie(String id, String title, String imageUrl, String rating, String genre, String year, String description) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.genre = genre;
        this.year = year;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getRating() { return rating; }

    // Safe Getters for optional fields
    public String getGenre() { return genre != null ? genre : ""; }
    public String getYear() { return year != null ? year : ""; }
    public String getDescription() { return description != null ? description : ""; }
}
