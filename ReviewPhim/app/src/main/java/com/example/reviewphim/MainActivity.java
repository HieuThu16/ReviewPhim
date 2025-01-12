package com.example.reviewphim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewMovies); // Khởi tạo RecyclerView
        Button btnAddMovie = findViewById(R.id.btnAddMovie);
        Button btnAddGenre = findViewById(R.id.btnAddGenre);

        // Hiển thị danh sách phim
        List<Movie> movieList = dbHelper.getAllMovies();
        movieAdapter = new MovieAdapter(movieList); // Sử dụng MovieAdapter đã khai báo sẵn
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        // Sự kiện nút "Thêm phim"
        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
        });

        // Sự kiện nút "Thêm thể loại"
        btnAddGenre.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGenreActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách phim khi quay lại màn hình chính
        List<Movie> movieList = dbHelper.getAllMovies();
        movieAdapter.updateMovieList(movieList); // Cập nhật dữ liệu trong Adapter
    }
}
