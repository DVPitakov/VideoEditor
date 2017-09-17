package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import java.io.FileOutputStream;

import layout.PanelColors;
import layout.PanelInstrumentImage;
import layout.PanelStckers;

public class EditorActivity extends Activity {


    public final static String INPUT_URI = "inputUri";
    public final static String OUTPUT_URI = "outputUri";



    Button pauseButton;

    EditText editText;

    ImageHolder imageHolder;
    LinearLayout lineraLayout;
    LinearLayout videoScrollLayoyt;

    Uri inputUri;
    Uri outputUri;

    MySurfaceView mySurfaceView;
    SeekBar seekBar;
    Handler handler = new Handler();
    PanelColors fragment2;
    PanelStckers panelStckers;


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
                mySurfaceView.setImageText(charSequence.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mySurfaceView.setImageText(charSequence.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mySurfaceView.setImageText(editable.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);

            }
        });

        lineraLayout = (LinearLayout)findViewById(R.id.editorLinearLayout);


        mySurfaceView = new MySurfaceView(lineraLayout.getContext(), inputUri, imageHolder);
        Resources res = getResources();
        float imageSize = res.getDimension(R.dimen.mySurfaceViewSize);
        mySurfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)imageSize));
        mySurfaceView.setFocusListener(new MySurfaceView.FocusListener() {
            @Override
            public void focusLosed() {
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment2);
                fragmentTransaction.commit();

            }

            @Override
            public void focusTaken() {
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

                //FragmentManager fragmentManager = getFragmentManager();
               // if(fragmentManager.findFragmentById(R.id.frameLayout2) == null) {
              //      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               //     fragmentTransaction.add(R.id.frameLayout2, fragment2);
               //     fragmentTransaction.commit();
               // }
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
        if(Tools.isVideo(this.getBaseContext().getContentResolver().getType(inputUri))) {
            videoScrollLayoyt.setVisibility(View.VISIBLE);
        }
        else {
            videoScrollLayoyt.setVisibility(View.GONE);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            PanelInstrumentImage fragment = new  PanelInstrumentImage (EditorActivity.this.getBaseContext());
            fragment2 = new PanelColors (EditorActivity.this.getBaseContext());
            panelStckers = new PanelStckers(EditorActivity.this.getBaseContext());

            fragmentTransaction.add(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
            final int KROP_ELEMENT = 0;
            final int FILTER_ELEMENT = 2;
            final int TEXT_ELEMENT = 1;
            final int IMAGE_ELEMENT = 3;

            fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                boolean b =false;
                int i = 0;
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case KROP_ELEMENT: {
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
                            break;
                        }
                        case FILTER_ELEMENT: {
                            Bitmap freshBitmap;
                            switch (this.i) {
                                case 0: {
                                    freshBitmap = ImageEditor.inversion(imageHolder.getDefaultBitmap());
                                    break;
                                }
                                case 1: {
                                    freshBitmap = ImageEditor.bombit(imageHolder.getDefaultBitmap());
                                    break;
                                }
                                case 2: {
                                    freshBitmap = ImageEditor.bombit2(imageHolder.getDefaultBitmap());
                                    break;
                                }
                                default: freshBitmap = ImageEditor.inversion(imageHolder.getDefaultBitmap());
                            }
                            imageHolder.setFreshBitmap(freshBitmap);
                            mySurfaceView.draw();
                            this.i = (this.i + 1) % 3;
                            break;
                        }
                        case TEXT_ELEMENT: {
                            mySurfaceView.selectedImageElement = null;
                            editText.setText("");
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                            mySurfaceView.addImageElement(new TextImage("Новый текст", 60, 60));
                            Log.d("step", "step1");
                            FragmentManager fragmentManager = getFragmentManager();
                            if(fragmentManager.findFragmentById(R.id.frameLayout2) == null) {
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.frameLayout2, fragment2);
                                fragmentTransaction.commit();
                            }
                            mySurfaceView.imageHolder.setBitmapWithElements(null);
                            mySurfaceView.draw();
                            break;
                        }
                        case IMAGE_ELEMENT: {
                            FragmentManager fragmentManager = getFragmentManager();
                            if(fragmentManager.findFragmentById(R.id.stickersLayout) == null) {
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.stickersLayout, panelStckers);
                                fragmentTransaction.commit();
                            }
                            break;
                        }
                        case 4: {
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
                            break;
                        }
                        case 5: {
                            mySurfaceView.imageHolder.setFreshBitmap(null);
                            mySurfaceView.imageEditorQueue.clear();
                            mySurfaceView.draw();
                            break;
                        }
                    }
                }
            });

            fragment2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                int []Colors = {
                        Color.WHITE,
                        Color.BLUE,
                        Color.RED,
                        Color.GREEN,
                        Color.GRAY,
                        Color.YELLOW};
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mySurfaceView.setImageColor(Colors[i]);
                    mySurfaceView.imageHolder.setBitmapWithElements(null);
                    mySurfaceView.draw();

                }
            });

            panelStckers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mySurfaceView.addImageElement(new IconImage(PanelStckers.ITEMS[i], mySurfaceView, 60, 60));
                    mySurfaceView.imageHolder.setBitmapWithElements(null);
                    mySurfaceView.draw();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(panelStckers);
                    fragmentTransaction.commit();
                }
            });
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

