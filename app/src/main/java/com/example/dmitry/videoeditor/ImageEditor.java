package com.example.dmitry.videoeditor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Locale;

/**
 * Created by dmitry on 09.09.17.
 */

public class ImageEditor {
    private ImageEditor() {}

    public static Bitmap addText(Bitmap bitmap, String text, float x, float y, Paint fontPaint) {
        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);
        Canvas canvas = new Canvas(freshBitmap);
        canvas.drawText(text, x, y, fontPaint);
        return freshBitmap;

    }
}
