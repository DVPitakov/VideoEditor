package edu.example.dmitry.videoeditor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;

import edu.example.dmitry.videoeditor.holders.UrlHolder;

/**
 * Created by alexandr on 22.10.17.
 */

public class SettingsVideo {
    static private Uri _input_uri;
    static private Uri _output_Uri;
    static private String _input_path;
    static private String _output_path;
    static private long start;
    static private long end;
    static private float compression_ratio;
    static private Context context;

    public static void setInput(Uri uri, Activity a){
        SettingsVideo._input_uri = uri;
        try {
            SettingsVideo._input_path = UrlHolder.getFilePath(a,_input_uri);
        }
        catch (Exception e) {

            SettingsVideo._input_path = uri.toString();
            Log.d(":-(", ":-(");
        }

    }

    public static void generateOutput() {
        SettingsVideo._output_path = SettingsVideo.getInput("") + "tmp.mp4";
    }



    public static void setInput(String path) {
        SettingsVideo._input_path = path;
        SettingsVideo._input_uri = UrlHolder.getUri(path);
    }

    public static void setOutput(Uri uri) {
        SettingsVideo._output_Uri = uri;
        SettingsVideo._output_path = UrlHolder.getFilePath(context,uri);
    }

    public static void setOutput(String path) {
        SettingsVideo._output_path = path;
        SettingsVideo._output_Uri = UrlHolder.getUri(path);
    }

    public static void setCompressionRatio(float compression_ratio) {
        SettingsVideo.compression_ratio = compression_ratio;
    }

    public static void setContext(Context context) {
        SettingsVideo.context = context;
    }

    public static Uri getInput() {
        return _input_uri;
    }

    public static String getInput(String type) {
        return _input_path;
    }

    public static Uri getOutput() {
        return _output_Uri;
    }

    public static String getOutput(String type) {
        return _output_path;
    }

    public static long getStart() {
        return start;
    }

    public static void setStart(long start) {
        SettingsVideo.start = start;
    }

    public static long getEnd() {
        return end;
    }

    public static void setEnd(long end) {
        SettingsVideo.end = end;
    }

    public static float getCompressionRatio() {
        return compression_ratio;
    }

    public static Context getContext() {
        return context;
    }
}
