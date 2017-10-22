package com.example.dmitry.videoeditor.Holders;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.example.dmitry.videoeditor.MySurfaceView;

import java.util.ArrayList;

/**
 * Created by dmitry on 21.10.17.
 */

public class SurfaceViewHolder {
    private static  SurfaceViewHolder instance;
    private SurfaceViewHolder() {}
    private MySurfaceView mySurfaceView;
    public static SurfaceViewHolder getInstance() {
        if(instance == null) {
            instance = new SurfaceViewHolder();
        }
        return instance;
    }

    public MySurfaceView getMySurfaceView() {
        return mySurfaceView;
    }

    public void setMySurfaceView(MySurfaceView mySurfaceView) {
        this.mySurfaceView = mySurfaceView;
    }

}


