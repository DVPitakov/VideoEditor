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

    public static Bitmap krop(Bitmap bitmap, int x1, int y1, int x2, int y2) {
        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        if(x1 > x2) {
            int x = x2;
            x2 = x1;
            x1 = x;
        }
        if (y1 > y2) {
            int y = y2;
            y2 = y1;
            y1 = y;
        }
        Bitmap freshBitmap = bitmap.createBitmap(bitmap, x1, y1, x2 - x1, y2 - y1);
        return freshBitmap;

    }

    public static Bitmap inversion(Bitmap bitmap) {
        return null;

    }
}
