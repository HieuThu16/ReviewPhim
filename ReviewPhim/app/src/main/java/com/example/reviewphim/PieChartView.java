package com.example.reviewphim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {

    private Paint paint;
    private float[] data = new float[]{100}; // Mặc định hiển thị nếu không có dữ liệu
    private int[] colors = new int[]{Color.LTGRAY}; // Màu mặc định

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
  
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = 0;
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 3;
        int centerX = width / 2;
        int centerY = height / 2;

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < data.length; i++) {
            float sweepAngle = (data[i] / 100) * 360;
            paint.setColor(colors[i]);
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, sweepAngle, true, paint);

            // Tính toán vị trí để vẽ số lượng
            float angle = startAngle + sweepAngle / 2;
            float textX = (float) (centerX + radius / 1.5 * Math.cos(Math.toRadians(angle)));
            float textY = (float) (centerY + radius / 1.5 * Math.sin(Math.toRadians(angle)));

            canvas.drawText(String.format("%.1f%%", data[i]), textX, textY, textPaint);

            startAngle += sweepAngle;
        }
    }


    public void setData(float[] data, int[] colors) {
        if (data != null && colors != null && data.length == colors.length) {
            this.data = data;
            this.colors = colors;
            invalidate(); // Làm mới biểu đồ
        }
    }
}
