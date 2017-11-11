package edu.example.dmitry.videoeditor.Models;

/**
 * Created by dmitry on 17.10.17.
 */

public class IconWithText {
    private int iconResource;
    private String text;
    public IconWithText(int incoResource, String text) {
        this.iconResource = incoResource;
        this.text = text;
    }

    public int getIconResource() {
        return iconResource;
    }

    public String getText() {
        return text;
    }
}
