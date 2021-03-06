package edu.example.dmitry.videoeditor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import edu.example.dmitry.videoeditor.holders.FontHolder;
import edu.example.dmitry.videoeditor.holders.HistoryHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.fragments.ElementRedactorFragment;
import edu.example.dmitry.videoeditor.fragments.ImageFragment;
import edu.example.dmitry.videoeditor.fragments.SettingsFragment;
import edu.example.dmitry.videoeditor.fragments.VideoFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import edu.example.dmitry.videoeditor.fragments.PanelColors;
import edu.example.dmitry.videoeditor.fragments.PanelInstrumentImage;
import edu.example.dmitry.videoeditor.fragments.PanelStckers;
import edu.example.dmitry.videoeditor.items.ImageEditorQueue;
import edu.example.dmitry.videoeditor.models.SettingsData;
import edu.example.dmitry.videoeditor.views.MySurfaceView;

public class EditorActivity extends FragmentActivity {
    public final static String INPUT_URI = "inputUri";
    public final static String OUTPUT_URI = "outputUri";
    public final static String IMAGE_BITMAP = "imageBitmap";


    public EditText editText;

    VideoFragment videoFragment;
    PanelColors panelColors;
    PanelStckers panelStckers;
    ElementRedactorFragment elementRedactorFragment;


    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getName());
        if (f != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(f)
                    .commit();
        }
        else if(SettingsData.getInstance(getApplicationContext()).isHistory() &&  HistoryHolder.getInstance().back()) {
            MySurfaceView surfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
            if (surfaceView == null) {
                ImageFragment imageFragment = (ImageFragment)
                        getSupportFragmentManager().findFragmentByTag(ImageFragment.class.getName());
                if (imageFragment != null) {
                    imageFragment.updateSurface();
                }
            }
            SurfaceViewHolder.getInstance().getMySurfaceView().focusLose();
            SurfaceViewHolder.getInstance().getMySurfaceView().draw();
        }
        else {
            HistoryHolder.getInstance().clear();
            super.onBackPressed();
        }
    }


    public void removeFragment(Class fragmentClass) {
        Fragment fragment  = getSupportFragmentManager().findFragmentByTag(fragmentClass.getName());
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }
    public void showFragment(Class fragment, int position) {
        try {
            Fragment obj = getSupportFragmentManager().findFragmentByTag(fragment.getName());
            if (obj == null) {
                Constructor con = fragment.getConstructor();
                obj = (Fragment) con.newInstance();
            }
            getSupportFragmentManager()
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer_pos, panelStckers, panelStckers.getClass().getName())
                .commit();
    }

    public void showColors() {
        if (panelColors == null) {
            panelColors = new PanelColors();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer_pos, panelColors, panelColors.getClass().getName())
                .commit();
    }

    public void showDefaultImageHeader() {
        PanelInstrumentImage defaultImageHeaderFragment
                = (PanelInstrumentImage)getSupportFragmentManager()
                .findFragmentByTag(PanelInstrumentImage.class.getName());
        if(defaultImageHeaderFragment == null) {
            defaultImageHeaderFragment = new PanelInstrumentImage();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.header_pos, defaultImageHeaderFragment
                            , PanelInstrumentImage.class.getName())
                    .commit();
        }
        else {
            defaultImageHeaderFragment = new PanelInstrumentImage();
            getSupportFragmentManager()
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.header_pos, elementRedactorFragment
                        , elementRedactorFragment.getClass().getName())
                .commit();

    }

    public void showVideoBody() {
        if(videoFragment == null) {
            videoFragment = new VideoFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_pos, videoFragment
                        , videoFragment.getClass().getName())
                .commit();
    }

    public void showImageBody() {
        ImageFragment imageFragment
                = (ImageFragment)getSupportFragmentManager().findFragmentByTag(ImageFragment.class.getName());
        if(imageFragment == null) {
            imageFragment = new ImageFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_pos, imageFragment
                        , imageFragment.getClass().getName())
                .commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LIFE_CIRCLE", "editor_activity_created");
        int state = 0;
        if (savedInstanceState != null) {
            state = savedInstanceState.getInt(EDITOR_STATE);

        }
        setContentView(R.layout.activity_editor);
        if (state == 1) {
            ImageHolder.getInstance().setFreshBitmap(null);
        }
        else {
            ImageHolder.getInstance().clear();
            HistoryHolder.getInstance().clear();
            ImageEditorQueue.getInstance().clear();
        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Tools.isVideo(SettingsVideo.getInput(""))) {
            showVideoBody();
            showDefaultVideoHeader();
        }
        else {
            showImageBody();
            showDefaultImageHeader();
        }

    }
    private final String EDITOR_STATE = "ImageHolder.editor_state";

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (videoFragment != null) {
            fragmentTransaction.remove(videoFragment);
        }

        savedInstanceState.putInt(EDITOR_STATE, 1);


        SurfaceViewHolder.getInstance().setMySurfaceView(null);
        fragmentTransaction.commit();
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id ==777) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setCancelable(false);
            adb.setTitle("Картинка не подходит");
            adb.setMessage("Возможно картинка повреждена или имеет неподдерживаемый формат");
            adb.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            return adb.create();
        }
        return super.onCreateDialog(id);
    }



}

