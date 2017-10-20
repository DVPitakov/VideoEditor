package com.example.dmitry.videoeditor;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;

/**
 * Created by dmitry on 20.10.17.
 */

public class CurrentVideoHolder {
    public interface UpdatedVideoLenChangedListener {
        void updatedVideoLenChanged(long updatedVideoLen);

    }
    public interface VideoShower {
        void showNewVideo();
    }
    private UpdatedVideoLenChangedListener listener;
    private CurrentVideoHolder() {}
    private static CurrentVideoHolder instance;
    private long videoLen;
    private long updatedVideoLen;
    private VideoShower mediaPlayer;

    public void setListener(UpdatedVideoLenChangedListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public static CurrentVideoHolder getInstance() {
        if(instance == null) {
            instance = new CurrentVideoHolder();
        }
        return  instance;
    }

    public void setVideoLen(long videoLen) {
        this.videoLen = videoLen;
    }

    public long getVideoLen() {
        return  videoLen;
    }

    public void setUpdatedVideoLen(long updatedVideoLen) {
        this.updatedVideoLen = updatedVideoLen;
        Log.d("1034pit", "video len: " + String.valueOf(videoLen));
        Log.d("1034pit", "updated video len: " + String.valueOf(updatedVideoLen));
        if(listener != null) {
            listener.updatedVideoLenChanged(updatedVideoLen);
        }
    }

    public void setCurrentMediaPlayer(VideoShower mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void removeCurrentVideoPlayer() {
        this.mediaPlayer = null;
    }

    public void showNewVideo() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.showNewVideo();
        }
    }


    public long getUpdatedVideoLen() {
        return  updatedVideoLen;
    }
}
