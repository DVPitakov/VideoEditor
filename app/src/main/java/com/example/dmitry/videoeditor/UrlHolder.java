package com.example.dmitry.videoeditor;

/**
 * Created by dmitry on 15.10.17.
 */

public class UrlHolder {
    private String inputUrl;
    private String outputUrl;
    private static UrlHolder instance;
    private UrlHolder() {
        this.instance = new UrlHolder();

    }

    public static UrlHolder getInstance() {
        if(instance == null) {
            instance = new UrlHolder();
        }
        return instance;
    }

    public void setInputUrl(String inputUrl) {
        this.inputUrl = inputUrl;

    }

    public void setOutputUrl(String outputUrl) {
        this.outputUrl = outputUrl;

    }

    public String getInpurUrl() {
        return inputUrl;

    }

    public String outputUrl() {
        return outputUrl;

    }




}
