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

    public ImageHolder(Uri defaultImagePath, Activity activity) {
        try {
            defaultBitmap = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), defaultImagePath
            );
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

    }
}
