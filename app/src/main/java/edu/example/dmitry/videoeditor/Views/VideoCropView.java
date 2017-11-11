package edu.example.dmitry.videoeditor.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by dmitry on 10.11.17.
 */

public class VideoCropView extends FrameLayout {
    float leftCur;
    float rightCur;
    float leftBorder;
    float rightBorder;
    ImageView imgPolzunoc;
    ImageView imgleftCur;
    ImageView imgRightCur;
    ImageView imgLeftShadow;
    ImageView imgRightShadow;
    GridView images;

    public interface VideoCropViewListener {
        void onPolzunocMove(float newPos);
        void onLeftCurMove(float newPos);
        void onRightCurMove(float newPos);
    }

    public VideoCropView(@NonNull Context context) {
        super(context);
        imgleftCur = new ImageView(context);
        imgleftCur.setImageResource(edu.example.dmitry.videoeditor.R.drawable.video_krop_left_24dp);
        imgRightCur = new ImageView(context);
        imgleftCur.setImageResource(edu.example.dmitry.videoeditor.R.drawable.video_crop_right);
    }
}
