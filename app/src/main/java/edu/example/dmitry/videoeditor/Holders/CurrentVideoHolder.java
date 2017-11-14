package edu.example.dmitry.videoeditor.Holders;

import android.util.Log;

import edu.example.dmitry.videoeditor.DecodeVideo;

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
    private DecodeVideo.Type compressType = DecodeVideo.Type.MEDIUM_QUALITY;

    public DecodeVideo.Type getCompressType() {
        return compressType;
    }

    public void setCompressType(DecodeVideo.Type compressType) {
        this.compressType = compressType;
    }

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

    public void setUpdatedVideoLen(float updatedVideoLen) {
        this.updatedVideoLen = (long) updatedVideoLen * videoLen/100;
        Log.d("1034pit", "video len: " + String.valueOf(videoLen));
        Log.d("1034pit", "updated video len: " + String.valueOf(this.updatedVideoLen));
        if(listener != null) {
            listener.updatedVideoLenChanged(this.updatedVideoLen);
        }
    }

    public void setCurrentMediaPlayer(VideoShower mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
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
