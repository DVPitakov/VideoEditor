package edu.example.dmitry.videoeditor.items;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import edu.example.dmitry.videoeditor.holders.HistoryHolder;

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
        for(int i = images.size() - 1; i >= 0; i--) {
            BaseItem baseItem = images.get(i);
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
        Log.d("gooding", "dx: " + dx);
        Log.d("gooding", "dy: " + dy);
        for(BaseItem baseItem : images) {
            baseItem.move(dx, dy, 1);
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
