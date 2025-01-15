package com.example.reviewphim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {

    private Paint paint;
    private float[] data = {25, 35, 40}; // Phần trăm của từng phần
    private int[] colors = {Color.RED, Color.GREEN, Color.BLUE}; // Màu sắc của từng phần

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

        for (int i = 0; i < data.length; i++) {
            float sweepAngle = (data[i] / 100) * 360;
            paint.setColor(colors[i]);
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }

    public void setData(float[] data, int[] colors) {
        this.data = data;
        this.colors = colors;
        invalidate(); // Làm mới biểu đồ
    }
}
