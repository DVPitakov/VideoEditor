package com.example.dmitry.videoeditor;

import layout.PanelInstrumentImage;

/**
 * Created by dmitry on 10.09.17.
 */

public class Tools {
    private Tools() {};

    public static float normalizator(float val, float maxVal, float minVal) {
        if (val > maxVal) {
            return maxVal;

        }
        if (val < minVal) {
            return minVal;

        }
        return val;

    }


    public static boolean isVideo(String string) {
        return string.equals("video/mp4")
                ||  string.equals("video/mp4")
                ||  string.equals("video/3gp")
                ||  string.equals("video/avi");


    }
}
