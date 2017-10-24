package com.example.dmitry.videoeditor.Vidgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.example.dmitry.videoeditor.Holders.FontHolder;
import com.example.dmitry.videoeditor.Tools;

/**
 * Created by dmitry on 11.09.17.
 */

public class TextImage extends ImageElement {
    private String text = "Новый текст";
    private int textSize = 60;
    private int textSizeD = 60;
    private int color = Color.WHITE;
    private boolean isBold = false;
    private boolean isItalic = false;
    private int textType = 0;

    public TextImage(String text, int left, int bottom) {
        super();
        rect = new Rect();
        rect.set(left - textSizeD / 4  * text.length(), bottom - textSizeD, left + textSizeD / 4 * text.length(), bottom);
        this.text = text;

    }

    public void setPos(int left, int bottom) {
        rect.set(left, bottom - textSizeD, left + textSizeD / 2 * text.length(), bottom);

    }

    public void setTextSize(float loupe) {
        Log.d("step", "setTextSize");
        textSizeD = (int)(textSize * loupe * 1.2);
        rect.set(rect.left,
                rect.bottom - textSizeD,
                rect.left + textSizeD / 2 * text.length(),
                rect.bottom);

    }

    public void setFont(int textType) {
        this.textType = textType;
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

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public void draw(Canvas canvas) {
        float senterX = Tools.getCenter(rect.right + x, rect.left + x);
        float senterY = Tools.getCenter(rect.top + y , rect.bottom + y);
        matrix.reset();
        matrix.setTranslate(rect.left + x, rect.top + y);
        matrix.postRotate(alpha + oldAlpha, senterX , senterY);

        inverseMatrix.reset();
        inverseMatrix.setRotate(-alpha -oldAlpha, senterX, senterY);
        inverseMatrix.postTranslate(-rect.left - x, - rect.top  - y);

        int textStyle = 0;
        if(isBold) textStyle += Typeface.BOLD;
        if(isItalic) textStyle += Typeface.ITALIC;
        Paint fontPaint = new Paint();

        Typeface typeface = FontHolder.getInstance().getType(textType);

        fontPaint.setColor(color);
        fontPaint.setTypeface(Typeface.create(typeface, textStyle));
        fontPaint.setStrokeWidth(5.0f);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setTextSize(textSizeD);
        canvas.setMatrix(matrix);
        canvas.drawText(text, 0, - rect.top + rect.bottom, fontPaint);
        //fontPaint.setStyle(Paint.Style.STROKE);
        //canvas.drawRect( 0, - rect.top + rect.bottom, rect.right - rect.left, 0, fontPaint);


    }
}
