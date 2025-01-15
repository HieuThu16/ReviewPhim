package com.example.reviewphim;

public class GenreStatistic {
    private String genreName;
    private int count;
    private int color;

    public GenreStatistic(String genreName, int count, int color) {
        this.genreName = genreName;
        this.count = count;
        this.color = color;
    }

    public String getGenreName() {
        return genreName;
    }

    public int getCount() {
        return count;
    }

    public int getColor() {
        return color;
    }
}
