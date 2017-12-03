package edu.example.dmitry.videoeditor;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
        void onScaleEnd(float scale);
    }

    public interface OnMoveListener {
        void onMove(float dx, float dy);
        void onMoveEnd(float dx, float dy);
    }

    public interface OnRotateListener {
        void onRotate(float alphaNotDegree);
        void onRotateEnd(float alphaNotDegree);
    }

    public OnScaleListener scaleListener;
    public OnMoveListener moveListener;
    public OnRotateListener rotateListener;
    public OnClickListener clickListener;

    public void setMoveListener(OnMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setScaleListener(OnScaleListener scaleListener) {
        this.scaleListener = scaleListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    public void setOnRotateListener(OnRotateListener onRotateListener) {
        this.rotateListener = onRotateListener;
    }

    public ImageEventTransformator() {

    }

    private int imgLeftX;
    private int imgTopY;
    private int imgScale;
    private int imgWidth;
    private int imgHeight;

    private int alignLeft;
    private int alighRight;

    private int fingerTouchsCount = 0;
    View.OnTouchListener listener;

    public void setListener(View.OnTouchListener listener) {
        this.listener = listener;

    }

    private HashMap<Integer, Float> fingerTouchX = new HashMap<>();
    private HashMap<Integer, Float> fingerTouchY = new HashMap<>();

    private HashMap<Integer, Float>fingerMoveX = new HashMap<>();
    private HashMap<Integer, Float> fingerMoveY = new HashMap<>();

    private HashMap<Integer, Float> fingerDetouchX = new HashMap<>();
    private HashMap<Integer, Float> fingerDetouchY = new HashMap<>();

    private int dx = 0;
    private int dy = 0;

    public boolean onTouch(View view, MotionEvent motionEvent, int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        boolean res = onTouch(view, motionEvent);
        this.dx = 0;
        this.dy = 0;
        return res;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int mark = motionEvent.getActionMasked();
        int count = motionEvent.getPointerCount();
        int index = motionEvent.getActionIndex();


        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d("1130", "ACTION_DOWN");
                fingerTouchsCount = 1;
                for (int i = 0; i < count; i++) {
                    fingerTouchX.put(i, motionEvent.getX(i) + dx);
                    fingerTouchY.put(i, motionEvent.getY(i) + dy);
                }
                if(clickListener != null && count == 1) {
                    clickListener.onClick(fingerTouchX.get(0), fingerTouchY.get(0));
                }
                break;

            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.d("1130", "ACTION_POINTER_DOWN");
                fingerTouchsCount++;
                for (int i = 0; i < count; i++) {
                    fingerTouchX.put(i, motionEvent.getX(i) + dx);
                    fingerTouchY.put(i, motionEvent.getY(i) + dy);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                Log.d("1130", "ACTION_MOVE");
                for (int i = 0; i < count; i++) {
                    fingerMoveX.put(i, motionEvent.getX(i) + dx);
                    fingerMoveY.put(i, motionEvent.getY(i) + dy);
                }
                if(moveListener != null && count == 1) {
                    moveListener.onMove(fingerMoveX.get(0) - fingerTouchX.get(0)
                            , fingerMoveY.get(0) - fingerTouchY.get(0));
                }
                else if (count == 2) {
                    if(scaleListener != null) {
                        scaleListener.onScale(Tools.getLoupe(
                                fingerTouchX.get(0), fingerTouchY.get(0)
                                , fingerTouchX.get(1), fingerTouchY.get(1)
                                , fingerMoveX.get(0), fingerMoveY.get(0)
                                , fingerMoveX.get(1), fingerMoveY.get(1)));
                    }
                    if(rotateListener != null) {
                      rotateListener.onRotate(Tools.getAlpha(
                              fingerTouchX.get(0), fingerTouchY.get(0)
                              , fingerTouchX.get(1), fingerTouchY.get(1)
                              , fingerMoveX.get(0), fingerMoveY.get(0)
                              , fingerMoveX.get(1), fingerMoveY.get(1)));
                    }
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                Log.d("1130", "ACTION_POINTER_UP");
                for (int i = 0; i < count; i++) {
                    fingerMoveX.put(i, motionEvent.getX(i) + dx);
                    fingerMoveY.put(i, motionEvent.getY(i) + dy);
                }
                if(scaleListener != null) {
                    scaleListener.onScaleEnd(Tools.getLoupe(
                            fingerTouchX.get(0), fingerTouchY.get(0)
                            , fingerTouchX.get(1), fingerTouchY.get(1)
                            , fingerMoveX.get(0), fingerMoveY.get(0)
                            , fingerMoveX.get(1), fingerMoveY.get(1)));
                }
                if(rotateListener != null) {
                    rotateListener.onRotateEnd(Tools.getAlpha(
                            fingerTouchX.get(0), fingerTouchY.get(0)
                            , fingerTouchX.get(1), fingerTouchY.get(1)
                            , fingerMoveX.get(0), fingerMoveY.get(0)
                            , fingerMoveX.get(1), fingerMoveY.get(1)));
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Log.d("1130", "ACTION_UP");
                for (int i = 0; i < count; i++) {
                    fingerMoveX.put(i, motionEvent.getX(i) + dx);
                    fingerMoveY.put(i, motionEvent.getY(i) + dy);
                    fingerDetouchX.put(i, motionEvent.getX(i) + dx);
                    fingerDetouchY.put(i, motionEvent.getY(i) + dy);
                }
                if (moveListener != null) {
                    moveListener.onMoveEnd(fingerMoveX.get(0) - fingerTouchX.get(0)
                            , fingerMoveY.get(0) - fingerTouchY.get(0));
                }
                break;
            }
        }
        if(listener != null) {
            listener.onTouch(view, motionEvent);
        }
        return true;
    }

}
