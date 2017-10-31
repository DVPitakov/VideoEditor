package com.example.dmitry.videoeditor.Vidgets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Tools;

/**
 * Created by dmitry on 11.09.17.
 */

public abstract class ImageElement {
    View view;
    protected static Bitmap rot = null;
    protected static Bitmap scale = null;
    protected static Bitmap delete = null;

    protected float rotate_button_width;
    protected float rotate_button_height;

    protected Rect rect;
    protected int x = 0;
    protected int y = 0;
    protected float alpha = 0;
    protected float oldAlpha = 0;
    protected Matrix matrix;
    protected Matrix inverseMatrix;
    protected int action = 0;
    float p1;
    float p2;

    public ImageElement(View view) {
        this.view = view;
        //if (rot == null) {
            rotate_button_width = view.getResources().getDimension(R.dimen.item_rotate_button_width);
            rotate_button_height = view.getResources().getDimension(R.dimen.item_rotate_button_height);
            rot = BitmapFactory.decodeResource(view.getResources(), R.drawable.close);
            rot = Bitmap.createScaledBitmap(rot, (int)rotate_button_width, (int)rotate_button_height, false);
        //}
        //if (scale == null) {
            rotate_button_width = view.getResources().getDimension(R.dimen.item_rotate_button_width);
            rotate_button_height = view.getResources().getDimension(R.dimen.item_rotate_button_height);
            scale = BitmapFactory.decodeResource(view.getResources(), R.drawable.close);
            scale = Bitmap.createScaledBitmap(scale, (int)rotate_button_width, (int)rotate_button_height, false);
        //}
        //if (delete == null) {
            rotate_button_width = view.getResources().getDimension(R.dimen.item_rotate_button_width);
            rotate_button_height = view.getResources().getDimension(R.dimen.item_rotate_button_height);
            delete = BitmapFactory.decodeResource(view.getResources(), R.drawable.close);
            delete = Bitmap.createScaledBitmap(delete, (int)rotate_button_width, (int)rotate_button_height, false);
        //}
        rect = new Rect();
        matrix = new Matrix();
        inverseMatrix = new Matrix();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

    }

    public int contains(int x, int y) {
        float points[] = new float[2];
        points[0] = (float)x;
        points[1] = (float)y;
        inverseMatrix.mapPoints(points);
        action = 0;
        Rect myRect = new Rect(0, 0, rect.right - rect.left, rect.bottom - rect.top);
        if (rect != null && myRect.contains((int)points[0], (int)points[1])) action = 1;
        if(points[0] < rotate_button_width/2 && points[1] < rotate_button_height/2) {
            action = 2;
            p1 = Tools.getCenter(0, rect.right - rect.left);
            p2 =Tools.getCenter(0, rect.bottom - rect.top);
        }
        if(points[0] > rect.right - rect.left -rotate_button_width/2 && points[1] > rect.bottom - rect.top-rotate_button_height/2) {
            action = 3;
            p1 = Tools.getCenter(0, rect.right - rect.left);
            p2 =Tools.getCenter(0, rect.bottom - rect.top);
        }
        if(points[0] > rect.right - rect.left -rotate_button_width/2 && points[1] < rotate_button_height/2) {
            action = 4;
            p1 = Tools.getCenter(0, rect.right - rect.left);
            p2 =Tools.getCenter(0, rect.bottom - rect.top);
        }
        if(points[0] < rotate_button_width/2 && points[1] > rect.bottom - rect.top-rotate_button_height/2) {
            action = 5;
            p1 = Tools.getCenter(0, rect.right - rect.left);
            p2 =Tools.getCenter(0, rect.bottom - rect.top);
        }
        return action;

    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void saveAlpha() {
        oldAlpha += alpha;
        alpha = 0;
    }

    public void move(int x, int y) {
        if (action == 1) {
            this.x = x;
            this.y = y;
        }
        else if (action == 2){
            setAlpha(Tools.getAlpha(0, 0, p1, p2, x, y, p1, p2));
        }
        else if (action == 4) {
            scale(1.0f * (x + (rect.right - rect.left))/ (rect.right - rect.left));
        }

    }

    public abstract void scale(float scale);

    public void saveLeft() {
        rect.set(rect.left + x, rect.top, rect.right + x, rect.bottom);
        x = 0;

    }

    public void saveTop() {
        rect.set(rect.left, rect.top + y, rect.right, rect.bottom + y);
        y = 0;

    }

    public void drawFrame(Canvas canvas) {
        Paint fontPaint = new Paint();
        fontPaint.setStyle(Paint.Style.STROKE);
        fontPaint.setStrokeWidth(20);
        canvas.drawRect(0,0, rect.right - rect.left, rect.bottom - rect.top, fontPaint);
        canvas.drawBitmap(rot, - rotate_button_width / 2, - rotate_button_height / 2, fontPaint);
        canvas.drawBitmap(delete, rect.right - rect.left - rotate_button_width / 2 , rect.bottom - rect.top  - rotate_button_height / 2, fontPaint);
        canvas.drawBitmap(scale, - rotate_button_width / 2, rect.bottom - rect.top  - rotate_button_height / 2, fontPaint);
        canvas.drawBitmap(scale, rect.right - rect.left - rotate_button_width / 2, - rotate_button_height / 2, fontPaint);

    }
}
