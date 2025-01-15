package com.example.reviewphim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private DatabaseHelper dbHelper;
    private List<String> genreList;  // Danh sách thể loại

    // Constructor nhận danh sách phim và DatabaseHelper
    public MovieAdapter(List<Movie> movieList, DatabaseHelper dbHelper) {
        this.movieList = movieList;
        this.dbHelper = dbHelper;
        // Lấy danh sách thể loại từ cơ sở dữ liệu một lần duy nhất
        this.genreList = dbHelper.getAllGenres();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Dùng genreId để tra cứu tên thể loại từ danh sách genreList
        String genreName = (movie.getGenreId() >= 0 && movie.getGenreId() < genreList.size()) ? genreList.get(movie.getGenreId()) : "Không xác định";

        // Gán dữ liệu vào các TextView
        holder.title.setText(movie.getName());
        holder.genre.setText("Thể loại: " + genreName);
        holder.episodesWatched.setText("Tập đã xem: " + movie.getEpisodesWatched());
        holder.rating.setText("Điểm đánh giá: " + movie.getRating());
        holder.note.setText("Ghi chú: " + (movie.getGhiChu() != null ? movie.getGhiChu() : "Không có"));
        holder.startDate.setText("Ngày bắt đầu xem: " + movie.getStartDate());
        holder.completed.setText("Hoàn thành: " + (movie.isCompleted() == 1 ? "Có" : "Chưa"));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // ViewHolder chứa các TextView
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView genre;
        TextView episodesWatched;
        TextView rating;
        TextView note;
        TextView startDate;
        TextView completed;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các TextView từ layout item_movie
            title = itemView.findViewById(R.id.tvTitle);
            genre = itemView.findViewById(R.id.tvGenre);
            episodesWatched = itemView.findViewById(R.id.tvEpisodesWatched);
            rating = itemView.findViewById(R.id.tvRating);
            note = itemView.findViewById(R.id.tvNote);
            startDate = itemView.findViewById(R.id.tvStartDate);
            completed = itemView.findViewById(R.id.tvCompleted);
        }
    }

    // Cập nhật danh sách phim và làm mới giao diện
    public void updateList(List<Movie> newMovieList) {
        this.movieList = newMovieList;
        notifyDataSetChanged();
    }
}
