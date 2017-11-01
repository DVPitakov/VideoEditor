package com.example.dmitry.videoeditor.Items;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.dmitry.videoeditor.Holders.HistoryHolder;

import java.util.ArrayList;

/**
 * Created by dmitry on 11.09.17.
 */

public class ImageEditorQueue {
    ArrayList<BaseItem> images = new ArrayList<>();
    private static ImageEditorQueue instance;
    private ImageEditorQueue() {}

    public static ImageEditorQueue getInstance() {
        if(instance == null) {
            instance = new ImageEditorQueue();
        }
        return instance;
    }



    public void addElement(BaseItem baseItem) {
        images.add(baseItem);

    }

    public Bitmap draw(Bitmap bitmap) {
        android.graphics.Bitmap.Config config = bitmap.getConfig();
        if(config == null) {
            config  = android.graphics.Bitmap.Config.ARGB_8888;
        }
        Bitmap freshBitmap = bitmap.copy(config, true);
        Canvas canvas = new Canvas(freshBitmap);
        for(BaseItem baseItem : images) {
            baseItem.draw(canvas);
        }
        return freshBitmap;

    }


    public BaseItem find(int x, int y) {
        int action;
        for(BaseItem baseItem : images) {
            action = baseItem.contains(x, y);
            if (action > 0) {
                if(action == 3) {
                    HistoryHolder.getInstance().addAction(new HistoryHolder.DeleteItem(baseItem));
                    deleteElement(baseItem);
                    return null;
                }
                return baseItem;

            }
        }
        return null;

    }

    public void moveAll(int dx, int dy) {
        for(BaseItem baseItem : images) {
            baseItem.move(dx, dy);
            baseItem.moveEnd();
        }
    }

    public void deleteElement(BaseItem element) {
        for(int i = 0; i < images.size(); i++) {
            if (images.get(i) == element) {
                images.remove(i);
                break;
            }
        }
    }
    public void clear() {
        images = new ArrayList<>();

    }
}
