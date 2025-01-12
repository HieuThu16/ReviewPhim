package com.example.reviewphim;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddMovieActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private List<Genre> genreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        dbHelper = new DatabaseHelper(this);

        EditText edtMovieName = findViewById(R.id.edtMovieName);
        EditText edtStartDate = findViewById(R.id.edtStartDate);
        EditText edtCompleted = findViewById(R.id.edtCompleted);
        EditText edtEpisodesWatched = findViewById(R.id.edtEpisodesWatched);
        EditText edtRating = findViewById(R.id.edtRating);  // Trường điểm đánh giá
        Spinner spinnerGenres = findViewById(R.id.spinnerGenres);
        Button btnSaveMovie = findViewById(R.id.btnSaveMovie);

        // Lấy danh sách thể loại từ database
        genreList = getAllGenres();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getGenreNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenres.setAdapter(adapter);

        btnSaveMovie.setOnClickListener(v -> {
            try {
                String movieName = edtMovieName.getText().toString();
                String startDate = edtStartDate.getText().toString();
                int completed = Integer.parseInt(edtCompleted.getText().toString());
                int episodesWatched = Integer.parseInt(edtEpisodesWatched.getText().toString());
                float rating = Float.parseFloat(edtRating.getText().toString());  // Lấy điểm đánh giá
                int genreId = genreList.get(spinnerGenres.getSelectedItemPosition()).getId();

                if (movieName.isEmpty() || startDate.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("ten_phim", movieName);
                values.put("theloai", genreId);
                values.put("ngay_bat_dau", startDate);
                values.put("ket_thuc_chua", completed);
                values.put("tap_da_xem", episodesWatched);
                values.put("diem_danh_gia", rating);  // Thêm điểm đánh giá vào cơ sở dữ liệu

                long result = db.insert("phim", null, values);

                if (result != -1) {
                    Toast.makeText(this, "Thêm phim thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm phim thất bại!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Dữ liệu nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM theloai", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ma_theloai"));
                String name = cursor.getString(cursor.getColumnIndex("ten_theloai"));
                genres.add(new Genre(id, name));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return genres;
    }

    private List<String> getGenreNames() {
        List<String> names = new ArrayList<>();
        for (Genre genre : genreList) {
            names.add(genre.getName());
        }
        return names;
    }
}
