package edu.example.dmitry.videoeditor.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dmitry on 10.11.17.
 */

public class SettingsData {

    private static SettingsData cache = null;
    private boolean history;
    private boolean showFrame;

    public static SettingsData getInstance(Context context) {
        if(cache == null) {
            cache = new SettingsData(context);
        }
        return cache;
    }
    public SharedPreferences getPreferences() {
        return preferences;
    }

    private SharedPreferences preferences;

    public SettingsData(Context context) {
        if (cache == null) {
            preferences = context.getSharedPreferences("SettingsData", 0);
            history = preferences.getBoolean("history", true);
            showFrame = preferences.getBoolean("showFrame", true);
        }
        else {
            preferences = cache.getPreferences();
            history = cache.isHistory();
            showFrame = cache.isShowFrame();
        }
    }


    public void saveSettingsData() {
        cache = this;
        preferences.edit()
                .putBoolean("history", history)
                .putBoolean("showFrame", showFrame)
                .apply();
    }

    public boolean isShowFrame() {
        return showFrame;
    }

    public void setShowFrame(boolean showFrame) {
        this.showFrame = showFrame;
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }
}
