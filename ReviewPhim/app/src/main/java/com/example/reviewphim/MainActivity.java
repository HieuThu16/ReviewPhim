package com.example.reviewphim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMovies);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Hiển thị danh sách phim
        List<Movie> movieList = dbHelper.getAllMovies();
        movieAdapter = new MovieAdapter(movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        // Sự kiện nhấn nút "Thêm"
        btnAdd.setOnClickListener(v -> showAddOptions());
    }

    // Hàm hiển thị BottomSheetDialog
    private void showAddOptions() {
        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_options, null);

        // Gắn các nút trong BottomSheetDialog
        Button btnAddMovie = bottomSheetView.findViewById(R.id.btnAddMovie);
        Button btnAddGenre = bottomSheetView.findViewById(R.id.btnAddGenre);

        // Xử lý sự kiện nút "Thêm phim"
        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // Xử lý sự kiện nút "Thêm thể loại"
        btnAddGenre.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGenreActivity.class);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
