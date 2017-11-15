package edu.example.dmitry.videoeditor.holders;

import edu.example.dmitry.videoeditor.views.MySurfaceView;

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


