package edu.example.dmitry.videoeditor.models;

import android.graphics.Point;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dmitry on 24.10.17.
 */


public class MathZalivka {
    public static void sortArrayByY(ArrayList<Point> point) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(point, new Comparator<Point>(){
                @Override
                public int compare(Point point, Point t1) {
                    return point.x - t1.x;
                }
            });
        }
    }
}
