package com.example.reviewphim;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);

        // Liên kết các thành phần giao diện
        Spinner spinnerDate = findViewById(R.id.spinnerDate);
        pieChartView = findViewById(R.id.customPieChart);

        // Lấy danh sách ngày từ cơ sở dữ liệu
        List<String> dateList = dbHelper.getAvailableDates();
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(dateAdapter);

        // Đặt sự kiện khi chọn ngày từ Spinner
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = dateList.get(position);
                updatePieChart(selectedDate); // Cập nhật PieChart với ngày đã chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có ngày được chọn
            }
        });

        // Cập nhật PieChart lần đầu (ngày đầu tiên trong danh sách)
        if (!dateList.isEmpty()) {
            updatePieChart(dateList.get(0));
        }
    }

    private void updatePieChart(String date) {
        // Lấy dữ liệu từ cơ sở dữ liệu cho ngày được chọn
        int totalMovies = dbHelper.getTotalMoviesInDay(date);
        List<GenreStatistic> stats = dbHelper.getGenreStatisticsForDate(date);

        // Tính toán tỷ lệ phần trăm
        float[] percentages = new float[stats.size()];
        int[] colors = new int[stats.size()];

        for (int i = 0; i < stats.size(); i++) {
            GenreStatistic stat = stats.get(i);
            percentages[i] = totalMovies > 0 ? ((float) stat.getCount() / totalMovies) * 100 : 0;
            colors[i] = stat.getColor();
        }

        // Cập nhật PieChart với dữ liệu
        if (stats.isEmpty()) {
            pieChartView.setData(new float[]{100}, new int[]{0xFFC0C0C0}); // Hiển thị màu xám nếu không có dữ liệu
        } else {
            pieChartView.setData(percentages, colors);
        }
        updateLegend(stats);
    }
    private void updateLegend(List<GenreStatistic> stats) {
        LinearLayout legendContainer = findViewById(R.id.legendContainer);
        legendContainer.removeAllViews(); // Xóa các chú thích cũ

        for (GenreStatistic stat : stats) {
            LinearLayout legendItem = new LinearLayout(this);
            legendItem.setOrientation(LinearLayout.HORIZONTAL);
            legendItem.setPadding(8, 8, 8, 8);

            View colorBox = new View(this);
            colorBox.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
            colorBox.setBackgroundColor(stat.getColor());

            TextView legendText = new TextView(this);
            legendText.setText(String.format("%s: %d phim", stat.getGenreName(), stat.getCount()));
            legendText.setTextSize(16);
            legendText.setPadding(16, 0, 0, 0);

            legendItem.addView(colorBox);
            legendItem.addView(legendText);
            legendContainer.addView(legendItem);
        }
    }

}
