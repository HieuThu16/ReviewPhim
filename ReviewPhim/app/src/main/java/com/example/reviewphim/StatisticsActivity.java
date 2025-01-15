package com.example.reviewphim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Ánh xạ các nút
        Button btnHome = findViewById(R.id.btnHome);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnStatistics = findViewById(R.id.btnStatistics);

        // Nút Trang chủ
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng Activity hiện tại
            }
        });

        // Nút Thêm
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện một hành động khác hoặc chuyển Activity
            }
        });

        // Nút Thống kê (có thể giữ nguyên hoặc cập nhật nếu cần)
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện tại bạn đang ở màn hình này, không cần chuyển
            }
        });
    }
}
