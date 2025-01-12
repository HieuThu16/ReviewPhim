package com.example.reviewphim;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGenreActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_genre);

        dbHelper = new DatabaseHelper(this);

        EditText edtGenreName = findViewById(R.id.edtGenreName);
        Button btnSaveGenre = findViewById(R.id.btnSaveGenre);

        btnSaveGenre.setOnClickListener(v -> {
            String genreName = edtGenreName.getText().toString();

            if (genreName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên thể loại!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ten_theloai", genreName);

            long result = db.insert("theloai", null, values);
            if (result != -1) {
                Toast.makeText(this, "Thêm thể loại thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thể loại thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
