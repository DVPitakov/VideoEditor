package com.example.dmitry.videoeditor;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by dmitry on 11.09.17.
 */

public class ImageElement {
    protected Rect rect;
    protected int x = 0;
    protected int y = 0;
    protected float alpha = 0;
    protected float oldAlpha = 0;
    protected Matrix matrix;
    protected Matrix inverseMatrix;

    public ImageElement() {
        rect = new Rect();
        matrix = new Matrix();
        inverseMatrix = new Matrix();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

    }

    public boolean contains(int x, int y) {
        float points[] = new float[2];
        points[0] = (float)x;
        points[1] = (float)y;
        inverseMatrix.mapPoints(points);
        Rect myRect = new Rect(0, 0, rect.right - rect.left, rect.bottom - rect.top);
        return rect != null && myRect.contains((int)points[0], (int)points[1]);

    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void saveAlpha() {
        oldAlpha += alpha;
        alpha = 0;
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
