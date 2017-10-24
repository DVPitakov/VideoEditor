package com.example.dmitry.videoeditor;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.dmitry.videoeditor.Holders.FontHolder;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.Holders.UrlHolder;
import com.example.dmitry.videoeditor.Views.ElementRedactorFragment;
import com.example.dmitry.videoeditor.Views.ImageFragment;
import com.example.dmitry.videoeditor.Views.VideoFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import layout.PanelColors;
import layout.PanelInstrumentImage;
import layout.PanelStckers;

public class EditorActivity extends Activity {
    public final static String INPUT_URI = "inputUri";
    public final static String OUTPUT_URI = "outputUri";


    public EditText editText;

    VideoFragment videoFragment;
    PanelColors panelColors;
    PanelStckers panelStckers;
    ElementRedactorFragment elementRedactorFragment;



    public void removeFragment(Class fragmentClass) {
        Fragment fragment  = getFragmentManager().findFragmentByTag(fragmentClass.getName());
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }
    public void showFragment(Class fragment, int position) {
        try {
            Fragment obj = getFragmentManager().findFragmentByTag(fragment.getName());
            if (obj == null) {
                Constructor con = fragment.getConstructor();
                obj = (Fragment) con.newInstance();
            }
            getFragmentManager()
                    .beginTransaction()
                    .replace(position, obj, obj.getClass().getName())
                    .commit();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void showStickers() {
        if(panelStckers == null) {
            panelStckers = new PanelStckers();
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.footer_pos, panelStckers, panelStckers.getClass().getName())
                .commit();
    }

    public void showColors() {
        if (panelColors == null) {
            panelColors = new PanelColors();
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.footer_pos, panelColors, panelColors.getClass().getName())
                .commit();
    }

    public void showDefaultImageHeader() {
        PanelInstrumentImage defaultImageHeaderFragment
                = (PanelInstrumentImage)getFragmentManager()
                .findFragmentByTag(PanelInstrumentImage.class.getName());
        if(defaultImageHeaderFragment == null) {
            defaultImageHeaderFragment = new PanelInstrumentImage();
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.slide_anim, R.animator.slide_anim)
                    .replace(R.id.header_pos, defaultImageHeaderFragment
                            , PanelInstrumentImage.class.getName())
                    .commit();
        }
        else {
            defaultImageHeaderFragment = new PanelInstrumentImage();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.header_pos, defaultImageHeaderFragment
                            , PanelInstrumentImage.class.getName())
                    .commit();
        }
    }

    public void showDefaultVideoHeader() {

    }

    public void showRedactorItemHeader() {
        if(elementRedactorFragment == null) {
            elementRedactorFragment = new ElementRedactorFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_anim, R.animator.slide_anim)
                .replace(R.id.header_pos, elementRedactorFragment
                        , elementRedactorFragment.getClass().getName())
                .commit();

    }

    public void showVideoBody() {
        if(videoFragment == null) {
            videoFragment = new VideoFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.body_pos, videoFragment
                        , videoFragment.getClass().getName())
                .commit();
    }

    public void showImageBody() {
        ImageFragment imageFragment
                = (ImageFragment)getFragmentManager().findFragmentByTag(ImageFragment.class.getName());
        if(imageFragment == null) {
            imageFragment = new ImageFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_anim, R.animator.slide_anim)
                .replace(R.id.body_pos, imageFragment
                        , imageFragment.getClass().getName())
                .commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editText = (EditText)findViewById(R.id.edutText);
        editText.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                if (SurfaceViewHolder.getInstance().getMySurfaceView() != null)
                                                    SurfaceViewHolder.getInstance().getMySurfaceView().setImageText(charSequence.toString());
                                                ImageHolder.getInstance().setBitmapWithElements(null);
                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                if (SurfaceViewHolder.getInstance().getMySurfaceView() != null)
                                                    SurfaceViewHolder.getInstance().getMySurfaceView().setImageText(charSequence.toString());
                                                ImageHolder.getInstance().setBitmapWithElements(null);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                if (SurfaceViewHolder.getInstance().getMySurfaceView() != null)
                                                    SurfaceViewHolder.getInstance().getMySurfaceView().setImageText(editable.toString());
                                                ImageHolder.getInstance().setBitmapWithElements(null);

                                            }
                                        });
        FontHolder.getInstance(getBaseContext());
        if(Tools.isVideo(UrlHolder.getInpurUrl())) {
            showDefaultVideoHeader();
            showVideoBody();
        }
        else {
            showImageBody();
            showDefaultImageHeader();
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
        if (videoFragment != null) {
            fragmentTransaction.remove(videoFragment);
        }
        SurfaceViewHolder.getInstance().setMySurfaceView(null);
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

