package com.example.dmitry.videoeditor.UFO;

import com.example.dmitry.videoeditor.Vidgets.ImageElement;

/**
 * Created by dmitry on 31.10.17.
 */

public class ImageChangesHolder {

    public interface MoveAble {
        void move(float dx, float dy);
        void saveMovePoin();
    }

    //
    public interface DrawBeforeScaleAble {}

    //
    public interface DrawAfterScaleAble {}

    //
    public interface RotatAble {}

    //
    public interface ScaleAble {}

    //
    public interface CanscellAble {}

    //
    public interface HeaderAble {}

    //
    public interface FooterAble {}

    //
    public interface ClickAble {
        void onClick(float x, float y);
    }

    private static ImageChangesHolder instance;
    private ImageChangesHolder() {}

    public static ImageChangesHolder beginTransaction(Item targetElement) {
        if (instance == null) {
            instance = new ImageChangesHolder();
        }
        return instance;
    }

    public ImageChangesHolder addChange(Change change) {
        return instance;

    }

    public ImageChangesHolder saveInHistory() {
        return instance;

    }

    public ImageChangesHolder back() {
        return instance;

    }

    //draw changes
    public ImageChangesHolder commit() {
        return instance;

    }



}
