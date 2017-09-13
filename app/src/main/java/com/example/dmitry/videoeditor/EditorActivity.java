package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import layout.ImageAdapter;
import layout.PanelInstrumentImage;

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
    Button backButton;

    EditText editText;

    ImageHolder imageHolder;
    LinearLayout lineraLayout;
    LinearLayout videoScrollLayoyt;

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
            boolean b =false;
            @Override
            public void onClick(View view) {
                if (b) {
                    mySurfaceView.kropUnset();
                    Rect rect = mySurfaceView.getKropRect();
                    Bitmap freshBitmap = ImageEditor.krop(imageHolder.getFreshBitmap(),
                            rect.left, rect.top, rect.right, rect.bottom);
                    imageHolder.setFreshBitmap(freshBitmap);
                    mySurfaceView.kropClear();
                    mySurfaceView.draw();
                }
                else {
                    mySurfaceView.kropSet();
                }
                b = !b;
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                    mySurfaceView.addImageElement(new TextImage("Новый текст", 60, 60));
                    Log.d("step", "step1");
                    mySurfaceView.imageHolder.setBitmapWithElements(null);
                    mySurfaceView.draw();


            }
        });

        backButton = (Button)findViewById(R.id.backHistoryButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySurfaceView.imageHolder.setFreshBitmap(null);
                mySurfaceView.imageEditorQueue.clear();
                mySurfaceView.draw();

            }
        });


        saveButton = (Button)findViewById(R.id.saveBtton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        editText = (EditText)findViewById(R.id.edutText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mySurfaceView.setImageText(editable.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);

            }
        });

        lineraLayout = (LinearLayout)findViewById(R.id.editorLinearLayout);

        mySurfaceView = new MySurfaceView(lineraLayout.getContext(), inputUri, imageHolder);
        mySurfaceView.setFocusListener(new MySurfaceView.FocusListener() {
            @Override
            public void focusLosed() {
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            }

            @Override
            public void focusTaken() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

            }
        });
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


        videoScrollLayoyt = (LinearLayout)findViewById(R.id.videoScrollLayout);
        if(this.getBaseContext().getContentResolver().getType(inputUri).equals("video/mp4")) {
            videoScrollLayoyt.setVisibility(View.VISIBLE);
        }
        else {
            videoScrollLayoyt.setVisibility(View.GONE);
        }



        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PanelInstrumentImage fragment = new  PanelInstrumentImage (EditorActivity.this.getBaseContext());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

