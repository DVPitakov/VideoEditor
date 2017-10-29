package com.example.dmitry.videoeditor.Holders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import com.example.dmitry.videoeditor.R;

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
    private Bitmap superSmall;
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

    public Bitmap getSmallImage(Activity activity) {

        return Bitmap
                .createScaledBitmap(kropedBitmap,
                        (int)activity.getResources().getDimension(R.dimen.small_img_width)
                        , (int)(activity.getResources().getDimension(R.dimen.small_img_width)
                                / kropedBitmap.getWidth() * kropedBitmap.getHeight())
                        , false);
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
