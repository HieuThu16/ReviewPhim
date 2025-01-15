package com.example.reviewphim;

import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.List;

public class AddMovieActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper; // Đối tượng trợ giúp để thao tác với cơ sở dữ liệu
    private List<Genre> genreList;  // Danh sách các thể loại (dùng để hiển thị trong Spinner)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper
        EditText edtMovieName = findViewById(R.id.edtMovieName);
        // Liên kết các thành phần giao diện với mã
        EditText edtNote = findViewById(R.id.edtNote); // Nhập tên phim
        EditText edtStartDate = findViewById(R.id.edtStartDate);

        edtStartDate.setOnClickListener(v -> {
            // Lấy ngày hiện tại
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Hiển thị DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                // Cập nhật ngày được chọn
                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                edtStartDate.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });// Nhập ngày bắt đầu
        EditText edtCompleted = findViewById(R.id.edtCompleted); // Nhập trạng thái hoàn thành
        EditText edtEpisodesWatched = findViewById(R.id.edtEpisodesWatched); // Nhập số tập đã xem
        EditText edtRating = findViewById(R.id.edtRating);  // Nhập điểm đánh giá
        Spinner spinnerGenres = findViewById(R.id.spinnerGenres); // Spinner chọn thể loại
        Button btnSaveMovie = findViewById(R.id.btnSaveMovie); // Nút lưu phim

        // Lấy danh sách thể loại từ cơ sở dữ liệu
        genreList = getAllGenres();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getGenreNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenres.setAdapter(adapter); // Gán adapter cho Spinner

        btnSaveMovie.setOnClickListener(v -> {
            try {
                String movieName = edtMovieName.getText().toString();
                String startDate = edtStartDate.getText().toString();
                int completed = Integer.parseInt(edtCompleted.getText().toString());
                int episodesWatched = Integer.parseInt(edtEpisodesWatched.getText().toString());
                float rating = Float.parseFloat(edtRating.getText().toString());
                int genreId = genreList.get(spinnerGenres.getSelectedItemPosition()).getId();
                String note = edtNote.getText().toString(); // Lấy nội dung ghi chú

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
                values.put("diem_danh_gia", rating);
                values.put("ghi_chu", note); // Thêm ghi chú vào cơ sở dữ liệu

                long result = db.insert("phim", null, values);

                if (result != -1) {
                    Toast.makeText(this, "Thêm phim thành công!", Toast.LENGTH_SHORT).show();

                    // Trả kết quả thành công về MainActivity
                    setResult(RESULT_OK);
                    finish(); // Đóng AddMovieActivity
                } else {
                    Toast.makeText(this, "Thêm phim thất bại!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Dữ liệu nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Lấy danh sách tất cả các thể loại từ cơ sở dữ liệu
    private List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM theloai", null); // Truy vấn bảng "theloai"

        if (cursor.moveToFirst()) {
            do {
                // Đọc dữ liệu từ từng dòng của bảng
                int id = cursor.getInt(cursor.getColumnIndex("ma_theloai")); // ID thể loại
                String name = cursor.getString(cursor.getColumnIndex("ten_theloai")); // Tên thể loại
                genres.add(new Genre(id, name)); // Thêm thể loại vào danh sách
            } while (cursor.moveToNext());
        }

        cursor.close(); // Đóng con trỏ sau khi đọc dữ liệu
        return genres;
    }

    // Lấy danh sách tên thể loại từ danh sách thể loại
    private List<String> getGenreNames() {
        List<String> names = new ArrayList<>();
        for (Genre genre : genreList) {
            names.add(genre.getName()); // Thêm tên thể loại vào danh sách
        }
        return names;
    }
}
