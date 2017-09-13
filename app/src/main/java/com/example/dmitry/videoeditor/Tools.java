package com.example.dmitry.videoeditor;

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
}
