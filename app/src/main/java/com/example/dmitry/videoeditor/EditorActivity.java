package com.example.dmitry.videoeditor;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.example.dmitry.videoeditor.Views.CompressionModeFragment;
import com.example.dmitry.videoeditor.Views.ConvertingProgressFragment;
import com.example.dmitry.videoeditor.Views.ElementRedactorFragment;
import com.example.dmitry.videoeditor.Views.VideoPlayerFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import layout.PanelColors;
import layout.PanelInstrumentImage;
import layout.PanelStckers;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

public class EditorActivity
        extends Activity
        implements VideoPlayerFragment.OnVideoPlayerFragmentInteractionListener
        , CompressionModeFragment.OnFragmentInteractionListener
        , ConvertingProgressFragment.OnConvertingFragmentInteractionListener{
    public final static String INPUT_URI = "inputUri";
    public final static String OUTPUT_URI = "outputUri";

    EditText editText;

    ImageHolder imageHolder;
    LinearLayout lineraLayout;
    LinearLayout videoScrollLayoyt;
    VideoPlayerFragment videoPlayerFragment;
    CompressionModeFragment compressionModeFragment;
    ElementRedactorFragment elementRedactorHeader;
    Decoder decoder;

    Uri inputUri;
    Uri outputUri;

    Handler handler = new Handler();

    MySurfaceView mySurfaceView;
    PanelColors fragment2;
    PanelStckers panelStckers;
    PanelInstrumentImage fragment;
    int dur;

    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();

        decoder = new Decoder();
        inputUri = intent.getParcelableExtra(INPUT_URI);
        outputUri = intent.getParcelableExtra(OUTPUT_URI);

        imageHolder = ImageHolder.getInstance();
        imageHolder.tryInit(inputUri, this);

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
        final float imageSize = res.getDimension(R.dimen.mySurfaceViewSize);
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

            }

            @Override
            public void doubleClick() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

                FragmentManager fragmentManager = getFragmentManager();
                 if(fragmentManager.findFragmentById(R.id.frameLayout2) == null) {
                     elementRedactorHeader = new ElementRedactorFragment();
                     getFragmentManager()
                             .beginTransaction()
                             .setCustomAnimations(R.animator.slide_anim, 0)
                             .replace(R.id.frameLayout, elementRedactorHeader)
                             .add(R.id.frameLayout2, fragment2)
                             .commit();
                 }
            }
        });
        lineraLayout.addView(mySurfaceView);


        videoScrollLayoyt = (LinearLayout)findViewById(R.id.videoScrollLayout);

        if(Tools.isVideo(this.getBaseContext().getContentResolver().getType(inputUri))) {
            videoPlayerFragment = new VideoPlayerFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.footer_pos, videoPlayerFragment)
                    .commit();


        }
        else {

            videoScrollLayoyt.setVisibility(View.GONE);


            //TODO
           // FragmentManager fragmentManager = getFragmentManager();
           // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment = new  PanelInstrumentImage (EditorActivity.this.getBaseContext());
            fragment2 = new PanelColors (EditorActivity.this.getBaseContext());
            panelStckers = new PanelStckers(EditorActivity.this.getBaseContext());

            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.slide_anim, R.animator.slide_anim)
                    .add(R.id.frameLayout, fragment)
                    .commit();

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
                                Bitmap kropedBitmap = imageHolder.getKropedBitmap();
                                if (kropedBitmap == null) {
                                    kropedBitmap = imageHolder.getDefaultBitmap();
                                }
                                kropedBitmap = ImageEditor.krop(kropedBitmap,
                                        rect.left, rect.top, rect.right, rect.bottom);
                                imageHolder.setKropedBitmap(kropedBitmap);
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
                            this.i = (this.i + 1) % 4;
                            imageHolder.setFreshBitmap(null);
                            mySurfaceView.setEffect(this.i);
                            mySurfaceView.draw();
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
                            if (Build.VERSION.SDK_INT >= 23) {
                                ActivityCompat.requestPermissions(EditorActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISION);

                            }
                            else {
                                Bitmap bitmap =  imageHolder.getBitmapWithElements();
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "image" , null);
                                Tools.saveAndSendImage(bitmap, EditorActivity.this);
                            }
                            break;
                        }
                        case 5: {
                            mySurfaceView.imageHolder.setKropedBitmap(null);
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        FragmentManager fragmentManager2 = getFragmentManager();
        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
        fragmentTransaction2.remove(fragment);
        fragmentTransaction2.remove(fragment2);
        fragmentTransaction2.remove(panelStckers);
        fragmentTransaction2.commit();
        savedInstanceState.putParcelable(INPUT_URI, inputUri);
        savedInstanceState.putParcelable(OUTPUT_URI, outputUri);
        super.onSaveInstanceState(savedInstanceState);
    }

    final int MY_PERMISION = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Bitmap bitmap =  imageHolder.getBitmapWithElements();
                Tools.saveAndSendImage(bitmap, this);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onVideoPlayerFragmentInteraction(boolean bul) {
        if(mySurfaceView.mediaPlayerIsPlaying()) {
            mySurfaceView.mediaPlayerPause();
        }
        else {
            mySurfaceView.mediaPlayerStart();
        }
    }


    @Override
    public void ready(Object object) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(videoPlayerFragment != null)
                    videoPlayerFragment.updateProgess(mySurfaceView.getMediaPlayerCurrentPosition(),
                            mySurfaceView.getMediaPlayerDuration());
                    handler.postDelayed(this, 100);
            }
        });
    }


    @Override
    public void show_compression_menu_request() {
        compressionModeFragment = new CompressionModeFragment();
        compressionModeFragment.show(getFragmentManager(), "123");
    }

    //TODO для обрезки видео leftCur, leftMax - текущее положение (leftCur / (rightMax - leftMax))
    @Override
    public void doVideoKrop(int leftCur, int rightCur, int leftMax, int rightMax) {

    }

    @Override
    public void onConvertingFragmentInteraction(Uri uri) {

    }

    //TODO
    //            convertingProgressFragment.setProgess(); - отображает прогресс

    //TODO устанавливает режим конвертации видео
    @Override
    public void onFragmentInteraction(String buttonType) {
        ConvertingProgressFragment convertingProgressFragment = new ConvertingProgressFragment();
        compressionModeFragment.dismiss();
        switch (buttonType) {
            case CompressionModeFragment.FAST_COMPRESS: {
                convertingProgressFragment.show(EditorActivity.this.getFragmentManager(), "456");
                decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH, inputUri.toString());
                decoder.outputFile(inputUri.toString() + ".mp4");
                decoder.setVideoCodec(Decoder.name_video_codec.MPEG4);

                //mySurfaceView.updateVideo(Uri.parse(inputUri.toString() + ".mp4"));
                break;
            }
            case CompressionModeFragment.QUALITY_COMPRESS: {
                convertingProgressFragment.show(EditorActivity.this.getFragmentManager(), "456");
                break;
            }
            case CompressionModeFragment.WITHOUT_COMPRESS: {
                //convertingProgressFragment.show(EditorActivity.this.getFragmentManager(), "456");
                break;
            }
        }
    }
}

