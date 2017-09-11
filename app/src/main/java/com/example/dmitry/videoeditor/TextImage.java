package com.example.dmitry.videoeditor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by dmitry on 11.09.17.
 */

public class TextImage extends ImageElement {
    private String text = "Russia";

    public TextImage(String text, int left, int bottom) {
        super();
        Log.d("step", "stepx");
        rect = new Rect();
        rect.set(left, bottom - 60, left + 30 * text.length(), bottom);
        this.text = text;
        Log.d("step", "stepx + 1");

    }

    public void setPos(int left, int bottom) {
        rect.set(left, bottom - 60, left + 30 * text.length(), bottom);

    }

    public void setText(String text) {
        this.text = text;
        int left = rect.left;
        int bottom = rect.bottom;

        rect.set(left, bottom - 60, left + 30 * text.length(), bottom);

    }

    public void draw(Canvas canvas) {
        Paint fontPaint = new Paint();
        fontPaint.setColor(Color.BLUE);
        fontPaint.setStrokeWidth(5.0f);
        fontPaint.setStyle(Paint.Style.STROKE);
        fontPaint.setTextSize(60);
        canvas.drawText(text, rect.left + x, rect.bottom + y, fontPaint);

    }
}
