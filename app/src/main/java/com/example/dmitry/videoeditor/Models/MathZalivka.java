package com.example.dmitry.videoeditor.Models;

import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by dmitry on 24.10.17.
 */


public class MathZalivka {
    private static class YPointComparator implements Comparator<Point> {

        @Override
        public int compare(Point point, Point t1) {
            return point.y - t1.y;
        }
    }
    public static void sortArrayByY(ArrayList<Point> point) {
        point.sort(new YPointComparator());
    }
}
