package com.example.dmitry.videoeditor.Items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.dmitry.videoeditor.R;

/**
 * Created by dmitry on 22.10.17.
 */

public class KropFrame {
    private int top;
    private int bottom;
    private int left;
    private int right;
    private int clicableFildLen;
    private int intLen;
    private int outLen;
    private int cornerLen;
    private int cornerLineLen;
    private boolean bleft=false;
    private boolean bright=false;
    private boolean btop =false ;
    private boolean bbottom=false;
    private int mtop;
    private int mright;
    private int mbotton;
    private int mleft;
    private int timeX = 0;
    private int timeY = 0;
    private float scale = 0;


    public void move(int dx, int dy) {
        timeX = dx;
        timeY = dy;
    }
    public KropFrame(int left, int top, int right, int bottom, Context context) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.mleft = left;
        this.mtop = top;
        this.mbotton = bottom;
        this.mright = right;
        this.clicableFildLen = (int)context.getResources().getDimension(R.dimen.krop_frame_touch_len);
        this.intLen = (int)context.getResources().getDimension(R.dimen.krop_frame_in_line_len);
        this.outLen = (int)context.getResources().getDimension(R.dimen.krop_frame_out_line_len);
        this.cornerLen = (int)context.getResources().getDimension(R.dimen.krop_frame_border_len);
        this.cornerLineLen = (int)context.getResources().getDimension(R.dimen.krop_frame_corner_len);
        this.scale = 1;
    }

    public Rect getRect() {
        return  new Rect(left, top, right, bottom);
    }

    public void onClick(int x, int y) {
        x += timeX;
        y += timeY;
        bleft = (Math.abs(left - x) < clicableFildLen);
        bright = (Math.abs(right - x) < clicableFildLen);
        btop = (Math.abs(top - y) < clicableFildLen);
        bbottom = (Math.abs(bottom - y) < clicableFildLen);
    }

    public void scale(float scale) {
        this.scale = scale;

    }

    public boolean moveCanched() {
        return bleft || bright || bbottom || btop;
    }
    public void onMove(int x, int y) {
        x += timeX;
        y += timeY;
        if(bleft) {
            if (x < mleft) {
                x = mleft;
            }
            if (right - x > cornerLineLen * 2) {
                left = x;
            }
        }
        if(bright) {
            if(x > mright) {
                x = mright;
            }
            if (x - left > cornerLineLen * 2) {
                right = x;
            }
        }
        if(btop) {
            if (y < mtop) {
               y = mtop;
            }
            if (bottom - y > cornerLineLen * 2) {
                top = y;
            }
        }
        if(bbottom) {
            if (y > mbotton) {
                y = mbotton;
            }
            if (y - top > 50 * 2) {
                bottom = y;
            }
        }
    }

    public void onDetouch() {


        bleft = bright = btop = bbottom = false;
        left += timeX;
        right += timeX;
        top += timeY;
        bottom += timeY;
        mleft += timeX;
        mright += timeX;
        mtop += timeY;
        mbotton += timeY;

        timeY = 0;
        timeX = 0;

    }

    public void draw(Canvas canvas) {
        top += timeY;
        bottom += timeY;
        left += timeX;
        right += timeX;


        Log.d("1510", "rect: "
                + String.valueOf(top)
               + " " + String.valueOf(left)
               + " " + String.valueOf(bottom)
               + " " + String.valueOf(right));
        Paint dark = new Paint();
        dark.setStyle(Paint.Style.FILL);
        dark.setColor(0xA0000000);

        canvas.drawRect(0, 0, mright, top, dark);
        canvas.drawRect(0, bottom, mright, mbotton, dark);
        canvas.drawRect(0, top, left, bottom, dark);
        canvas.drawRect(right, top, mright, bottom, dark);

        Paint paint = new Paint();
        Paint cornersPaint = new Paint();
        Paint tablePaint = new Paint();
        tablePaint.setStrokeWidth(intLen);
        tablePaint.setColor(Color.WHITE);
        cornersPaint.setColor(Color.WHITE);
        cornersPaint.setStrokeWidth(cornerLen);
        paint.setStrokeWidth(outLen);
        paint.setColor(Color.GRAY);
        canvas.drawLine(
                left + (right - left)  / 3
                ,top
                ,left + (right - left)  / 3
                ,bottom
                , tablePaint);

        canvas.drawLine(
                left + (right - left) * 2 / 3
                , top
                , left + (right -left) * 2 / 3
                , bottom
                , tablePaint);


        canvas.drawLine(
               left  + outLen
                , top + (bottom - top) /3
                , right - outLen
                , top + (bottom - top) /3
                , tablePaint);

        canvas.drawLine(
                left  + outLen
                , top + (bottom - top) * 2 /3
                , right - outLen
                , top + (bottom - top) * 2 /3
                , tablePaint);

        canvas.drawLine(left + outLen/2, top, left + outLen/2, bottom, paint);
        canvas.drawLine(right - outLen/2, top, right - outLen/2, bottom, paint);
        canvas.drawLine(left, top, right, top, paint);
        canvas.drawLine(left, bottom, right, bottom, paint);



        canvas.drawLine(left, top, left + cornerLineLen, top, cornersPaint);
        canvas.drawLine(left  + outLen/2, top, left + outLen/2 , top  + cornerLineLen , cornersPaint);

        canvas.drawLine(right - cornerLineLen, top, right,top, cornersPaint);
        canvas.drawLine(right - outLen/2, top, right - outLen/2, top + cornerLineLen, cornersPaint);

        canvas.drawLine(left + outLen/2, bottom - cornerLineLen, left + outLen/2, bottom, cornersPaint);
        canvas.drawLine(left + cornerLineLen, bottom, left, bottom, cornersPaint);

        canvas.drawLine(right, bottom, right - cornerLineLen, bottom, cornersPaint);
        canvas.drawLine(right  - outLen/2, bottom, right - outLen/2, bottom - cornerLineLen, cornersPaint);

        top -= timeY;
        bottom -= timeY;
        left -= timeX;
        right -= timeX;

    }
}
