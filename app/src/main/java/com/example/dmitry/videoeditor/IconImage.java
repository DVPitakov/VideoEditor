package com.example.dmitry.videoeditor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

/**
 * Created by dmitry on 13.09.17.
 */

public class IconImage extends ImageElement {
    private int image = R.drawable.shapka;
    private int imageSize = 60;
    private int imageSizeD = 60;
    private Bitmap bitmapSource;

    View view;

    public IconImage(int imageType, View view, int left, int top) {
        super();
        this.view = view;
        rect = new Rect();
        rect.set(left, top, left + imageSizeD, top +  imageSizeD);
        bitmapSource = BitmapFactory.decodeResource(view.getResources(), image);
        int width = bitmapSource.getWidth();
        int height = bitmapSource.getHeight();
        Resources res = view.getResources();
        int maxSize = (int)res.getDimension(R.dimen.maxIconSize);
        float w = width /maxSize;
        float h = height / maxSize;
        if(w > 1 || h > 1) {
            if (w > h) {
                h = w;
            }
            else {
                w = h;
            }
            bitmapSource = Bitmap.createScaledBitmap(bitmapSource, (int)(width / w), (int)(height / h), false);
        }
        imageSize = bitmapSource.getWidth();
        imageSizeD = imageSize;

    }

    public void setPos(int left, int top) {
        rect.set(left, top, left + imageSizeD, top +  imageSizeD);

    }

    public void setImageSize(float loupe) {
        imageSizeD = (int)(imageSize * loupe);
        bitmapSource = Bitmap.createScaledBitmap(bitmapSource, imageSizeD, imageSizeD, false);
        rect.right = rect.left + imageSize;
        rect.bottom = rect.top + imageSize;

    }

    public void saveImageSize() {
        imageSize = imageSizeD;

    }

    public void setImage(int imageId) {
        this.image = imageId;
        int left = rect.left;
        int bottom = rect.bottom;

    }

    public void draw(Canvas canvas) {
        Paint fontPaint = new Paint();
        canvas.drawBitmap(bitmapSource, rect.left + x, rect.top + y, fontPaint);

    }
}
