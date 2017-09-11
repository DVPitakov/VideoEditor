package com.example.dmitry.videoeditor;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

import static java.lang.Math.sqrt;
import static java.util.Collections.swap;

/**
 * Created by dmitry on 08.09.17.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    DrawingThread drawingThread;
    MediaPlayer mediaPlayer;
    Context context;
    Uri inputUri;
    SurfaceHolder surfaceHolder;
    ContentResolver cR;
    Bitmap bitmap;
    ImageHolder imageHolder;
    ImageEditorQueue imageEditorQueue;
    ImageElement selectedImageElement;

    double x1;
    double y1;
    double x2;
    double y2;

    float fingerX1;
    float fingerY1;
    float fingerX2;
    float fingerY2;

    float fingernX1;
    float fingernY1;
    float fingernX2;
    float fingernY2;

    float alignLeft = 0.0f;
    float alignTop = 0.0f;
    float loupeX = 1.0f;
    float loupeY = 1.0f;
    float oldLoupeX = 1.0f;
    float oldLoupeY = 1.0f;

    float alignLeftOld = 0.0f;
    float alignTopOld = 0.0f;

    float elX;
    float elY;


    boolean isKrop = false;

    public MySurfaceView(Context context, Uri inputUri, ImageHolder imageHolder) {
        super(context);
        getHolder().addCallback(this);
        this.imageHolder = imageHolder;
        this.cR = context.getContentResolver();
        this.context = context;
        this.inputUri = inputUri;
        this.setOnTouchListener(this);
        imageEditorQueue = new ImageEditorQueue();


    }

    public void kropClear() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;

    }
    public int getMediaPlayerCurrentPosition() {
        if(mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        else {
            return -1;
        }

    }

    public int getMediaPlayerDuration() {
        if(mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        else {
            return -1;
        }

    }

    public Rect getKropRect() {
        if(x1 > x2) {
            double x = x1;
            x1 = x2;
            x2 = x;
        }
        if (y1 > y2) {
            double y = y1;
            y1 = y2;
            y2 = y;
        }
        int left = (int)((x1) / loupeX - alignLeft);
        int top = (int)((y1) / loupeY - alignTop);
        int right = (int)((x2) / loupeX - alignLeft);
        int bottom = (int)((y2) / loupeY - alignTop);
        Rect resultRect = new Rect(left, top, right, bottom);
        return resultRect;

    }

    public void mediaPlayerStart() {
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }

    }

    public void mediaPlayerPause() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public boolean mediaPlayerIsPlaying() {
            return (mediaPlayer != null) && mediaPlayer.isPlaying();

    }

    public void addImageElement(ImageElement imageElement) {
        imageEditorQueue.addElement(imageElement);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        try {
            if (cR.getType(inputUri).equals("video/mp4")) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, inputUri);
                mediaPlayer.setSurface(surfaceHolder.getSurface());
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        Log.d("db", "next");
                    }
                });

            } else {
                bitmap = imageHolder.getDefaultBitmap();
                int iw = bitmap.getWidth();
                int ih = bitmap.getHeight();
                int tw = this.getWidth();
                int th = this.getHeight();

                alignLeft = (float)(tw - iw * loupeX) / 2;
                alignTop = (float)(th - ih * loupeY) / 2;

                alignLeftOld = alignLeft;
                alignTopOld = alignTop;

            }
            draw(surfaceHolder);
        }
        catch (IOException e) {
            Log.d("exc", "IOExcepiton in MySurfaceView.surfaceCreated");
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int mark = motionEvent.getActionMasked();
        int count = motionEvent.getPointerCount();
        int index = motionEvent.getActionIndex();


            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();
                    x2 = x1;
                    y2 = y1;
                    fingerX1 = motionEvent.getX();
                    fingerY1 = motionEvent.getY();
                    if(count > 1) {
                        fingerX2 = motionEvent.getX(1);
                        fingerY2 = motionEvent.getY(1);
                    }
                    if(count == 1) {
                        selectedImageElement = imageEditorQueue.find((int)((x1- alignLeft)/loupeX),
                                (int)((y1 - alignTop)/loupeY));
                    }
                    Log.d("101525: ", "ACTION_DOWN");
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    if(isKrop == false && count == 1) {
                        if(selectedImageElement == null) {
                            alignLeft = (float) (alignLeftOld + x2 - x1);
                            alignTop = (float) (alignTopOld + y2 - y1);
                        }
                        else {
                            selectedImageElement.setLeft((int)((x2 - x1)/loupeX));
                            selectedImageElement.setTop((int)((y2 - y1)/loupeY));
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN: {
                    fingerX2 = motionEvent.getX(1);
                    fingerY2 = motionEvent.getY(1);
                    Log.d("101525", "ACTION_POINTER_DOWN");
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();

                    alignLeftOld = alignLeft;
                    alignTopOld = alignTop;

                    if (selectedImageElement != null) {
                        selectedImageElement.saveLeft();
                        selectedImageElement.saveTop();
                    }

                    Log.d("db", "ACTION_UP");
                    break;
                }
            }
        if (count == 2) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_MOVE: {

                    fingernX1 = motionEvent.getX(0);
                    fingernY1 = motionEvent.getY(0);
                    fingernX2 = motionEvent.getX(1);
                    fingernY2 = motionEvent.getY(1);


                    if (selectedImageElement != null) {
                        Log.d("step", "selectedImageElement != null");
                        //if(selectedImageElement.getClass() == TextImage.class) {
                            ((TextImage)selectedImageElement).setTextSize(((fingernX2 - fingernX1) / (fingerX2 - fingerX1)));
                        //}
                    }
                    else {
                        loupeX = ((fingernX2 - fingernX1) / (fingerX2 - fingerX1)) * oldLoupeX;
                        loupeY = loupeX;
                    }

                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    oldLoupeX = loupeX;
                    oldLoupeY = loupeY;
                    if (selectedImageElement != null) {
                        //SUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU
                        ((TextImage)selectedImageElement).saveTextSize();
                    }

                    break;
                }
            }
        }
        draw(surfaceHolder);
        return true;

    }

    public void draw() {
        draw(surfaceHolder);

    }
    private void draw(SurfaceHolder surfaceHolder) {

        if (cR.getType(inputUri).equals("video/mp4")) {

        } else {
            Surface surface = surfaceHolder.getSurface();
            Canvas canvas = surface.lockCanvas(null);
            Paint paint = new Paint();

            canvas.drawColor(Color.BLACK);

            Bitmap bitmap = imageHolder.getFreshBitmap();
            bitmap = imageEditorQueue.draw(bitmap);



            int iw = bitmap.getWidth();
            int ih = bitmap.getHeight();
            int tw = this.getWidth();
            int th = this.getHeight();


            bitmap = Bitmap.createScaledBitmap(bitmap, (int)(iw * loupeX), (int)(ih * loupeY), true);

            canvas.drawBitmap(bitmap, alignLeft, alignTop, paint);
            paint.setARGB(128, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            if (isKrop) {
                canvas.drawRect((float) x1, (float) y1, (float) x2, (float) y2, paint);
            }
            surface.unlockCanvasAndPost(canvas);
        }

    }

}
