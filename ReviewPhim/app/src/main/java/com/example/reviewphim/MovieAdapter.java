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

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout tùy chỉnh
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Gán dữ liệu vào các TextView
        holder.title.setText(movie.getName());
        holder.episodesWatched.setText("Tập đã xem: " + movie.getEpisodesWatched());
        holder.rating.setText("Điểm đánh giá: " + movie.getRating());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView episodesWatched;
        TextView rating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các TextView từ layout tùy chỉnh
            title = itemView.findViewById(R.id.tvTitle);
            episodesWatched = itemView.findViewById(R.id.tvEpisodesWatched);
            rating = itemView.findViewById(R.id.tvRating);
        }
    }

    public void updateMovieList(List<Movie> newMovieList) {
        movieList = newMovieList;
        notifyDataSetChanged();
    }
}
