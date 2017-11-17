package edu.example.dmitry.videoeditor.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import edu.example.dmitry.videoeditor.holders.HistoryHolder;
import edu.example.dmitry.videoeditor.models.SettingsData;
import edu.example.dmitry.videoeditor.Tools;

/**
 * Created by dmitry on 11.09.17.
 */

public abstract class BaseItem {
    View view;
    protected static Bitmap rot = null;
    protected static Bitmap scale = null;
    protected static Bitmap delete = null;

    protected float rotate_button_width;
    protected float rotate_button_height;

    protected int touchX = 0;
    protected int touchY = 0;
    private float spsificAlpha = 0;

    protected Rect rect;
    protected int x = 0;
    protected int y = 0;
    protected float alpha = 0;
    protected float oldAlpha = 0;
    protected Matrix matrix;
    protected Matrix inverseMatrix;
    protected int previosWidth;
    protected int previosHeight;
    protected int action = 0;
    protected float savedScale = 1;
    protected float timeScale = 1;
    protected float frame_width = 1;
    boolean focused = false;
    float p1;
    float p2;

    public int getActionType() {
        return action;
    }
    public BaseItem(View view) {
        this.view = view;
        //if (rot == null) {
            frame_width = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.frame);
            rotate_button_width = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.item_rotate_button_width);
            rotate_button_height = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.item_rotate_button_height);
            rot = BitmapFactory.decodeResource(view.getResources(), edu.example.dmitry.videoeditor.R.drawable.refresh_button);
            rot = Bitmap.createScaledBitmap(rot, (int)rotate_button_width, (int)rotate_button_height, false);
        //}
        //if (scale == null) {
            rotate_button_width = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.item_rotate_button_width);
            rotate_button_height = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.item_rotate_button_height);
            scale = BitmapFactory.decodeResource(view.getResources(), edu.example.dmitry.videoeditor.R.drawable.enlarge_filled);
            scale = Bitmap.createScaledBitmap(scale, (int)rotate_button_width, (int)rotate_button_height, false);
        //}
        //if (delete == null) {
            rotate_button_width = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.item_rotate_button_width);
            rotate_button_height = view.getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.item_rotate_button_height);
            delete = BitmapFactory.decodeResource(view.getResources(), edu.example.dmitry.videoeditor.R.drawable.delete_button);
            delete = Bitmap.createScaledBitmap(delete, (int)rotate_button_width, (int)rotate_button_height, false);
        //}
        rect = new Rect();
        matrix = new Matrix();
        inverseMatrix = new Matrix();
    }

    public void focusLosed() {
        focused = false;

    }

    public void setFocus() {
        focused = true;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

    }

    public int contains(int x, int y) {
        touchX = x;
        touchY = y;
        float points[] = new float[2];
        points[0] = (float)x;
        points[1] = (float)y;
        inverseMatrix.mapPoints(points);
        action = 0;
        Rect myRect = new Rect(0, 0, rect.right - rect.left, rect.bottom - rect.top);
        if (rect != null && myRect.contains((int)points[0], (int)points[1])) action = 1;
        if (SettingsData.getInstance(view.getContext()).isShowFrame()) {
            if (
                    points[0] > -rotate_button_width / 2
                            && points[1] > -rotate_button_width / 2
                            && points[0] < rotate_button_width / 2
                            && points[1] < rotate_button_height / 2) {
                action = 2;
            }
            if (points[0] < rect.right - rect.left + rotate_button_width / 2
                    && points[1] < rect.bottom - rect.top + rotate_button_width / 2
                    && points[0] > rect.right - rect.left - rotate_button_width / 2
                    && points[1] > rect.bottom - rect.top - rotate_button_height / 2) {
                action = 3;
                p1 = Tools.getCenter(0, rect.right - rect.left);
                p2 = Tools.getCenter(0, rect.bottom - rect.top);
            }
            if (points[0] < rect.right - rect.left + rotate_button_width / 2
                    && points[1] > -rotate_button_width / 2
                    && points[0] > rect.right - rect.left - rotate_button_width / 2
                    && points[1] < rotate_button_height / 2) {
                action = 4;
                previosWidth = rect.right - rect.left;
                previosHeight = rect.bottom - rect.top;
                p1 = Tools.getCenter(0, rect.right - rect.left);
                p2 = Tools.getCenter(0, rect.bottom - rect.top);
            }
            if (points[0] < -rotate_button_width / 2
                    && points[1] > rect.right - rect.left + rotate_button_width / 2
                    && points[0] < rotate_button_width / 2
                    && points[1] > rect.bottom - rect.top - rotate_button_height / 2) {
                action = 5;
                previosWidth = rect.right - rect.left;
                previosHeight = rect.bottom - rect.top;
                p1 = Tools.getCenter(0, rect.right - rect.left);
                p2 = Tools.getCenter(0, rect.bottom - rect.top);
            }
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

    public void __force_set_action_1() {
        action = 1;
    }

    public void move(int x, int y, int action) {

        this.action = action;
        move(x, y);
    }

    public void move(int x, int y) {
        if (action == 1) {
            this.x = x;
            this.y = y;
        }
        else if (action == 2){
            float points[] = new float[2];
            points[0] = (float)(x +touchX);
            points[1] = (float)(y + touchY);
            inverseMatrix.mapPoints(points);
            p1 = Tools.getCenter(0, rect.right - rect.left);
            p2 =Tools.getCenter(0, rect.bottom - rect.top);
            Log.d("2240", "" + p1 + " " +  p2);
            Log.d("2240", "" + x + " " + y);
            setAlpha(Tools.getAlpha(0, 0, p1, p2, points[0], points[1], p1, p2));
            spsificAlpha += this.alpha;
            saveAlpha();
        }
        else if (action == 4) {
            float points[] = new float[2];
            points[0] = (float)(x +touchX);
            points[1] = (float)(y + touchY);
            inverseMatrix.mapPoints(points);
            scale(1.0f * (points[0]/ (previosWidth)));
        }

    }

    public HistoryHolder.Action moveEnd() {
        HistoryHolder.Action action = null;
        if(this.action == 2) {
            action = new HistoryHolder.RotateAndScale(this, spsificAlpha + alpha, 1);
        }
        else if(this.action == 4) {
            action = new HistoryHolder.RotateAndScale(this, 0, timeScale);
        }
        saveLeft();
        saveTop();
        saveAlpha();
        saveScale();
        spsificAlpha = 0; //O_o
        return action;
    }

    protected void saveScale() {
        savedScale = timeScale;
    }

    public abstract void scale(float scale);

    protected void saveLeft() {
        rect.set(rect.left + x, rect.top, rect.right + x, rect.bottom);
        x = 0;
    }

    protected void saveTop() {
        rect.set(rect.left, rect.top + y, rect.right, rect.bottom + y);
        y = 0;

    }

    public void drawFrame(Canvas canvas) {
        if (SettingsData.getInstance(view.getContext()).isShowFrame()) {
            Paint fontPaint = new Paint();
            fontPaint.setStyle(Paint.Style.STROKE);
            fontPaint.setStrokeWidth(frame_width);
            fontPaint.setColor(0xFFFFFFFF);
            canvas.drawRect(0, 0, rect.right - rect.left, rect.bottom - rect.top, fontPaint);
            fontPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(0
                    , 0
                    , rotate_button_width / 2
                    , fontPaint);
            canvas.drawBitmap(rot, -rotate_button_width / 2, -rotate_button_height / 2, fontPaint);
            canvas.drawCircle(rect.right - rect.left
                    , rect.bottom - rect.top
                    , rotate_button_width / 2
                    , fontPaint);
            canvas.drawBitmap(delete, rect.right - rect.left - rotate_button_width / 2, rect.bottom - rect.top - rotate_button_height / 2, fontPaint);
            canvas.drawCircle(0
                    , rect.bottom - rect.top
                    , rotate_button_width / 2
                    , fontPaint);
            canvas.drawBitmap(scale, -rotate_button_width / 2, rect.bottom - rect.top - rotate_button_height / 2, fontPaint);
            canvas.drawCircle(rect.right - rect.left
                    , 0
                    , rotate_button_width / 2
                    , fontPaint);
            canvas.drawBitmap(scale, rect.right - rect.left - rotate_button_width / 2, -rotate_button_height / 2, fontPaint);

        }
    }
}
