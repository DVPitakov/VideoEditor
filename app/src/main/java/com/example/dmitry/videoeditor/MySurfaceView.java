package com.example.dmitry.videoeditor;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.dmitry.videoeditor.Holders.CurrentElementHolder;
import com.example.dmitry.videoeditor.Holders.CurrentVideoHolder;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.Holders.UrlHolder;
import com.example.dmitry.videoeditor.Vidgets.IconImage;
import com.example.dmitry.videoeditor.Vidgets.ImageEditorQueue;
import com.example.dmitry.videoeditor.Vidgets.ImageElement;
import com.example.dmitry.videoeditor.Vidgets.KropFrame;
import com.example.dmitry.videoeditor.Vidgets.RisunocImage;
import com.example.dmitry.videoeditor.Vidgets.TextImage;

import java.io.IOException;

/**
 * Created by dmitry on 08.09.17.
 */

public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, View.OnTouchListener,
        CurrentVideoHolder.VideoShower {

    private long oldClickTime;
    private long newClickTime;
    private KropFrame kropFrame;
    private int iw;
    private int ih;

    @Override
    public void showNewVideo() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(UrlHolder.getInpurUrl() + "tmp.mp4"));
            mediaPlayer.setSurface(surfaceHolder.getSurface());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCurrentItem() {
        imageEditorQueue.deleteElement(CurrentElementHolder.getInstance().getCurrentElement());
        CurrentElementHolder.getInstance().removeCurrentElement();
        selectedImageElement = null;
        if(focusListener != null) {
            focusListener.focusLosed();
        }
    }

    public interface FocusListener {
        void focusLosed();
        void focusTaken();
        void doubleClick();
    }

    public void setFocusListener(FocusListener listener) {
        focusListener = listener;

    }
    private void focusLosed() {
        if(focusListener != null) {
            focusListener.focusLosed();
            oldClickTime = 0;
        }
    }

    private void focusTaken() {
        if(focusListener != null) {
            oldClickTime = newClickTime;
            newClickTime = System.currentTimeMillis();
            if((newClickTime - oldClickTime) < 250) {
                focusListener.doubleClick();
            }
            else {
                focusListener.focusTaken();
            }
        }
    }

    MediaPlayer mediaPlayer;
    Context context;
    SurfaceHolder surfaceHolder;
    ContentResolver cR;
    public ImageEditorQueue imageEditorQueue;
    public ImageElement selectedImageElement;
    public ImageEventTransformator eventTransformator = new ImageEventTransformator();

    int effect = 0;

    double x1;
    double y1;
    double x2;
    double y2;

    private float alignLeft = 0.0f;
    private float alignTop = 0.0f;
    private float loupeX = 1.0f;
    private float loupeY = 1.0f;

    float alignLeftOld = 0.0f;
    float alignTopOld = 0.0f;

    FocusListener focusListener;

    boolean isKrop = false;

    public void setEffect(int i) {
        effect = i;

    }

    private ImageEventTransformator.OnClickListener clickListener
            = new ImageEventTransformator.OnClickListener() {
        @Override
        public void onClick(float dx, float dy) {
            x1 = dx;
            y1 = dy;
            x2 = 0;
            y2 = 0;
            ImageElement imageElement = selectedImageElement;
            selectedImageElement =
                    imageEditorQueue.find((int)((dx/loupeX - alignLeft)),
                    (int)((dy/loupeY - alignTop)));
            if(selectedImageElement == null) {
                if(imageElement instanceof RisunocImage
                        && !((RisunocImage)imageElement).isReady()) {
                    selectedImageElement = imageElement;
                }
                else {
                    focusLosed();
                    CurrentElementHolder.getInstance().removeCurrentElement();
                }
            }
            else {
                focusTaken();
                CurrentElementHolder.getInstance().setCurrentElement(selectedImageElement);
            }
            if (isKrop) {
                kropFrame.onClick((int)(dx/loupeX), (int)(dy/loupeY));
            }
        }
    };

    private ImageEventTransformator.OnScaleListener scaleListener = new ImageEventTransformator.OnScaleListener() {
        @Override
        public void onScale(float scale) {
            if (selectedImageElement != null) {
                if(selectedImageElement.getClass() == TextImage.class) {
                    ((TextImage)selectedImageElement).setTextSize(
                            Tools.normalizator(scale, 6, (float)0.2));
                }
                else if (selectedImageElement instanceof IconImage) {
                    ((IconImage)selectedImageElement).setImageSize(
                            Tools.normalizator(scale, 6, (float)0.2));
                }
            }
            else {
                loupeX = scale;
                loupeY = loupeX;
                loupeX = Tools.normalizator(loupeX, 4, (float)0.2);
                loupeY = Tools.normalizator(loupeY, 4, (float)0.2);
                ImageHolder.getInstance().setScaledBitmap(null);
            }
        }

        @Override
        public void onScaleEnd(float scale) {
            if (selectedImageElement != null) {
                if(selectedImageElement.getClass() == TextImage.class) {
                    ((TextImage)selectedImageElement).saveTextSize();
                }
                else if (selectedImageElement instanceof  IconImage) {
                    ((IconImage)selectedImageElement).saveImageSize();
                }

            }
        }
    };

    public PointF getCenter() {
        PointF p = new PointF();
        p.x = getWidth()  / loupeX / 2 - alignLeft;
        p.y = getHeight()  / loupeY / 3 - alignTop ;
        return p;
    }

    private ImageEventTransformator.OnMoveListener moveListener = new ImageEventTransformator.OnMoveListener() {
        @Override
        public void onMove(float dx, float dy) {
            x2 = dx;
            y2 = dy;
            if(!isKrop || !kropFrame.moveCanched()) {
                if (isKrop) {
                    kropFrame.move((int)(dx / loupeX), (int)(dy / loupeY));
                }
                if(selectedImageElement == null) {
                    alignLeft = (float) (alignLeftOld + x2/loupeX);
                    alignTop = (float) (alignTopOld + y2/loupeY);
                }
                else if (selectedImageElement instanceof RisunocImage
                        && !((RisunocImage)selectedImageElement).isReady()) {

                }
                else{
                    selectedImageElement.setLeft((int)((x2/loupeX)));
                    selectedImageElement.setTop((int)((y2/loupeY)));
                    ImageHolder.getInstance().setBitmapWithElements(null);
                }
            }
            else {
                kropFrame.onMove((int)((x1 + x2)/loupeX), (int)((y1 + y2)/loupeY));
                draw();
            }
            if (selectedImageElement != null
                    && selectedImageElement.getClass() == RisunocImage.class
                    && !((RisunocImage)selectedImageElement).isReady()) {
                ((RisunocImage)selectedImageElement)
                        .addDrawingPoint(
                                (int)((x2 + x1) / loupeX - alignLeft)
                        , (int)((y2 + y1) / loupeY  - alignTop));
                ImageHolder.getInstance().setBitmapWithElements(null);
                draw();
            }

        }

        @Override
        public void onMoveEnd(float dx, float dy) {
            x2 = dx;
            y2 = dy;

            alignLeftOld = alignLeft;
            alignTopOld = alignTop;
            if (selectedImageElement != null) {
                selectedImageElement.saveLeft();
                selectedImageElement.saveTop();
                ImageHolder.getInstance().setBitmapWithElements(null);
                if(selectedImageElement instanceof RisunocImage) {
                    ((RisunocImage)selectedImageElement).newLine();
                }
            }
            if (isKrop) {
                kropFrame.onDetouch();
            }
        }
    };

    private ImageEventTransformator.OnRotateListener rotateListener = new ImageEventTransformator.OnRotateListener() {
        @Override
        public void onRotate(float alphaNotDegree) {
            if (selectedImageElement != null) {
                selectedImageElement.setAlpha(alphaNotDegree);
                ImageHolder.getInstance().setBitmapWithElements(null);
            }
        }

        @Override
        public void onRotateEnd(float alphaNotDegree) {
            if (selectedImageElement != null) {
                selectedImageElement.saveAlpha();
            }
        }
    };

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.cR = context.getContentResolver();
        this.context = context;
        this.setOnTouchListener(this);
        imageEditorQueue = ImageEditorQueue.getInstance();
        eventTransformator.setMoveListener(moveListener);
        eventTransformator.setScaleListener(scaleListener);
        eventTransformator.setOnRotateListener(rotateListener);
        eventTransformator.setOnClickListener(clickListener);


    }

    public void kropClear() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;

    }
    public int getMediaPlayerCurrentPosition() {
        if(mediaPlayer != null) {
            mediaPlayer.getDuration();
            return mediaPlayer.getCurrentPosition();
        }
        else {
            return -1;
        }

    }


    public Rect getKropRect() {
        Rect kropRect = kropFrame.getRect();
        int left = (int)((kropRect.left) - alignLeft);
        int top = (int)((kropRect.top) - alignTop);
        int right = (int)((kropRect.right) - alignLeft);
        int bottom = (int)((kropRect.bottom) - alignTop);
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

    public void kropSet() {
        isKrop = true;
        kropFrame = new KropFrame(
                (int)alignLeft
                ,(int)alignTop
                ,(int)(iw + alignLeft)
                ,(int)(ih + alignTop)
                ,context);
        draw();

    }
    public void kropUnset() {
        isKrop = false;
        draw();

    }
    public void addImageElement(ImageElement imageElement) {
        imageEditorQueue.addElement(imageElement);
        selectedImageElement = imageElement;
        focusListener.focusTaken();

    }

    public void setImageText(String text) {
        if (selectedImageElement != null) {
            if(selectedImageElement.getClass() == TextImage.class) {
                ((TextImage)selectedImageElement).setText(text);
            }
            draw();

        }
    }

    public void updateVideo(Uri uri) {
        mediaPlayer.stop();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(UrlHolder.getInpurUrl()));
            mediaPlayer.setSurface(surfaceHolder.getSurface());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    Log.d("db", "next");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        SurfaceViewHolder.getInstance().setMySurfaceView(this);
        CurrentVideoHolder.getInstance().setCurrentMediaPlayer(this);
        this.surfaceHolder = surfaceHolder;
        try {
            if (Tools.isVideo(UrlHolder.getInpurUrl())) {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, UrlHolder._getInputUri());
                mediaPlayer.setSurface(surfaceHolder.getSurface());
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                CurrentVideoHolder.getInstance().setVideoLen(mediaPlayer.getDuration());
                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        Log.d("db", "next");
                    }
                });
            }
            else {
                ImageHolder.getInstance().setMaxImageSize(getWidth(), getHeight());
                int iw = ImageHolder.getInstance().getDefaultBitmap().getWidth();
                int ih = ImageHolder.getInstance().getDefaultBitmap().getHeight();
                int tw = this.getWidth();
                int th = this.getHeight();

                alignLeft = (float)(tw - iw * loupeX) / 2;
                alignTop = (float)(th - ih * loupeY) / 2;

                alignLeftOld = alignLeft;
                alignTopOld = alignTop;


                draw(surfaceHolder);
            }
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
        eventTransformator.onTouch(view, motionEvent);
        draw(surfaceHolder);
        return true;

    }

    public void  setImageColor(int color) {
        if (selectedImageElement != null && selectedImageElement.getClass() == TextImage.class) {
            ((TextImage)selectedImageElement).setColor(color);
        }
        if (selectedImageElement != null && selectedImageElement.getClass() == RisunocImage.class) {
            ((RisunocImage)selectedImageElement).beginNewLineGroop(color);
        }
    }

    public void draw() {
        draw(surfaceHolder);

    }
    private void draw(SurfaceHolder surfaceHolder) {

        if (Tools.isNotVideo(UrlHolder.getInpurUrl())) {
            Surface surface = surfaceHolder.getSurface();
            Canvas canvas = surface.lockCanvas(null);
            Paint paint = new Paint();
            canvas.drawColor(Color.BLACK);

            Bitmap scaledBitmap = ImageHolder.getInstance().getScaledBitmap();
            Bitmap bitmapWithElements;
            if (scaledBitmap == null) {
                bitmapWithElements = ImageHolder.getInstance().getBitmapWithElements();
                if(bitmapWithElements == null) {
                    Bitmap freshBitmap = ImageHolder.getInstance().getFreshBitmap();
                    if(freshBitmap == null) {
                        Bitmap kropedBitmap = ImageHolder.getInstance().getKropedBitmap();
                        if(kropedBitmap == null) {
                            kropedBitmap = ImageHolder.getInstance().getDefaultBitmap();
                            ImageHolder.getInstance().setKropedBitmap(kropedBitmap);
                        }
                        alignLeft = getWidth() / loupeX / 2 - kropedBitmap.getWidth() / 2;
                        alignTop = getHeight() / loupeY / 2 - kropedBitmap.getHeight() / 2;
                        alignLeftOld = getWidth() / loupeX / 2 - kropedBitmap.getWidth() / 2;
                        alignTopOld = getHeight() /loupeY / 2 - kropedBitmap.getHeight() / 2;
                        switch (effect) {
                            case 0: {
                                freshBitmap = kropedBitmap;
                                break;
                            }
                            case 1: {
                                freshBitmap = ImageEditor.bombit(kropedBitmap);
                                break;
                            }
                            case 2: {
                                freshBitmap = ImageEditor.bombit2(kropedBitmap);
                                break;
                            }
                            case 3: {
                                freshBitmap = ImageEditor.inversion(kropedBitmap);
                                break;
                            }
                            default: {
                                freshBitmap = kropedBitmap;
                            }
                        }

                        ImageHolder.getInstance().setFreshBitmap(freshBitmap);
                    }
                    bitmapWithElements = imageEditorQueue.draw(freshBitmap);
                    ImageHolder.getInstance().setBitmapWithElements(bitmapWithElements);
                }
                iw = bitmapWithElements.getWidth();
                ih = bitmapWithElements.getHeight();
                //scaledBitmap = Bitmap.createScaledBitmap(bitmapWithElements, (int) (iw * loupeX), (int) (ih * loupeY), true);
                //ImageHolder.getInstance().setScaledBitmap(scaledBitmap);
                scaledBitmap = bitmapWithElements;
            }
            canvas.scale(loupeX, loupeY);
            canvas.drawBitmap(scaledBitmap, alignLeft, alignTop, paint);

            paint.setARGB(128, 255, 0, 0);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            if (isKrop) {
                kropFrame.draw(canvas);

            }
            surface.unlockCanvasAndPost(canvas);
        }

    }

}
