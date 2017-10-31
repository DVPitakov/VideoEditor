package com.example.dmitry.videoeditor.UFO;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.dmitry.videoeditor.Tools;

/**
 * Created by dmitry on 31.10.17.
 */

public class Item {

    float savedX = 0;
    float savedY = 0;
    float savedAlpha = 0;
    float width = 0;
    float height = 0;

    float timeScale = 1;
    float savedScale = 1;
    float timeX = 0;
    float timeY = 0;
    float timeAlpha = 0;

    protected Matrix matrix = new Matrix();
    protected Matrix inverseMatrix = new Matrix();

    public void move(float dx, float dy) {
        timeX = dx + savedX;
        timeY = dy + savedY;
    }

    public void rotate(float alpha) {
        this.timeAlpha = alpha + savedAlpha;
    }

    public void saveRotate() {
        this.savedAlpha = timeAlpha;
    }

    public void scale(float scall) {
        timeScale = savedScale + scall;
    }

    public void saveScale() {
        savedScale = timeScale;
    }

    protected void updateMatrix() {
        matrix.reset();
        matrix.setTranslate(timeX, timeX);
        matrix.postRotate(timeAlpha, centerX(), centerY());

        inverseMatrix.reset();
        inverseMatrix.setRotate(-timeAlpha, centerX(), centerY());
        inverseMatrix.postTranslate(-timeX, -timeY);
    }

    public void saveMovePoin() {
        savedX = timeX;
        savedY = timeY;
    }

    public float centerX() {
        return Tools.getCenter(timeX, timeX + width);
    }

    public float centerY() {
        return Tools.getCenter(timeY, timeY + height);
    }


    public void onClick(float x, float y) {

    }

    public void draw(Canvas canvas) {
        updateMatrix();
        canvas.drawColor(0xFF00FF00);

    }
}
