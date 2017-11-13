package edu.example.dmitry.videoeditor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
    public ImageVideoSelectAdapter(@NonNull Activity activity, int resource, @NonNull List<String> objects) {
        super(activity, resource, objects.subList(0, objects.size()));
        strings = objects;
        this.activity = activity;
    }

    MyTask myTask = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (true) {
            if (myTask == null) {
                myTask = new MyTask();
            }
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.photo_video_tip_rect
                            , parent
                            , false);
            convertView.setTag(strings.get(strings.size() - 1 - position));
            new MyTask().execute(new ViewAndString(convertView, strings.get(strings.size() - 1 - position), null));


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
    private class MyTask extends AsyncTask<ViewAndString, ViewAndString, ViewAndString> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(ViewAndString... result) {
            super.onProgressUpdate(result);
            ((ImageView)result[0].view.findViewById(R.id.image_pos)).setImageBitmap(result[0].bm);
        }

        @Override
        protected ViewAndString doInBackground(ViewAndString... params) {
            Bitmap bm = null;
            try {
                File imageFile = new File(params[0].string);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                if (imageFile.exists()) {
                    try {
                        bm = BitmapFactory.decodeFile(params[0].string, options);
                    } catch (OutOfMemoryError oushit) {
                        Log.d("err", oushit.toString());
                    }
                } else {
                    throw new IOException();
                }
            } catch(IOException e){
                bm = null;
            }
            params[0].bm = bm;
            return params[0];

        }

        @Override
        protected void onPostExecute(ViewAndString result) {
            super.onPostExecute(result);
            ((ImageView)result.view.findViewById(R.id.image_pos)).setImageBitmap(result.bm);
        }
/*
        ViewAndString viewAndString = null;
        public void go() {
            ExecutorService service = Executors.newFixedThreadPool(2);
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        File imageFile = new File(params[0].string);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        if (imageFile.exists()) {
                            try {
                                bm = BitmapFactory.decodeFile(params[0].string, options);
                            } catch (OutOfMemoryError oushit) {
                                Log.d("err", oushit.toString());
                            }
                        } else {
                            throw new IOException();
                        }
                    } catch(IOException e){
                        bm = null;
                    }
                    params[0].bm = bm;
                }
            })
        }*/
    }

}
