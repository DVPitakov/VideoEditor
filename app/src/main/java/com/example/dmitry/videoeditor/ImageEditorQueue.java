package com.example.dmitry.videoeditor;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by dmitry on 11.09.17.
 */

public class ImageEditorQueue {
    ArrayList<ImageElement> images = new ArrayList<>();

    public ImageEditorQueue() {

    }

    public void addElement(ImageElement imageElement) {
        images.add(imageElement);

    }

    public Bitmap draw(Bitmap bitmap) {
        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;
        }
        Bitmap freshBitmap = bitmap.copy(config, true);
        Canvas canvas = new Canvas(freshBitmap);
        for(ImageElement imageElement: images) {
            imageElement.draw(canvas);
        }
        return freshBitmap;

    }

    public ImageElement find(int x, int y) {
        for(ImageElement imageElement: images) {
            if (imageElement.contains(x, y)) {
                return imageElement;

            }
        }
        return null;

    }
}
