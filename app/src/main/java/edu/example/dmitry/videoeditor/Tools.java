package edu.example.dmitry.videoeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Tools {
    private Tools() {};

    public static void setForceVideo(boolean forceVideo) {
        Tools.forceVideo = forceVideo;
    }

    static boolean forceVideo = false;


    public static int min(int v1, int v2) {
        if (v1 > v2) return v2;
        return v1;
    }

    public static int max(int v1, int v2) {
        if (v1 < v2) return v2;
        return v1;
    }

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
        if (forceVideo) return true;
        if (string == null) return false;
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

        if(Math.abs(dx0) < 0.000001) {
            if (dx0 > 0) {
                dx0 = 0.000001f;
            }
            else {
                dx0 = -0.000001f;
            }
        }
        if(Math.abs(dx1) < 0.000001) {
            if (dx1 > 0) {
                dx1 = 0.000001f;
            }
            else {
                dx1 = -0.000001f;
            }
        }

        float val = 0;
        if (dy1 < 0 && dx1 < 0) {
            val += Math.PI;
        }
        if (dy1 > 0 && dx1 < 0) {
            val += Math.PI;
        }
        val += (float)(Math.atan(dy1 / dx1) - Math.atan(dy0 / dx0));
        Log.d("1240", "Math.atan(dy1 / dx1)=" + String.valueOf(Math.atan(dy1 / dx1)));
        Log.d("1240", "Math.atan(dy0 / dx0)=" + String.valueOf(Math.atan(dy0 / dx0)));
        if(val == 0) {
            return 0;
        }
        return (float)(180 * val / Math.PI);

    }



    public static float getAlpha(float x0, float y0, float xc, float yc,
                                 float x1, float y1) {
        float dx0 = x0 - xc;
        float dy0 = y0 - yc;

        float dx1 = x1 - xc;
        float dy1 = y1 - yc;

        if(Math.abs(dx0) < 0.000001) {
            if (dx0 > 0) {
                dx0 = 0.000001f;
            }
            else {
                dx0 = -0.000001f;
            }
        }
        if(Math.abs(dx1) < 0.000001) {
            if (dx1 > 0) {
                dx1 = 0.000001f;
            }
            else {
                dx1 = -0.000001f;
            }
        }

        float val = 0;
        if (dy1 < 0 && dx1 < 0) {
            val += Math.PI;
        }
        if (dy1 > 0 && dx1 < 0) {
            val += Math.PI;
        }
        val += (float)(Math.atan(dy1 / dx1) - Math.atan(dy0 / dx0));
        Log.d("1240", "Math.atan(dy1 / dx1)=" + String.valueOf(Math.atan(dy1 / dx1)));
        Log.d("1240", "Math.atan(dy0 / dx0)=" + String.valueOf(Math.atan(dy0 / dx0)));
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
        Uri videoUri = SettingsVideo.getOutput();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.setType("video/mp4");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "send"));

    }

    public static ArrayList<String> getAllImagesAndVideos(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    public static ArrayList<String> getAllImages(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }


}
