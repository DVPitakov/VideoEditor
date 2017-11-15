package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import edu.example.dmitry.videoeditor.adapters.ImageAdapter;
import edu.example.dmitry.videoeditor.holders.CurrentElementHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.models.IconWithText;
import edu.example.dmitry.videoeditor.views.MySurfaceView;
import edu.example.dmitry.videoeditor.R;
import edu.example.dmitry.videoeditor.items.TextItem;

import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */


public class PanelInstrumentImage extends ImageAdapter {
    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.fragment_image_ok_button).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_image_send_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        Fragment sharedPanel = editorActivity
                .getSupportFragmentManager()
                .findFragmentByTag(PanelInstrumentImageShared.class.getName());
        if (sharedPanel != null) {
            editorActivity
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .remove(sharedPanel)
                    .commit();
        }
        super.onPause();
    }


    public final static int KROP_BUTTON = 0;
    public final static int TEXT_BUTTON = 1;
    public final static int EFFECT_BUTTON = 2;
    static boolean b = false;

    public PanelInstrumentImage() {
        super();
        arrayList = new ArrayList<IconWithText>();
        arrayList.add(new IconWithText(R.drawable.ic_crop_white_24dp, "Кроп"));
        arrayList.add(new IconWithText(R.drawable.ic_text_fields_white_24dp, "Текст"));
        arrayList.add(new IconWithText(R.drawable.ic_photo_filter_white_24dp, "Эффект"));
        arrayList.add(new IconWithText(R.drawable.ic_image_white_24dp, "Стикер"));
        arrayList.add(new IconWithText(R.drawable.ic_cancel_black_24dp, "Отмена"));
        arrayList.add(new IconWithText(R.drawable.ic_keyboard_arrow_down_white_24dp, "InDev"));
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean sharedOpen = false;
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case KROP_BUTTON: {
                        mySurfaceView.kropSet();
                        editorActivity.showFragment(PanelKrop.class, R.id.header_pos);
                        break;
                    }
                    case TEXT_BUTTON: {
                        CurrentElementHolder.getInstance().removeCurrentElement();
                        ((EditText)(editorActivity.findViewById(R.id.edutText))).setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(((EditText)(editorActivity.findViewById(R.id.edutText))), InputMethodManager.SHOW_FORCED);
                        PointF pf = SurfaceViewHolder.getInstance().getMySurfaceView().getCenter();
                        mySurfaceView.addImageElement(new TextItem(SurfaceViewHolder.getInstance().getMySurfaceView(),"Новый текст", (int)pf.x, (int)pf.y));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        mySurfaceView.draw();
                        editorActivity.showColors();
                        editorActivity.showRedactorItemHeader();
                        break;
                    }
                    case EFFECT_BUTTON: {
                        editorActivity.showFragment(HorizontalImagesScrallFragment.class, R.id.header_pos);
                        break;
                    }
                    case 3: {
                        editorActivity.showStickers();
                        break;
                    }
                    case 4: {
                        ImageHolder.getInstance().setKropedBitmap(null);
                        mySurfaceView.imageEditorQueue.clear();
                        mySurfaceView.draw();
                        break;
                    }
                    case 5: {
                        if (sharedOpen) {
                            Fragment f = editorActivity
                                    .getSupportFragmentManager()
                                    .findFragmentByTag(PanelInstrumentImageShared.class.getName());
                            if (f != null) {
                                editorActivity
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .remove(f)
                                        .commit();
                                ((ImageView)(view.findViewById(R.id.image_with_text_icon))).setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                            }
                        } else {
                        editorActivity
                                .showFragment(PanelInstrumentImageShared.class, R.id._shared_header_pos);
                            ((ImageView)(view.findViewById(R.id.image_with_text_icon))).setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                        }
                        sharedOpen = !sharedOpen;
                        break;
                    }
                }
            }
        });
    }


}


