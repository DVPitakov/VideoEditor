package com.example.dmitry.videoeditor.Holders;

import com.example.dmitry.videoeditor.Vidgets.ImageElement;

/**
 * Created by dmitry on 21.10.17.
 */

public class CurrentElementHolder {
    private static CurrentElementHolder instance;
    private CurrentElementHolder() {}
    private ImageElement imageElement;
    public static CurrentElementHolder getInstance() {
        if(instance == null) {
            instance = new CurrentElementHolder();
        }
        return instance;
    }

    public void setCurrentElement(ImageElement imageElement) {
        this.imageElement = imageElement;

    }

    public void removeCurrentElement() {
        this.imageElement = null;

    }

    public ImageElement getCurrentElement() {
        return imageElement;

    }

}
