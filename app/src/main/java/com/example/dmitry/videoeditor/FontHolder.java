package com.example.dmitry.videoeditor;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by dmitry on 15.10.17.
 */

public class FontHolder {
    private static  FontHolder instance;
    private Typeface type;
    private FontHolder(Context context) {
        type = Typeface.createFromAsset(context.getAssets(),"fonts/nexus_01.ttf");
    }
    public static FontHolder getInstance(Context context) {
        if(instance == null) {
            instance = new FontHolder(context);
        }
        return instance;
    }

    public static FontHolder getInstance() {
        return instance;
    }

    public Typeface getType() {
        return type;
    }
}
