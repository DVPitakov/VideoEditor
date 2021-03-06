package edu.example.dmitry.videoeditor.views;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import edu.example.dmitry.videoeditor.ImageEditor;
import edu.example.dmitry.videoeditor.ImageEventTransformator;
import edu.example.dmitry.videoeditor.SettingsVideo;
import edu.example.dmitry.videoeditor.Tools;
import edu.example.dmitry.videoeditor.holders.CurrentElementHolder;
import edu.example.dmitry.videoeditor.holders.CurrentVideoHolder;
import edu.example.dmitry.videoeditor.holders.HistoryHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.holders.UrlHolder;
import edu.example.dmitry.videoeditor.items.ImageItem;
import edu.example.dmitry.videoeditor.items.ImageEditorQueue;
import edu.example.dmitry.videoeditor.items.BaseItem;
import edu.example.dmitry.videoeditor.items.KropFrame;
import edu.example.dmitry.videoeditor.items.RisunocItem;
import edu.example.dmitry.videoeditor.items.TextItem;

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
    private int startPlay = 0;

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

    public void reset() {
        loupeY = 1;
        loupeX = 1;
        savedLoupe = 1;
    }

    public void canselKrop() {
        isKrop = false;
        kropFrame = null;
        kropClear();
        draw();
    }

    public void deleteCurrentItem() {
        HistoryHolder.getInstance().addAction(new HistoryHolder.DeleteItem(CurrentElementHolder.getInstance().getCurrentElement()));
        imageEditorQueue.deleteElement(CurrentElementHolder.getInstance().getCurrentElement());
        CurrentElementHolder.getInstance().removeCurrentElement();
        if(focusListener != null) {
            focusListener.focusLosed(true);
        }
    }

    public void setStartPlay(int startPlay) {
        this.startPlay = startPlay;
    }

    public void setImageParceErrorListener(ImageParceErrorListener imageParceErrorListener) {
        this.imageParceErrorListener = imageParceErrorListener;
    }


    public interface FocusListener {
        void focusLosed(boolean setDefaultMenu);
        void focusTaken();
        void doubleClick();
    }

    public void setFocusListener(FocusListener listener) {
        focusListener = listener;

    }
    private void focusLosed() {
        if(focusListener != null) {
            focusListener.focusLosed(true);
            oldClickTime = 0;
        }
    }

    private void focusTaken() {
        if(focusListener != null && !isKrop) {
            CurrentElementHolder.getInstance().getCurrentElement().setFocus();
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
    public ImageEventTransformator eventTransformator = new ImageEventTransformator();


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
        HistoryHolder.getInstance().addAction(new HistoryHolder.Effect(HistoryHolder.getInstance().lastEffect, i));
        HistoryHolder.getInstance().lastEffect = i;
    }

    private ImageEventTransformator.OnClickListener clickListener
            = new ImageEventTransformator.OnClickListener() {
        @Override
        public void onClick(float dx, float dy) {
            x1 = dx;
            y1 = dy;
            x2 = 0;
            y2 = 0;
            BaseItem baseItem = CurrentElementHolder.getInstance().getCurrentElement();
            CurrentElementHolder
                    .getInstance()
                    .setCurrentElement(imageEditorQueue.find(
                            (int)((dx/loupeX - alignLeft)),
                            (int)((dy/loupeY - alignTop))));

            if(CurrentElementHolder.getInstance().getCurrentElement() == null && !isKrop) {
                if(baseItem instanceof RisunocItem
                        && !((RisunocItem) baseItem).isReady()) {
                    CurrentElementHolder.getInstance().setCurrentElement(baseItem);
                }
                else {

                    focusLosed();
                    CurrentElementHolder.getInstance().removeCurrentElement();
                }
            }
            else {
                focusTaken();
            }
            if (isKrop) {
                kropFrame.onClick((int)(dx/loupeX), (int)(dy/loupeY));
            }
        }
    };

    private float savedLoupe = 1;
    private ImageEventTransformator.OnScaleListener scaleListener = new ImageEventTransformator.OnScaleListener() {
        @Override
        public void onScale(float scale) {
            if (CurrentElementHolder.getInstance().getCurrentElement() != null && !isKrop) {
                if(CurrentElementHolder.getInstance().getCurrentElement() instanceof TextItem) {
                    ((TextItem)CurrentElementHolder.getInstance().getCurrentElement()).setTextSize(
                            Tools.normalizator(scale, 6, (float)0.2));
                }
                else if (CurrentElementHolder.getInstance().getCurrentElement() instanceof ImageItem) {
                    ((ImageItem) CurrentElementHolder.getInstance().getCurrentElement()).setImageSize(
                            Tools.normalizator(scale, 6, (float)0.2));
                }
            }
            else {
                loupeX = savedLoupe * scale;
                loupeY = savedLoupe * scale;
                loupeX = Tools.normalizator(loupeX, 4, (float)0.2);
                loupeY = Tools.normalizator(loupeY, 4, (float)0.2);
                ImageHolder.getInstance().setScaledBitmap(null);
            }
        }

        @Override
        public void onScaleEnd(float scale) {
            if(CurrentElementHolder.getInstance().getCurrentElement() instanceof TextItem) {
                ((TextItem) CurrentElementHolder.getInstance().getCurrentElement()).saveTextSize();
            }
            else if (CurrentElementHolder.getInstance().getCurrentElement() instanceof ImageItem) {
                ((ImageItem) CurrentElementHolder.getInstance().getCurrentElement()).saveImageSize();
            }
            if (CurrentElementHolder.getInstance().getCurrentElement() != null)  {
                HistoryHolder.getInstance().addAction(new HistoryHolder.RotateAndScale(CurrentElementHolder.getInstance().getCurrentElement(), 0, scale));
            }
            else {
                savedLoupe = loupeX;
            }

        }
    };

    public PointF getCenter() {
        PointF p = new PointF();
        p.x = getWidth()  / loupeX / 2 - alignLeft;
        p.y = getHeight()  / loupeY / 2.5f - alignTop;
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
                if(CurrentElementHolder.getInstance().getCurrentElement() == null) {
                    alignLeft = (float) (alignLeftOld + x2/loupeX);
                    alignTop = (float) (alignTopOld + y2/loupeY);
                }
                else if (CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocItem
                        && !((RisunocItem)CurrentElementHolder.getInstance().getCurrentElement()).isReady() && !isKrop) {

                }
                else if (!isKrop && CurrentElementHolder.getInstance().getCurrentElement() != null) {
                    CurrentElementHolder.getInstance().getCurrentElement().move((int)((x2/loupeX)), (int)((y2/loupeY)));
                    ImageHolder.getInstance().setBitmapWithElements(null);
                }
            }
            else {
                kropFrame.onMove((int)((x1 + x2)/loupeX), (int)((y1 + y2)/loupeY));
                draw();
            }
            if (CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocItem
                    && !((RisunocItem)CurrentElementHolder.getInstance().getCurrentElement()).isReady()) {
                ((RisunocItem)CurrentElementHolder.getInstance().getCurrentElement())
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
            if (CurrentElementHolder.getInstance().getCurrentElement() != null) {
                if (CurrentElementHolder.getInstance().getCurrentElement().getActionType() == 1) {
                    HistoryHolder.getInstance().addAction(new HistoryHolder.Move(CurrentElementHolder.getInstance().getCurrentElement(), (int) dx, (int) dy));
                }
                HistoryHolder.Action action = CurrentElementHolder.getInstance().getCurrentElement().moveEnd();
                if(action != null) {
                    HistoryHolder.getInstance().addAction(action);
                }
                ImageHolder.getInstance().setBitmapWithElements(null);
                if(CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocItem) {
                    ((RisunocItem) CurrentElementHolder.getInstance().getCurrentElement()).newLine();
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
            if (CurrentElementHolder.getInstance().getCurrentElement() != null) {
                CurrentElementHolder.getInstance().getCurrentElement().setAlpha(alphaNotDegree);
                ImageHolder.getInstance().setBitmapWithElements(null);
            }
        }

        @Override
        public void onRotateEnd(float alphaNotDegree) {
            if (CurrentElementHolder.getInstance().getCurrentElement() != null) {
                HistoryHolder.Action action = CurrentElementHolder.getInstance().getCurrentElement().moveEnd();
                if(action != null) {
                    HistoryHolder.getInstance().addAction(action);
                }
                HistoryHolder.getInstance().addAction(new HistoryHolder.RotateAndScale(CurrentElementHolder.getInstance().getCurrentElement(), alphaNotDegree, 1));

            }
        }
    };

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        savedLoupe = 1;
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

    public void doKrop() {

        kropUnset();
        Rect rect = getKropRect();
        Bitmap kropedBitmap = ImageHolder.getInstance().getKropedBitmap();
        if (kropedBitmap == null) {
            kropedBitmap = ImageHolder.getInstance().getDefaultBitmap();
        }
        if (HistoryHolder.getInstance().lastKrop == null) {
            HistoryHolder.getInstance().lastKrop = new Rect(0
                    ,0
                    ,ImageHolder.getInstance().getDefaultBitmap().getWidth()
                    ,ImageHolder.getInstance().getDefaultBitmap().getHeight());
        }
        Rect last = HistoryHolder.getInstance().lastKrop;
        Rect recto = new Rect(
                last.left + rect.left
                , last.top + rect.top
                , rect.right
                , rect.bottom);
        HistoryHolder.getInstance().addAction(new HistoryHolder.Krop(
                HistoryHolder.getInstance().lastKrop
                , recto
        ));
        HistoryHolder.getInstance().lastKrop = recto;

        kropedBitmap = ImageEditor.krop(kropedBitmap,
                rect.left, rect.top, rect.right, rect.bottom);
        ImageHolder.getInstance().setKropedBitmap(kropedBitmap);
        imageEditorQueue.moveAll(-rect.left, -rect.top);
        kropClear();
        draw();
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
        BaseItem tg = CurrentElementHolder.getInstance().getCurrentElement();
        if (tg != null) {
            CurrentElementHolder.getInstance().getCurrentElement().focusLosed();
            CurrentElementHolder.getInstance().setCurrentElement(null);
            ImageHolder.getInstance().setBitmapWithElements(null);
        }

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
    public void addImageElement(BaseItem baseItem) {
        imageEditorQueue.addElement(baseItem);
        HistoryHolder.getInstance().addAction(new HistoryHolder.AddItem(baseItem));
        CurrentElementHolder.getInstance().setCurrentElement(baseItem);
        if (baseItem instanceof RisunocItem) {
            ((RisunocItem) baseItem).beginNewLineGroop(0xFFFFFFFF, 1);
        }
        focusListener.focusTaken();

    }

    public void setImageText(String text) {
        if (CurrentElementHolder.getInstance().getCurrentElement() != null) {
            if(CurrentElementHolder.getInstance().getCurrentElement() instanceof TextItem) {
                ((TextItem)CurrentElementHolder.getInstance().getCurrentElement()).setText(text);
            }
            draw();

        }
    }


    public void setImageSize(int imgSize) {
        if (CurrentElementHolder.getInstance().getCurrentElement() != null) {
            ((RisunocItem)CurrentElementHolder.getInstance().getCurrentElement()).beginNewLineGroop(null, imgSize);
            draw();

        }
    }

    public void pauseVideo() {
        mediaPlayerPause();
    }

    public void continueVideo(float pos) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo((int) (pos / 100f * mediaPlayer.getDuration()));
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    //;
                }
            });

        }
    }

    public interface ImageParceErrorListener {
        void onImageEpsent();
    }
    private ImageParceErrorListener imageParceErrorListener = null;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        SurfaceViewHolder.getInstance().setMySurfaceView(this);
        CurrentVideoHolder.getInstance().setCurrentMediaPlayer(this);
        if (surfaceHolder == null) {

        }
        this.surfaceHolder = surfaceHolder;
        try {
            if (Tools.isVideo(SettingsVideo.getInput(""))) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, SettingsVideo.getInput());
                mediaPlayer.setSurface(surfaceHolder.getSurface());
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
                CurrentVideoHolder.getInstance().setVideoLen(mediaPlayer.getDuration());
                mediaPlayer.start();
                mediaPlayer.pause();
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
        catch (NullPointerException e) {
            if(imageParceErrorListener != null) {
                imageParceErrorListener.onImageEpsent();
            }
            else {
                throw e;
            }
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
        eventTransformator.onTouch(view
                , motionEvent
                , (int)((loupeX - 1) * MySurfaceView.this.getWidth() / 2)
                , (int)((loupeX - 1) * MySurfaceView.this.getHeight() / 2));
        draw(surfaceHolder);
        return true;

    }

    public void  setImageColor(int color) {
        if (CurrentElementHolder.getInstance().getCurrentElement() instanceof TextItem) {
            ((TextItem) CurrentElementHolder.getInstance().getCurrentElement()).setColor(color);
        }
        if (CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocItem) {
            ((RisunocItem) CurrentElementHolder.getInstance().getCurrentElement()).beginNewLineGroop(color, null);
        }
    }

    public void draw() {
        draw(surfaceHolder);

    }
    private void draw(SurfaceHolder surfaceHolder) {

        if (Tools.isNotVideo(SettingsVideo.getInput(""))) {
            try {
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
                            alignLeft = getWidth()  / 2 - kropedBitmap.getWidth() / 2;
                            alignTop = getHeight()   / 2 - kropedBitmap.getHeight() / 2;
                            alignLeftOld = getWidth() / 2 - kropedBitmap.getWidth() / 2;
                            alignTopOld = getHeight()  / 2 - kropedBitmap.getHeight() / 2;
                            freshBitmap = ImageEditor.getEffectByNum(HistoryHolder.getInstance().lastEffect
                                    , kropedBitmap);
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
                if (alignLeft * loupeX -(loupeX - 1) * getWidth() / 2 > getWidth()) {
                    alignLeft = getWidth() / loupeX + (loupeX - 1) / loupeX * getWidth() / 2 - 5 * loupeX;
                }
                if (alignTop * loupeY - (loupeX - 1) * getHeight() / 2 > getHeight()) {
                    alignTop = getHeight() / loupeY + (loupeX - 1) / loupeX * getHeight() / 2 - 5 * loupeY;
                }
                canvas.translate(-(loupeX - 1) / loupeX * getWidth() / 2,  - (loupeX - 1) / loupeX * getHeight() / 2);
                canvas.drawBitmap(scaledBitmap, alignLeft, alignTop, paint);

                paint.setARGB(128, 255, 0, 0);
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);
                if (isKrop) {
                    kropFrame.draw(canvas);

                }
                surface.unlockCanvasAndPost(canvas);
            }
            catch (Exception e) {
                Log.e("mySurfaceView.draw", e.toString());
            }
        }

    }

    public void focusLose() {
        if (focusListener != null) {
            focusListener.focusLosed(true);
        }
    }

}
