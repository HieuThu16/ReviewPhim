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

    // Constructor đầy đủ
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

    // Constructor mặc định
    public Movie() {
    }

    // Getter cho từng thuộc tính
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getStartDate() {
        return startDate;
    }

    public int isCompleted() {
        return completed;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
    }

    public float getRating() {
        return rating;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    // Setter cho từng thuộc tính
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public void setEpisodesWatched(int episodesWatched) {
        this.episodesWatched = episodesWatched;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
