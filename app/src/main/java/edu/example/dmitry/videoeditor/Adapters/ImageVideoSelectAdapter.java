package edu.example.dmitry.videoeditor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.example.dmitry.videoeditor.Holders.CurrentVideoHolder;
import edu.example.dmitry.videoeditor.ImageEditor;
import edu.example.dmitry.videoeditor.Models.IconWithText;
import edu.example.dmitry.videoeditor.R;
import edu.example.dmitry.videoeditor.SettingsVideo;

/**
 * Created by dmitry on 10.11.17.
 */

public class ImageVideoSelectAdapter  extends ArrayAdapter<String> {
    List<String> strings;
    Activity activity;

    private ExecutorService service = Executors.newFixedThreadPool(2);
    private static Handler handler = new MyHandler();
    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ViewAndString viewAndString = (ViewAndString)msg.obj;
            ((ImageView)viewAndString.view.findViewById(R.id.image_pos)).setImageBitmap(viewAndString.bm);
        }
    }

    public ImageVideoSelectAdapter(@NonNull Activity activity, int resource, @NonNull List<String> objects) {
        super(activity, resource, objects.subList(0, objects.size()));
        strings = objects;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (true) {

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.photo_video_tip_rect
                            , parent
                            , false);
            convertView.setTag(strings.get(strings.size() - 1 - position));
            service.submit(new MyRunnable(new ViewAndString(convertView, strings.get(strings.size() - 1 - position), null)));


        }

        return convertView;
    }

    private class ViewAndString {
        public ViewAndString(View view, String string, Bitmap bm) {
            this.view = view;
            this.string = string;
            this.bm = bm;
        }
        public View view;
        public String string;
        public Bitmap bm;
    }

    private class MyRunnable implements Runnable {

        public MyRunnable(ViewAndString viewAndString) {
            setViewAndString(viewAndString);
        }

        public void setViewAndString(ViewAndString viewAndString) {
            this.viewAndString = viewAndString;
        }
        private ViewAndString viewAndString = null;
        private Bitmap result = null;
        @Override
        public void run() {
            try {
                File imageFile = new File(viewAndString.string);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                if (imageFile.exists()) {
                    try {
                        result = BitmapFactory.decodeFile(viewAndString.string, options);
                    } catch (OutOfMemoryError oushit) {
                        Log.d("err", oushit.toString());
                    }
                } else {
                    throw new IOException();
                }
            } catch(IOException e){
                result = null;
            }
            viewAndString.bm = result;
            Message msg = handler.obtainMessage(0, viewAndString);
            handler.sendMessage(msg);
        }
    }

}
