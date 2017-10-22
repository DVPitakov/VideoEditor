package com.example.dmitry.videoeditor.Holders;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dmitry on 15.10.17.
 */

public class FontHolder {
    private static  FontHolder instance;
    private ArrayList<Typeface> type = new ArrayList<>();
    private FontHolder(Context context) {
        type.add(Typeface.createFromAsset(context.getAssets(),"fonts/nexus_01.ttf"));
        type.add(Typeface.createFromAsset(context.getAssets(),"fonts/font_2.ttf"));
        type.add(Typeface.createFromAsset(context.getAssets(),"fonts/font_3.ttf"));
    }
    public static FontHolder getInstance(Context context) {
        if(instance == null) {
            instance = new FontHolder(context);
        }
        return instance;
    }

    public static FontHolder getInstance() {
        if(instance == null) {
            Log.d("Error", "in FontHolder01");
        }
        return instance;
    }

    public Typeface getType(int type) {
        return this.type.get(type);
    }
}
