package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.IOException;

/**
 * Created by dmitry on 10.09.17.
 */

public class ImageHolder {
    private Bitmap defaultBitmap;
    private Bitmap freshBitmap;
    private Bitmap bitmapWithElements;
    private Bitmap scaledBitmap;

    private float loupe = 1;


    public ImageHolder(Uri defaultImagePath, Activity activity) {
        try {
            defaultBitmap = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), defaultImagePath
            );
            freshBitmap = defaultBitmap;
        }
        catch (IOException e) {
            Log.d("er", "101128");
        }

    }

    public Bitmap getDefaultBitmap() {
        return defaultBitmap;

    }

    public Bitmap getFreshBitmap() {
        return  freshBitmap;

    }

    public void setFreshBitmap(Bitmap freshBitmap) {
        this.freshBitmap = freshBitmap;
        bitmapWithElements = null;
        scaledBitmap = null;

    }

    public void setScaledBitmap(Bitmap bitmap) {
        scaledBitmap = bitmap;

    }

    public void setBitmapWithElements(Bitmap bitmap) {
        bitmapWithElements = bitmap;
        scaledBitmap = null;

    }

    public Bitmap getBitmapWithElements() {
        return bitmapWithElements;

    }

    public  Bitmap getScaledBitmap() {
        return scaledBitmap;

    }
}
