package com.example.dmitry.videoeditor.Holders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by dmitry on 10.09.17.
 */

public class ImageHolder {
    private  Bitmap defaultBitmap;
    private  Bitmap kropedBitmap;
    private  Bitmap freshBitmap;
    private  Bitmap bitmapWithElements;
    private  Bitmap scaledBitmap;
    private static ImageHolder instance;

    public static ImageHolder getInstance() {
        if (instance == null) {
         instance = new ImageHolder();
        }
        return instance;
    }
    private ImageHolder() {}

    public void tryInit(Activity activity) {
        if (defaultBitmap == null) {
            try {
                defaultBitmap = MediaStore.Images.Media.getBitmap(
                        activity.getContentResolver(), UrlHolder._getInputUri()
                );
                Log.d("1996", "defaultBitmap: " + String.valueOf(defaultBitmap));
                freshBitmap = defaultBitmap;
            } catch (IOException e) {
                Log.d("2245", "101128");
            }
        }
    }

    public void setMaxImageSize(int maxWidth, int maxHeight) {
        if (maxWidth != 0 && maxHeight != 0 && defaultBitmap != null) {


            float x = 1.0f *  maxWidth / defaultBitmap.getWidth() ;
            float y = 1.0f * maxHeight / defaultBitmap.getHeight();
            float actor;
            if (x < y) {
                actor = x;
            }
            else {
                actor = y;
            }


            int iw = defaultBitmap.getWidth();
            int ih = defaultBitmap.getHeight();
            Log.d("1110", "bm width: " + defaultBitmap.getWidth());
            Log.d("1110", "bm height" + defaultBitmap.getHeight());

            Log.d("1110", "maxWidth / defaultBitmap.getWidth(): " +  1.0f *  maxWidth / defaultBitmap.getWidth());
            Log.d("1110", "maxHeight / defaultBitmap.getHeight():" + 1.0 * maxHeight / defaultBitmap.getHeight());


            Log.d("1110", "iw * actor: " +  iw * actor);
            Log.d("1110", "ih * actor:" + ih * actor);

            Log.d("1110", "maxWidth: " +  maxWidth);
            Log.d("1110", "maxHeight:" + maxHeight);


            if (iw > 0 && ih > 0)
                defaultBitmap = Bitmap.createScaledBitmap(defaultBitmap, (int) (iw * actor), (int) (ih * actor), true);
            setFreshBitmap(null);
        }
    }


    public Bitmap getDefaultBitmap() {
        return defaultBitmap;

    }

    public Bitmap getFreshBitmap() {
        return freshBitmap;

    }

    public void setKropedBitmap(Bitmap bitmap) {
        this.kropedBitmap = bitmap;
        setFreshBitmap(null);
    }

    public Bitmap getKropedBitmap() {
        return kropedBitmap;

    }

    public void setFreshBitmap(Bitmap freshBitmap) {
        this.freshBitmap = freshBitmap;
        bitmapWithElements = null;
        scaledBitmap = null;
        System.gc();

    }

    public void setScaledBitmap(Bitmap bitmap) {
        scaledBitmap = bitmap;

    }

    public void setBitmapWithElements(Bitmap bitmap) {
        bitmapWithElements = bitmap;
        scaledBitmap = null;
        System.gc();

    }

    public Bitmap getBitmapWithElements() {
        return bitmapWithElements;

    }

    public  Bitmap getScaledBitmap() {
        return scaledBitmap;

    }
}
