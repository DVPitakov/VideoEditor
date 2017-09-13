package com.example.dmitry.videoeditor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by dmitry on 11.09.17.
 */

public class ImageElement {
    protected Rect rect;
    protected int x;
    protected int y;
    public ImageElement() {
        Rect rect = new Rect();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

    }

    public boolean contains(int x, int y) {
        Log.d("logo", "here " + String.valueOf(rect.contains(x, y)));
        return rect != null && rect.contains(x, y);

    }

    public void setLeft(int x) {
        this.x = x;

    }

    public void setTop(int y) {
        this.y = y;

    }

    public void saveLeft() {
        rect.set(rect.left + x, rect.top, rect.right + x, rect.bottom);
        x = 0;

    }

    public void saveTop() {
        rect.set(rect.left, rect.top + y, rect.right, rect.bottom + y);
        y = 0;

    }
}
