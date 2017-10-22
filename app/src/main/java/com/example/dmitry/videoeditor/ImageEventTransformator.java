package com.example.dmitry.videoeditor;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dmitry on 21.10.17.
 */

public class ImageEventTransformator implements View.OnTouchListener {
    public interface OnClickListener {
        void onClick(float dx, float dy);
    }

    public interface OnScaleListener {
        void onScale(float scale);
    }

    public interface OnMoveListener {
        void onMove(float dx, float dy);
    }

    public interface OnRotateListener {
        void onRotate(float alphaNotDegree);
    }

    public OnScaleListener scaleListener;
    public OnMoveListener moveListener;
    public OnRotateListener rotateListener;
    public OnClickListener clickListener;

    public ImageEventTransformator() {}

    private int imgLeftX;
    private int imgTopY;
    private int imgScale;
    private int imgWidth;
    private int imgHeight;

    private int fingerTouchsCount = 0;

    private HashMap<Integer, Float> fingerTouchX = new HashMap<>();
    private HashMap<Integer, Float> fingerTouchY = new HashMap<>();

    private HashMap<Integer, Float>fingerMoveX = new HashMap<>();
    private HashMap<Integer, Float> fingerMoveY = new HashMap<>();

    private HashMap<Integer, Float> fingerDetouchX = new HashMap<>();
    private HashMap<Integer, Float> fingerDetouchY = new HashMap<>();



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int mark = motionEvent.getActionMasked();
        int count = motionEvent.getPointerCount();
        int index = motionEvent.getActionIndex();


        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                fingerTouchsCount = 1;
                for (int i = 0; i < count; i++) {
                    fingerTouchX.put(i, motionEvent.getX(i));
                    fingerTouchY.put(i, motionEvent.getY(i));
                }
                if(clickListener != null && count == 1) {
                    clickListener.onClick(fingerTouchX.get(0), fingerTouchY.get(0));
                }
                break;

            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                fingerTouchsCount++;
                for (int i = 0; i < count; i++) {
                    fingerTouchX.put(i, motionEvent.getX(i));
                    fingerTouchY.put(i, motionEvent.getY(i));
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < count; i++) {
                    fingerMoveX.put(i, motionEvent.getX(i));
                    fingerMoveY.put(i, motionEvent.getY(i));
                }
                if(moveListener != null && count == 1) {
                    moveListener.onMove(fingerMoveX.get(0) - fingerTouchX.get(0)
                            , fingerMoveX.get(0) - fingerTouchX.get(0));
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                for (int i = 0; i < count; i++) {
                    fingerMoveX.put(i, motionEvent.getX(i));
                    fingerMoveY.put(i, motionEvent.getY(i));
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                for (int i = 0; i < count; i++) {
                    fingerMoveX.put(i, motionEvent.getX(i));
                    fingerMoveY.put(i, motionEvent.getY(i));
                }
                break;
            }
        }
        return true;
    }

}
