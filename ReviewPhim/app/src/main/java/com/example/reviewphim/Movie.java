package com.example.reviewphim;

public class Movie {
    private int id;
    private String name;
    private int genreId;
    private String startDate;
    private int completed;
    private int episodesWatched;
    private float rating;
    private String ghiChu; // Thêm thuộc tính ghi chú

    public Movie(int id, String name, int genreId, String startDate, int completed, int episodesWatched, float rating, String ghiChu) {
        this.id = id;
        this.name = name;
        this.genreId = genreId;
        this.startDate = startDate;
        this.completed = completed;
        this.episodesWatched = episodesWatched;
        this.rating = rating;
        this.ghiChu = ghiChu; // Gán giá trị ghi chú
    }

    public float getRating() {
        return rating;
    }
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getGenreId() { return genreId; }
    public String getStartDate() { return startDate; }
    public int isCompleted() { return completed; }
    public int getEpisodesWatched() { return episodesWatched; }
}
