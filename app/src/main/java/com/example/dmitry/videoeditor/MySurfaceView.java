package com.example.dmitry.videoeditor;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    double x1;
    double y1;
    double x2;
    double y2;

    public MySurfaceView(Context context, Uri inputUri) {
        super(context);
        getHolder().addCallback(this);
        this.cR = context.getContentResolver();
        this.context = context;
        this.inputUri = inputUri;
        this.setOnTouchListener(this);


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

    public Bitmap getImageBitmap() {
        return bitmap;

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

    public void updateImage(Bitmap freshImage) {
        bitmap = freshImage;
        draw(surfaceHolder);

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
                bitmap = MediaStore.Images.Media.getBitmap(cR, inputUri);
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

        draw(surfaceHolder);
        return true;

    }

    private void draw(SurfaceHolder surfaceHolder) {

        if (cR.getType(inputUri).equals("video/mp4")) {

        } else {
            Surface surface = surfaceHolder.getSurface();
            Canvas canvas = surface.lockCanvas(null);
            Paint paint = new Paint();

            canvas.drawColor(Color.BLACK);

            Matrix matrix = new Matrix();
            canvas.drawBitmap(bitmap, matrix, paint);

            paint.setARGB(128, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawRect((float) x1, (float) y1, (float) x2, (float) y2, paint);
            surface.unlockCanvasAndPost(canvas);
        }

    }

}
