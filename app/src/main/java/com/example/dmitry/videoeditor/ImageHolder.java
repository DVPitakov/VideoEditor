package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

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
                Log.d("er", "101128");
            }
        }
    }

    public void setMaxImageSize(int maxWidth, int maxHeight) {
        if (maxWidth != 0 && maxHeight != 0) {
            float x =  defaultBitmap.getWidth() / maxWidth;
            float y = defaultBitmap.getHeight() / maxHeight;
            if (x > 1 || y > 1) {
                float actor;
                if (x > y) {
                    actor = x;
                }
                else {
                    actor = y;
                }
                int iw = defaultBitmap.getWidth();
                int ih = defaultBitmap.getHeight();
                defaultBitmap = Bitmap.createScaledBitmap(defaultBitmap, (int) (iw / actor), (int) (ih / actor), true);
                setFreshBitmap(null);

            }
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
