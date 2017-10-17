package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.example.dmitry.videoeditor.Views.ImageFragment;
import com.example.dmitry.videoeditor.Views.VideoFragment;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

public class EditorActivity extends Activity {
    public final static String INPUT_URI = "inputUri";
    public final static String OUTPUT_URI = "outputUri";


    EditText editText;

    ImageHolder imageHolder;
    ImageFragment imageFragment;
    VideoFragment videoFragment;

    FFmpeg ffmpeg;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);
        if(Tools.isVideo(UrlHolder.getInpurUrl())) {
            videoFragment = new VideoFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_pos, videoFragment)
                    .commit();
        }
        else {
            imageFragment = new ImageFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_pos, imageFragment)
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (imageFragment != null) {
            fragmentTransaction.remove(imageFragment);
        }
        if (videoFragment != null) {
            fragmentTransaction.remove(videoFragment);
        }
        fragmentTransaction.commit();
        super.onSaveInstanceState(savedInstanceState);
    }

    final int MY_PERMISION = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Bitmap bitmap =  ImageHolder.getInstance().getBitmapWithElements();
                Tools.saveAndSendImage(bitmap, this);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }







}

