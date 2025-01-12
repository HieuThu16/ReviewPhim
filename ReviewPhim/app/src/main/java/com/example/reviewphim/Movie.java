package com.example.reviewphim;

public class Movie {
    private int id;
    private String name;
    private int genreId;
    private String startDate;
    private int completed;
    private int episodesWatched;
    private float rating; // Điểm đánh giá

    public Movie(int id, String name, int genreId, String startDate, int completed, int episodesWatched, float rating) {
        this.id = id;
        this.name = name;
        this.genreId = genreId;
        this.startDate = startDate;
        this.completed = completed;
        this.episodesWatched = episodesWatched;
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getGenreId() { return genreId; }
    public String getStartDate() { return startDate; }
    public int isCompleted() { return completed; }
    public int getEpisodesWatched() { return episodesWatched; }
}
