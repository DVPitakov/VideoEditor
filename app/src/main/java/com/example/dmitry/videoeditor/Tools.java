package com.example.dmitry.videoeditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import layout.PanelInstrumentImage;

import static java.lang.Math.asin;

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

    private static Pattern p = Pattern.compile("(mp4)|(3gp)|(avi)$");

    public static boolean isNotVideo(String string) {
        return !isVideo(string);
    }

    public static boolean isVideo(String string) {
        Matcher m = p.matcher(string);
        return m.find();

    }

    public static float getCenter(float x00, float x01) {
        return (x01 + x00)/2;

    }

    public static float getLoupe(float x00, float y00, float x01, float y01,
                                 float x10, float y10, float x11, float y11) {
        float dx0 = x01 - x00;
        float dy0 = y01 - y00;

        float dx1 = x11 - x10;
        float dy1 = y11 - y10;

        float l0 = (float)Math.sqrt(dx0 * dx0 + dy0 * dy0);
        float l1 = (float)Math.sqrt(dx1 * dx1 + dy1 * dy1);

        if (l0 < 0.01) {
            l0 = 0.01f;
        }

        return l1 / l0;

    }

    public static float getAlpha(float x00, float y00, float x01, float y01,
                                 float x10, float y10, float x11, float y11) {
        float dx0 = x01 - x00;
        float dy0 = y01 - y00;

        float dx1 = x11 - x10;
        float dy1 = y11 - y10;
        float val =(float)(Math.atan(dy1 / dx1) - Math.atan(dy0 / dx0));
        if(val == 0) {
            return 0;
        }
        return (float)(180 * val / Math.PI);

    }

    public static void saveAndSendImage(Bitmap bitmap, Context context) {
        String str = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "image.jpg" , null);
        Uri imageUri = Uri.parse(str);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "send"));

    }

    public static void sendVideo(Context context) {
        Uri videoUri = Uri.parse(UrlHolder.getOutputUrl());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.setType("video/mp4");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "send"));

    }

    public static void hh() {

    }

}
