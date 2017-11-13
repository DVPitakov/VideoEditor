package edu.example.dmitry.videoeditor.Items;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import edu.example.dmitry.videoeditor.Tools;

/**
 * Created by dmitry on 13.09.17.
 */

public class ImageItem extends BaseItem {
    private int image = edu.example.dmitry.videoeditor.R.drawable.shapka1;
    private int imageSizex= 60;
    private int imageSizeDx = 60;
    private int imageSizey= 60;
    private int imageSizeDy = 60;
    private Bitmap bitmapSource;
    private Bitmap originalBitmapSource;

    public ImageItem(int imageType, View view, int left, int top) {
        super(view);

        image = imageType;
        rect = new Rect();
        rect.set(left, top, left + imageSizeDx, top +  imageSizeDy);
        bitmapSource = BitmapFactory.decodeResource(view.getResources(), image);
        originalBitmapSource = bitmapSource;
        int width = bitmapSource.getWidth();
        int height = bitmapSource.getHeight();
        Resources res = view.getResources();
        int maxSize = (int)res.getDimension(edu.example.dmitry.videoeditor.R.dimen.maxIconSize);
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
        imageSizex = bitmapSource.getWidth();
        imageSizeDx = imageSizex;
        imageSizey = bitmapSource.getHeight();
        imageSizeDy = imageSizey;

        rect.right = rect.left + bitmapSource.getWidth();
        rect.bottom = rect.top + bitmapSource.getHeight();

    }

    public void setImageSize(float loupe) {
        if (savedScale * loupe > 10) {
            timeScale = 10f;
        }
        else if(savedScale * loupe < 0.1) {
            timeScale = 0.1f;
        }
        else {
            timeScale = savedScale * loupe;
        }
        int imageSizeDxOld = bitmapSource.getWidth();
        int imageSizeDyOld = bitmapSource.getHeight();
        imageSizeDx = (int)(imageSizex * timeScale);
        imageSizeDy = (int)(imageSizey * timeScale);
        bitmapSource = Bitmap.createScaledBitmap(originalBitmapSource, imageSizeDx, imageSizeDy, false);

        rect.left += (imageSizeDxOld - bitmapSource.getWidth()) / 2;
        rect.top += (imageSizeDyOld - bitmapSource.getHeight()) / 2;
        rect.right = rect.left + bitmapSource.getWidth();
        rect.bottom = rect.top + bitmapSource.getHeight();

    }

    @Override
    public void scale(float scale) {
        setImageSize(scale);
    }

    public void saveImageSize() {
        imageSizex = imageSizeDx;
        imageSizey = imageSizeDy;

    }

    public void setImage(int imageId) {
        this.image = imageId;
        int left = rect.left;
        int bottom = rect.bottom;

    }

    public void draw(Canvas canvas) {
        float senterX = Tools.getCenter(rect.right + x, rect.left + x);
        float senterY = Tools.getCenter(rect.top + y , rect.bottom + y);
        matrix.reset();
        matrix.setTranslate(rect.left + x, rect.top + y);
        matrix.postRotate(alpha + oldAlpha, senterX, senterY);

        inverseMatrix.reset();
        inverseMatrix.setRotate(-alpha - oldAlpha, senterX, senterY);
        inverseMatrix.postTranslate(-rect.left - x, - rect.top  - y);
        canvas.setMatrix(matrix);

        Paint fontPaint = new Paint();
        canvas.drawBitmap(bitmapSource, 0, 0, fontPaint);

        fontPaint.setStyle(Paint.Style.STROKE);
        fontPaint.setStrokeWidth(20);

        if (focused) {
            drawFrame(canvas);
        }
    }


}
