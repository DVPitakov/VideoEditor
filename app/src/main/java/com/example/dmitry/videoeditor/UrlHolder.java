package com.example.dmitry.videoeditor;

/**
 * Created by dmitry on 15.10.17.
 */

public class UrlHolder {
    private static String inputUrl;
    private static String outputUrl;
    private static UrlHolder instance;

    public static UrlHolder getInstance() {
        if(instance == null) {
            instance = new UrlHolder();
        }
        return instance;
    }

    public static void setInputUrl(String _inputUrl) {
        inputUrl = _inputUrl;

    }

    public static void setOutputUrl(String _outputUrl) {
        outputUrl = _outputUrl;

    }

    public static String getInpurUrl() {
        return inputUrl;

    }

    public static String getOutputUrl() {
        return outputUrl;

    }




}
