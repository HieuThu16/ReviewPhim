package com.example.reviewphim;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private PieChartView customPieChart;
    private Spinner spinnerDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Ánh xạ view
        customPieChart = findViewById(R.id.customPieChart);
        spinnerDate = findViewById(R.id.spinnerDate);

        // Danh sách ngày giả lập
        List<String> dateList = new ArrayList<>();
        dateList.add("Hôm nay");
        dateList.add("Hôm qua");
        dateList.add("Ngày trước nữa");

        // Cấu hình Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapter);

        // Dữ liệu giả lập cho biểu đồ
        float[] data = {30, 50, 20}; // Tỷ lệ phần trăm
        int[] colors = {0xFFE57373, 0xFF81C784, 0xFF64B5F6}; // Màu sắc

        // Hiển thị dữ liệu lên biểu đồ
        customPieChart.setData(data, colors);
    }
}
