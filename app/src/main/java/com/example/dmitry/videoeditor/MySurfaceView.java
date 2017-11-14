package com.example.dmitry.videoeditor;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.example.dmitry.videoeditor.Vidgets.RisunocImage;
import com.example.dmitry.videoeditor.Vidgets.TextImage;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ExoPlaybackException;

import java.io.IOException;

/**
 * Created by dmitry on 08.09.17.
 */

public class MySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, View.OnTouchListener,
        CurrentVideoHolder.VideoShower {

    private long oldClickTime;
    private long newClickTime;

    @Override
    public void showNewVideo() {
        mediaPlayer.stop();
        //mediaPlayer.reset();
        //try {
            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(UrlHolder.getInpurUrl() + "tmp.mp4"),
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            mediaPlayer.prepare(videoSource);
            mediaPlayer.setVideoSurface(surfaceHolder.getSurface());
            mediaPlayerStart();

/*
            Log.d("1130", UrlHolder.getInpurUrl() + "tmp.mp4");
            mediaPlayer.setDataSource(context, Uri.parse(UrlHolder.getInpurUrl() + "tmp.mp4"));
            mediaPlayer.setSurface(surfaceHolder.getSurface());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();//*/
            Log.d("1130", "survive");
        /*
        } catch (IOException e) {
            e.printStackTrace();
        }//*/
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

    SimpleExoPlayer mediaPlayer;
    boolean durationSet = false;
    Context context;
    SurfaceHolder surfaceHolder;
    ContentResolver cR;
    public ImageEditorQueue imageEditorQueue;
    public ImageElement selectedImageElement;
    int effect = 0;

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

    FocusListener focusListener;


    boolean isKrop = false;

    public void setEffect(int i) {
        effect = i;

    }

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.cR = context.getContentResolver();
        this.context = context;
        this.setOnTouchListener(this);
        imageEditorQueue = ImageEditorQueue.getInstance();


    }

    public void kropClear() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;

    }
    public long getMediaPlayerCurrentPosition() {
        if(mediaPlayer != null) {
            //mediaPlayer.getDuration();
            //return mediaPlayer.getContentPosition();
            long position = mediaPlayer.getCurrentPosition();
            return position;
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
            mediaPlayer.setPlayWhenReady(true);
        }

    }

    public void mediaPlayerPause() {
        if(mediaPlayer != null) {
            mediaPlayer.setPlayWhenReady(false);
        }
    }

    public boolean mediaPlayerIsPlaying() {
            return (mediaPlayer != null) && mediaPlayer.getPlayWhenReady();

    }

    public void kropSet() {
        isKrop = true;

    }
    public void kropUnset() {
        isKrop = false;

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
        // Stop ExoPlayer working, but still
        mediaPlayer.stop();
        //try {
            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(UrlHolder.getInpurUrl()),
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            mediaPlayer.prepare(videoSource);
            /*
            mediaPlayer.setDataSource(context, Uri.parse(UrlHolder.getInpurUrl()));
            mediaPlayer.setSurface(surfaceHolder.getSurface());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            //*/
            /*
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    Log.d("db", "next");
                }
            });//*/
        /*
        } catch (IOException e) {
            e.printStackTrace();
        }//*/

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        SurfaceViewHolder.getInstance().setMySurfaceView(this);
        CurrentVideoHolder.getInstance().setCurrentMediaPlayer(this);
        this.surfaceHolder = surfaceHolder;
        //try {
            if (Tools.isVideo(UrlHolder.getInpurUrl())) {
                // 1. Create a default TrackSelector
                //Handler mainHandler = new Handler();
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);

                // 2. Create the player
                /*SimpleExoPlayer*/ mediaPlayer =
                        ExoPlayerFactory.newSimpleInstance(context, trackSelector);


                // Bind the player to the view.
                mediaPlayer.setVideoSurface(surfaceHolder.getSurface());

                // Measures bandwidth during playback. Can be null if not required.
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "VideoEditor"));
                // Produces Extractor instances for parsing the media data.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                // This is the MediaSource representing the media to be played.
                MediaSource videoSource = new ExtractorMediaSource(Uri.parse(UrlHolder.getInpurUrl()),
                        dataSourceFactory, extractorsFactory, null, null);
                // Prepare the player with the source.
                mediaPlayer.prepare(videoSource);
                mediaPlayer.addListener(new Player.EventListener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == Player.STATE_READY && !durationSet) {
                            long realDurationMillis = mediaPlayer.getDuration();
                            CurrentVideoHolder.getInstance().setVideoLen(realDurationMillis);
                            durationSet = true;
                        }

                    }

                    /*@Override
                    public void onPlayWhenReadyCommitted() {
                        // No op.
                    }*/
                    @Override
                    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {
                        // Do nothing
                    }
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest) {
                        // Do nothing
                    }
                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                        // Do nothing
                    }
                    @Override
                    public void onPositionDiscontinuity() {
                        // Do nothing
                    }
                    @Override
                    public void onPlayerError(ExoPlaybackException error) {
                        // No op.
                    }
                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                        // No op.
                    }
                    @Override
                    public void onLoadingChanged(boolean isLoading) {

                    }
                });
                CurrentVideoHolder.getInstance().setVideoLen(mediaPlayer.getDuration());
                /*mediaPlayer = new MediaPlayer();
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
                });*/
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
        /*
        }
        catch (IOException e) {
            Log.d("exc", "IOExcepiton in MySurfaceView.surfaceCreated");
        }//*/

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
                    if (isKrop == false) {
                        if(count > 1) {
                            fingerX2 = motionEvent.getX(1);
                            fingerY2 = motionEvent.getY(1);
                        }
                        if(count == 1) {
                            ImageElement imageElement = selectedImageElement;
                            selectedImageElement = imageEditorQueue.find((int)((x1- alignLeft)/loupeX),
                                    (int)((y1 - alignTop)/loupeY));
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
                        }
                    }
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
                        else if (selectedImageElement instanceof RisunocImage
                                && !((RisunocImage)selectedImageElement).isReady()) {

                        }
                        else{
                            selectedImageElement.setLeft((int)((x2 - x1)/loupeX));
                            selectedImageElement.setTop((int)((y2 - y1)/loupeY));
                            ImageHolder.getInstance().setBitmapWithElements(null);
                        }
                    }
                    if (selectedImageElement != null
                            && selectedImageElement.getClass() == RisunocImage.class
                            && !((RisunocImage)selectedImageElement).isReady()) {
                        ((RisunocImage)selectedImageElement).addDrawingPoint((int)(x2 - alignLeft)
                                , (int)(y2 - alignTop));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        draw();
                    }
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN: {
                    fingerX2 = motionEvent.getX(1);
                    fingerY2 = motionEvent.getY(1);
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
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        if(selectedImageElement instanceof RisunocImage) {
                          ((RisunocImage)selectedImageElement).newLine();
                        }
                    }

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
                        if(selectedImageElement.getClass() == TextImage.class) {
                            ((TextImage)selectedImageElement).setTextSize(
                                    Tools.normalizator(Tools.getLoupe(fingerX1, fingerY1,
                                            fingerX2, fingerY2,
                                            fingernX1, fingernY1,
                                            fingernX2, fingernY2), 6, (float)0.2));
                        }
                        else if (selectedImageElement instanceof IconImage){
                            ((IconImage)selectedImageElement).setImageSize(
                                    Tools.normalizator(Tools.getLoupe(fingerX1, fingerY1,
                                            fingerX2, fingerY2,
                                            fingernX1, fingernY1,
                                            fingernX2, fingernY2), 6, (float)0.2));
                        }
                        selectedImageElement.setAlpha(
                                Tools.getAlpha(fingerX1, fingerY1,
                                        fingerX2, fingerY2,
                                        fingernX1, fingernY1,
                                        fingernX2, fingernY2));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        //}
                    }
                    else {
                        loupeX = Tools.getLoupe(fingerX1, fingerY1,
                                fingerX2, fingerY2,
                                fingernX1, fingernY1,
                                fingernX2, fingernY2) * oldLoupeX;
                        loupeY = loupeX;
                        loupeX = Tools.normalizator(loupeX, 4, (float)0.2);
                        loupeY = Tools.normalizator(loupeY, 4, (float)0.2);
                        ImageHolder.getInstance().setScaledBitmap(null);
                    }

                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    oldLoupeX = loupeX;
                    oldLoupeY = loupeY;
                    if (selectedImageElement != null) {
                        if(selectedImageElement.getClass() == TextImage.class) {
                            ((TextImage)selectedImageElement).saveTextSize();
                        }
                        else if (selectedImageElement instanceof  IconImage) {
                            ((IconImage)selectedImageElement).saveImageSize();
                        }
                        selectedImageElement.saveAlpha();

                    }

                    break;
                }
            }
        }
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
                int iw = bitmapWithElements.getWidth();
                int ih = bitmapWithElements.getHeight();
                scaledBitmap = Bitmap.createScaledBitmap(bitmapWithElements, (int) (iw * loupeX), (int) (ih * loupeY), true);
                ImageHolder.getInstance().setScaledBitmap(scaledBitmap);
            }
            canvas.scale(loupeX, loupeY);
            canvas.drawBitmap(scaledBitmap, alignLeft, alignTop, paint);

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
