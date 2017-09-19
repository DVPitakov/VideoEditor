package com.example.dmitry.videoeditor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by dmitry on 11.09.17.
 */

public class TextImage extends ImageElement {
    private String text = "Новый текст";
    private int textSize = 60;
    private int textSizeD = 60;
    private int color = Color.WHITE;

    public TextImage(String text, int left, int bottom) {
        super();
        rect = new Rect();
        rect.set(left, bottom - textSizeD, left + textSizeD / 2 * text.length(), bottom);
        this.text = text;

    }

    public void setPos(int left, int bottom) {
        rect.set(left, bottom - textSizeD, left + textSizeD / 2 * text.length(), bottom);

    }

    public void setTextSize(float loupe) {
        Log.d("step", "setTextSize");
        textSizeD = (int)(textSize * loupe);
        rect.set(rect.left,
                rect.bottom - textSizeD,
                rect.left + textSizeD / 2 * text.length(),
                rect.bottom);

    }

    public void saveTextSize() {
        Log.d("step", "saveTextSize");
        textSize = textSizeD;

    }

    public void setText(String text) {
        this.text = text;
        int left = rect.left;
        int bottom = rect.bottom;
        rect.set(left, bottom - textSizeD, left + textSizeD / 2  * text.length(), bottom);

    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.setTranslate(rect.left + x, rect.top + y);
        matrix.postRotate(alpha + oldAlpha, rect.left + x, rect.top + y);

        inverseMatrix.reset();
        inverseMatrix.setTranslate(-rect.left - x, - rect.top  - y);
        inverseMatrix.postRotate(-alpha -oldAlpha, 0, 0);

        Paint fontPaint = new Paint();
        fontPaint.setColor(color);
        fontPaint.setStrokeWidth(5.0f);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setTextSize(textSizeD);
        canvas.setMatrix(matrix);
        canvas.drawText(text, 0, - rect.top + rect.bottom, fontPaint);
        //fontPaint.setStyle(Paint.Style.STROKE);
        //canvas.drawRect( 0, - rect.top + rect.bottom, rect.right - rect.left, 0, fontPaint);


    }
}
