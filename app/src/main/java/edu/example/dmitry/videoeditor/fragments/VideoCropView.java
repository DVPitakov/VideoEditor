package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

import edu.example.dmitry.videoeditor.R;


/**
 * Created by dmitry on 10.11.17.
 */

public class VideoCropView extends View {
    private float leftest;
    private float rightest;
    private float leftCur;
    private float rightCur;
    private float polzunoc;
    private float polzunocClickable = 0;
    private float leftCurTemp = 0;
    private float rightCurTemp = 0;
    private float polzunocTemp = 0;
    private int selected = 0;
    private Drawable imgleftCur;
    private Drawable imgRightCur;
    private Drawable imagePos;
    private HashMap<Integer, Drawable> videoMoments = new HashMap<>();
    private Matrix bitmapMatrix;

    private Paint shadowPaint;
    private Paint framePaint;
    private Paint polzunocPaint;
    private float borderWidth;
    private VideoCropViewListener videoCropViewListener = null;


    public interface VideoCropViewListener {
        void onPolzunocMove(float newPos);
        void onLeftCurMove(float newPos);
        void onRightCurMove(float newPos);

        void onPolzunocMoveEnd(float newPos);
        void onLeftCurMoveEnd(float newPos);
        void onRightCurMoveEnd(float newPos);

        void leftOverflow(float newPos);
        void rightOverflow(float newPos);
    }

    public VideoCropView(@NonNull Context context, float leftest, float rightest) {
        super(context);
        this.leftest = leftest;
        this.rightest = rightest;
        this.leftCur = leftest;
        this.rightCur = rightest;
        this.polzunoc = this.leftCur;

    }

    public void setVideoCropViewListener(VideoCropViewListener videoCropViewListener) {
        this.videoCropViewListener = videoCropViewListener;
    }

    //Добавить кадр img на позицию num
    public void addVideoMoment(int num, Bitmap img) {
        Log.d("19962159", img.toString());

        videoMoments.put(num, new BitmapDrawable(getResources(), img));
        invalidate();
    }



    //Считает сколько кадров поместится
    public float calculateShownVideoMoments(Drawable dr) {
        return (float)getWidth()
                / dr.getIntrinsicWidth()
                * (float)getHeight() * 3f / 4f
                / dr.getIntrinsicHeight();
    }

    public float calculateShownVideoMoments(Drawable dr, int vWidth, int vHeight) {
        return (float)vWidth
                / dr.getIntrinsicWidth()
                / (float)vHeight * 3f / 4f
                * dr.getIntrinsicHeight();
    }



    protected void initDrawObjects() {
        polzunocClickable = getResources().getDimension(R.dimen.video_crop_view_frame_polzunoc_clichable);
        shadowPaint = new Paint();
        shadowPaint.setColor(0xA0FFFFFF);
        polzunocPaint = new Paint();
        polzunocPaint.setColor(0xFF0000FF);
        polzunocPaint.setStrokeWidth(getResources().getDimension(R.dimen.video_crop_view_frame_default_width));
        framePaint = new Paint();
        framePaint.setColor(0xFF10A0FF);
        framePaint.setStrokeWidth(getResources().getDimension(R.dimen.video_crop_view_frame_default_width));
        borderWidth = getResources().getDimension(R.dimen.video_crop_view_frame_border_width);
        imgleftCur = getResources().getDrawable(R.drawable.video_krop_left_24dp);
        imgleftCur.setBounds(0,0,100,100);
        imgRightCur = getResources().getDrawable(R.drawable.video_crop_right);
        bitmapMatrix = new Matrix();
        imagePos = getResources().getDrawable(R.drawable.dont_found);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initDrawObjects();
    }

    float vStartX = 0;
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.d("debug1425", "i am here");
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d("debug1425", "i am here 2");
                vStartX = motionEvent.getX();
                float vWidth = getWidth() - borderWidth * 2;
                float borderWidthHalf = borderWidth / 2.0f;
                float vLeftCur = (leftCur+leftCurTemp - leftest) / (rightest - leftest) * (vWidth);
                float vRightCur = (rightCur+rightCurTemp - leftest) / (rightest - leftest) * (vWidth) + borderWidth;
                float vPolzunoc = (polzunoc+polzunocTemp - leftest) / (rightest - leftest) * (vWidth) + borderWidth;
                if (Math.abs(vStartX - vPolzunoc) < polzunocClickable) {
                    selected = 3;
                }
                else if((vStartX - vLeftCur) < (borderWidth) && (vStartX - vLeftCur) > 0) {
                    selected = 1;
                }
                else if ((vStartX - vRightCur) < borderWidth && (vStartX - vRightCur) > 0) {
                    selected = 2;
                }
                else {
                    selected = 0;
                    Log.d("debug1425", "i am here 6");
                }
                break;
            }
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_MOVE: {
                Log.d("debug1425", "i am here 3");
                float vWidth = getWidth() - 2 * borderWidth;
                if (selected == 1) {
                    float buf = leftCurTemp;
                    leftCurTemp = (rightest - leftest) * (motionEvent.getX() - vStartX) / vWidth;
                    if (leftCur + leftCurTemp >= rightCur) {
                        leftCurTemp = rightCur - leftCur;
                    }
                    if (leftCur + leftCurTemp < leftest) {
                        leftCurTemp = leftest - leftCur;
                    }
                    if (videoCropViewListener != null) {
                        videoCropViewListener.onLeftCurMove(leftCur+leftCurTemp);
                    }
                }
                else if(selected == 2) {
                    float buf = rightCurTemp;
                    rightCurTemp = (rightest - leftest) * (motionEvent.getX() - vStartX) / vWidth;
                    if (rightCur + rightCurTemp  <= leftCur) {
                        rightCurTemp = leftCur - rightCur;
                    }
                    if (rightCur + rightCurTemp > rightest) {
                        rightCurTemp = rightest - rightCur;
                    }
                    if (videoCropViewListener != null) {
                        videoCropViewListener.onRightCurMove(rightCur+rightCurTemp);
                    }
                }
                else if(selected == 3) {
                    polzunocTemp = (rightest - leftest) * (motionEvent.getX() - vStartX) / vWidth;
                    if (rightCur < polzunoc + polzunocTemp) {
                        polzunocTemp = rightCur - polzunoc;
                    }
                    if (leftCur > polzunoc + polzunocTemp) {
                        polzunocTemp = leftCur - polzunoc;
                    }
                    if (videoCropViewListener != null) {
                        videoCropViewListener.onPolzunocMove(polzunoc + polzunocTemp);
                    }
                }
                if (rightCur + rightCurTemp < polzunoc || leftCur + leftCurTemp > polzunoc) {
                    if (rightCur + rightCurTemp < polzunoc) {
                        polzunoc = rightCur + rightCurTemp;
                    }
                    if (leftCur + leftCurTemp > polzunoc) {
                        polzunoc = leftCur + leftCurTemp;
                    }
                    if (videoCropViewListener != null) {
                        videoCropViewListener.onPolzunocMove(polzunoc + polzunocTemp);
                    }
                }
                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                leftCur += leftCurTemp;
                rightCur += rightCurTemp;
                polzunoc += polzunocTemp;
                leftCurTemp = 0;
                rightCurTemp = 0;
                polzunocTemp = 0;
                if (selected == 1) {
                    videoCropViewListener.onLeftCurMoveEnd(leftCur);
                }
                else if(selected == 2) {
                    videoCropViewListener.onRightCurMoveEnd(rightCur);
                }
                else if(selected == 3) {
                    videoCropViewListener.onPolzunocMoveEnd(polzunoc);
                }
                if (rightCur < polzunoc || leftCur > polzunoc) {
                    videoCropViewListener.onPolzunocMoveEnd(polzunoc);
                }
                invalidate();

                break;
            }
        }
        return true;
    }
    void setProgress(float progress) {
        float vWidth = getWidth() - 2 * borderWidth;
        float vLeftCur = (leftCur+leftCurTemp - leftest) / (rightest - leftest) * (vWidth);
        float vRightCur = (rightCur+rightCurTemp - leftest) / (rightest - leftest) * (vWidth) + borderWidth;
        if (polzunoc < leftCur) {
           //if(videoCropViewListener != null) {
           //    progress = leftCur;
          //     videoCropViewListener.leftOverflow(progress);
           //}
        }
       else if (progress > rightCur) {
          if (videoCropViewListener != null) {
              progress = rightCur;
              videoCropViewListener.rightOverflow(progress);
          }
       }
      //  }
      //  else {
            polzunoc = progress;
      //  }
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("debug1425", "i am here 9:" + leftCurTemp);
        float vWidth = getWidth() - 2 * borderWidth;
        float vHeight = getHeight();

        float borderWidthHalf = borderWidth / 2.0f;
        float vLeftCur = (leftCur+leftCurTemp - leftest) / (rightest - leftest) * (vWidth);
        float vRightCur = (rightCur+rightCurTemp - leftest) / (rightest - leftest) * (vWidth) + borderWidth;
        float vPolzunoc = (polzunoc+polzunocTemp - leftest) / (rightest - leftest) * (vWidth) + borderWidth;

        float imW = imagePos.getIntrinsicWidth();
        float loupe = 1.0f * getHeight() * 3 / 4/ imagePos.getIntrinsicHeight();
        for(int curLeft = 0, j = 0; curLeft < getWidth(); curLeft += loupe * imW, j++) {
            if (videoMoments.get(j) != null) {
                Drawable d = videoMoments.get(j);
                d.setBounds(
                        curLeft
                        , getHeight() / 4
                        , (int) (loupe * imW) + curLeft
                        , (int) (getHeight()));
                d.draw(canvas);
            }
            else {
                imagePos.setBounds(
                        curLeft
                        , getHeight() / 4
                        , (int) (loupe * imW) + curLeft
                        , (int) (getHeight()));
                imagePos.draw(canvas);
            }
        }

        canvas.drawRect(0, getHeight() / 4, vLeftCur, getHeight(), shadowPaint);
        canvas.drawRect(vRightCur, getHeight() / 4, getWidth(), getHeight(), shadowPaint);
        canvas.drawLine(vLeftCur,getHeight() / 4, vRightCur + borderWidth, getHeight() / 4, framePaint);
        canvas.drawLine(vLeftCur,getHeight(), vRightCur + borderWidth, getHeight(), framePaint);

        canvas.drawRect(vLeftCur
                , getHeight() / 4
                , vLeftCur + borderWidth
                , vHeight
                , framePaint);

        canvas.drawRect(vRightCur
                , getHeight() / 4
                , vRightCur + borderWidth
                , vHeight
                , framePaint);
        imgleftCur.setBounds(
                (int)(vLeftCur)
                , getHeight() / 4
                , (int)(vLeftCur + borderWidth)
                , (int)(vHeight));
        imgleftCur.draw(canvas);
        imgRightCur.setBounds(
                (int)(vRightCur)
                , getHeight() / 4
                , (int)(vRightCur +  borderWidth)
                , (int)(vHeight));
        imgRightCur.draw(canvas);

        canvas.drawCircle(vPolzunoc, (int)borderWidth / 3, (int)borderWidth / 3 , polzunocPaint);
        canvas.drawLine(vPolzunoc, 0, vPolzunoc, getHeight(), polzunocPaint);

    }


}
