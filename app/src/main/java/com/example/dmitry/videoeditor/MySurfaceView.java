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

    double x1;
    double y1;
    double x2;
    double y2;

    double fingerX1;
    double fingerY1;
    double fingerX2;
    double fingerY2;

    float alignLeft = 0.0f;
    float alignTop = 0.0f;
    float loupe = 1.0f;

    public MySurfaceView(Context context, Uri inputUri, ImageHolder imageHolder) {
        super(context);
        getHolder().addCallback(this);
        this.imageHolder = imageHolder;
        this.cR = context.getContentResolver();
        this.context = context;
        this.inputUri = inputUri;
        this.setOnTouchListener(this);


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
        Log.d("trol", "tg1");
        Rect resultRect = new Rect((int)(x1 - alignLeft),
                (int)(y1 - alignTop),
                (int)(x2 - alignLeft),
                (int)(y2 - alignTop));
        Log.d("trol", "tg2");
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

        if (count == 1) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();
                    x2 = x1;
                    y2 = y1;
                    Log.d("db", "ACTION_DOWN");
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    Log.d("db", "ACTION_MOVE");
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    Log.d("db", "ACTION_UP");
                    break;
                }
            }
        }
        else if (count == 2) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    if(index == 0) {

                    }
                    else if(index == 1) {

                    }
                    Log.d("db", "ACTION_MOVE");
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
            int iw = bitmap.getWidth();
            int ih = bitmap.getHeight();
            int tw = this.getWidth();
            int th = this.getHeight();

            alignLeft = (float)(tw - iw * loupe) / 2;
            alignTop = (float)(th - ih * loupe) / 2;
            bitmap = Bitmap.createScaledBitmap(bitmap, (int)(iw * loupe), (int)(ih * loupe), true);

            canvas.drawBitmap(bitmap, alignLeft, alignTop, paint);
            paint.setARGB(128, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawRect((float) x1, (float) y1, (float) x2, (float) y2, paint);
            surface.unlockCanvasAndPost(canvas);
        }

    }

}
