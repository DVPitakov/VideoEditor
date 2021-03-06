package edu.example.dmitry.videoeditor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

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
        if (x1 < 0) x1 = 0;
        if (x2 < 0) x2 = 0;
        if (y1 < 0) y1 = 0;
        if (y2 < 0) y2 = 0;

        if (x1 > bitmap.getWidth()) x1 = bitmap.getWidth();
        if (x2 > bitmap.getWidth()) x2 = bitmap.getWidth();
        if (y1 > bitmap.getHeight()) y1 = bitmap.getHeight();
        if (y2 > bitmap.getHeight()) y2 = bitmap.getHeight();

        Bitmap freshBitmap = bitmap.createBitmap(bitmap, x1, y1, x2 - x1, y2 - y1);
        return freshBitmap;

    }

    public static Bitmap mirror(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        android.graphics.Bitmap.Config config = bitmap.getConfig();
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return rotatedBitmap;

    }


    public static Bitmap rotate(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        android.graphics.Bitmap.Config config = bitmap.getConfig();
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;

    }


    public static Bitmap inversion(Bitmap bitmap) {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        -1,  0,  0,  0, 255,
                        0, -1,  0,  0, 255,
                        0,  0, -1,  0, 255,
                        0,  0,  0,  1,   0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }

    public static Bitmap bombit(Bitmap bitmap) {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        (float)1.2,  (float)0.2,  0,  0, 0,
                        0,   (float)1.2,  (float)0.2,  0, 0,
                        0,  0,  1,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }

    public static Bitmap bombit2(Bitmap bitmap) {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        (float)1.2,  0,  0,  0, 0,
                        0,   (float)1.2,  0,  0, 0,
                        0,  0,  (float)1.2,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }

    public static Bitmap sobol(Bitmap bitmap) {
        ColorMatrix colorMatrix;
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        (float)1.2,  0,  0,  0, 0,
                        0,   (float)1.2,  0,  0, 0,
                        0,  0,  (float)1.2,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }




    public static Bitmap whiteBlack(Bitmap bitmap) {
        ColorMatrix colorMatrix;
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        0.333f,  0.333f,  0.333f,  0, 0,
                        0.333f,  0.333f,  0.333f,  0, 0,
                        0.333f,  0.333f,  0.333f,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }

    public static Bitmap lessRed(Bitmap bitmap) {
        ColorMatrix colorMatrix;
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        0f,  0f,  0f,  0, 0,
                        0f,  1f,  0f,  0, 0,
                        0f,  0f,  1f,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }

    public static  Bitmap getEffectByNum(int effectNum, Bitmap bitmap) {
        Bitmap result = null;
        switch (effectNum) {
            case 0: {
                result = bitmap;
                break;
            }
            case 1: {
                result = ImageEditor.bombit(bitmap);
                break;
            }
            case 2: {
                result = ImageEditor.bombit2(bitmap);
                break;
            }
            case 3: {
                result = ImageEditor.inversion(bitmap);
                break;
            }
            case 4: {
                result = ImageEditor.whiteBlack(bitmap);
                break;
            }
            case 5: {
                result = ImageEditor.lessRed(bitmap);
                break;
            }
            case 6: {
                result = ImageEditor.lessGreen(bitmap);
                break;
            }
            case 7: {
                result = ImageEditor.lessBlue(bitmap);
                break;
            }
        }
        return result;
    }
    public static Bitmap lessGreen(Bitmap bitmap) {
        ColorMatrix colorMatrix;
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        1f,  0f,  0f,  0, 0,
                        0f,  0f,  0f,  0, 0,
                        0f,  0f,  1f,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }

    public static Bitmap lessBlue(Bitmap bitmap) {
        ColorMatrix colorMatrix;
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        1f,  0f,  0f,  0, 0,
                        0f,  1f,  0f,  0, 0,
                        0f,  0f,  0f,  0, 0,
                        0,  0,  0,  1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);


        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;

        }
        Bitmap freshBitmap = bitmap.copy(config, true);

        Canvas canvas = new Canvas(freshBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return freshBitmap;

    }


}
