package com.example.reviewphim;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private MovieAdapter movieAdapter;
    private Spinner spinnerGenre;
    private RecyclerView recyclerView;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        recyclerView = findViewById(R.id.recyclerViewMovies);
        Button btnAdd = findViewById(R.id.btnAdd);
        edtSearch = findViewById(R.id.edtSearch);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        Button btnStatistics = findViewById(R.id.btnStatistics);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Hiển thị danh sách phim
        List<Movie> movieList = dbHelper.getAllMovies();

        // Khởi tạo adapter cho RecyclerView
        movieAdapter = new MovieAdapter(movieList, dbHelper);

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        // Thiết lập sự kiện cho nút "Thêm"
        btnAdd.setOnClickListener(v -> showAddOptions());

        // Thiết lập sự kiện cho nút thống kê
        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // Lắng nghe thay đổi trên EditText (Tìm kiếm phim)
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Thiết lập Spinner thể loại
        List<String> genreList = dbHelper.getAllGenres();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);

        // Sự kiện khi chọn thể loại từ Spinner
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterMovies(edtSearch.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void filterMovies(String query) {
        // Lấy thể loại được chọn
        String selectedGenre = spinnerGenre.getSelectedItem().toString();

        // Nếu người dùng không nhập tên phim, tìm kiếm theo thể loại
        List<Movie> filteredList;
        if (selectedGenre != null && !selectedGenre.isEmpty()) {
            filteredList = dbHelper.searchMoviesByNameAndGenre(query, selectedGenre);
        } else {
            filteredList = dbHelper.searchMoviesByName(query);
        }

        // Kiểm tra danh sách phim đã lọc
        if (filteredList == null) {
            filteredList = new ArrayList<>();
        }

        // Cập nhật danh sách phim cho RecyclerView
        movieAdapter.updateList(filteredList);
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
