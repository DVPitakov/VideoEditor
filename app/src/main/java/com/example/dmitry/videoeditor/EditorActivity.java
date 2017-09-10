package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class EditorActivity extends Activity {


    public final static String INPUT_URI = "inputUri";
    public final static String OUTPUT_URI = "outputUri";


    float x1;
    float y1;

    float x2;
    float y2;

    float x3;
    float y3;

    Button kropButton;
    Button filterButton;
    Button addTextButton;
    Button pauseButton;
    Button saveButton;

    ImageHolder imageHolder;
    LinearLayout lineraLayout;

    Uri inputUri;
    Uri outputUri;

    MySurfaceView mySurfaceView;
    SeekBar seekBar;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();

        inputUri = intent.getParcelableExtra(INPUT_URI);
        outputUri = intent.getParcelableExtra(OUTPUT_URI);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        imageHolder = new ImageHolder(inputUri, this);

        kropButton = (Button)findViewById(R.id.kropButton);
        kropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rect rect = mySurfaceView.getKropRect();

                Bitmap freshBitmap = ImageEditor.krop(imageHolder.getFreshBitmap(),
                        rect.left, rect.top, rect.right, rect.bottom);
                imageHolder.setFreshBitmap(freshBitmap);
                mySurfaceView.draw();
            }
        });
        filterButton = (Button)findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap freshBitmap = ImageEditor.inversion(imageHolder.getFreshBitmap());
                imageHolder.setFreshBitmap(freshBitmap);
                mySurfaceView.draw();
            }
        });

        addTextButton = (Button)findViewById(R.id.addTextButton);
        addTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    paint.setStrokeWidth(5.0f);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setTextSize(60);
                    Bitmap freshBitmap = ImageEditor.addText(imageHolder.getFreshBitmap(),
                            "Hello world", 20.0f, 50.0f, paint);
                    imageHolder.setFreshBitmap(freshBitmap);
                    mySurfaceView.draw();


            }
        });

        saveButton = (Button)findViewById(R.id.saveBtton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = imageHolder.getFreshBitmap();
                String str = outputUri.getPath();
                Log.d("ddd", str);
                String filename = "myfile.jpg";
                String string = "Hello world!";
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pauseButton = (Button)findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mySurfaceView.mediaPlayerIsPlaying()) {
                    mySurfaceView.mediaPlayerPause();
                }
                else {
                    mySurfaceView.mediaPlayerStart();
                }

            }
        });
        lineraLayout = (LinearLayout)findViewById(R.id.editorLinearLayout);

        mySurfaceView = new MySurfaceView(lineraLayout.getContext(), inputUri, imageHolder);
        lineraLayout.addView(mySurfaceView);


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = mySurfaceView.getMediaPlayerCurrentPosition();
                seekBar.setMax(mySurfaceView.getMediaPlayerDuration());
                seekBar.setProgress(currentPosition);
                handler.postDelayed(this, 100);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

