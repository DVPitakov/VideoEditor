package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import edu.example.dmitry.videoeditor.adapters.ImageAdapter;
import edu.example.dmitry.videoeditor.holders.CurrentElementHolder;
import edu.example.dmitry.videoeditor.holders.HistoryHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.items.RisunocItem;
import edu.example.dmitry.videoeditor.items.TextItem;
import edu.example.dmitry.videoeditor.models.IconWithText;
import edu.example.dmitry.videoeditor.models.SettingsData;
import edu.example.dmitry.videoeditor.views.MySurfaceView;
import edu.example.dmitry.videoeditor.R;


public class PanelInstrumentImageShared extends ImageAdapter {
    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.fragment_image_ok_button).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_image_send_button).setVisibility(View.VISIBLE);
    }


    public final static int KROP_BUTTON = 0;
    public final static int TEXT_BUTTON = 1;
    public final static int EFFECT_BUTTON = 2;
    static boolean b = false;
    final int MY_PERMISION = 1;

    public PanelInstrumentImageShared() {
        super();
        arrayList.add(new IconWithText(R.drawable.ic_mode_edit_white_24dp, "Рисовать"));
        arrayList.add(new IconWithText(R.drawable.ic_account_box_white_24dp, "Фон"));
        arrayList.add(new IconWithText(R.drawable.ic_photo_camera_white_24dp, "Камера"));
        arrayList.add(new IconWithText(R.drawable.ic_settings_black_24dp, "Настрой"));
        arrayList.add(new IconWithText(R.drawable.ic_chevron_left_white_24dp, "Отмена"));
        arrayList.add(new IconWithText(R.drawable.ic_chevron_right_white_24dp, "Вперед"));
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case 0: {
                        CurrentElementHolder.getInstance().removeCurrentElement();
                        mySurfaceView.addImageElement(
                                new RisunocItem(SurfaceViewHolder.getInstance().getMySurfaceView()
                                        , 0
                                        , 0));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        mySurfaceView.draw();
                        editorActivity.showColors();
                        editorActivity.showFragment(PanelRisunoc.class, R.id.header_pos);
                        break;
                    }
                    case 1: {
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
                    case 2: {
                        editorActivity.showFragment(HorizontalImagesScrallFragment.class, R.id.header_pos);
                        break;
                    }
                    case 3: {
                        Log.d("1216", "i am here");
                        editorActivity.showFragment(SettingsFragment.class, R.id.settings_pos);
                        break;
                    }
                    case 4: {
                         if(SettingsData.getInstance(getContext()).isHistory() &&  HistoryHolder.getInstance().back()) {
                             SurfaceViewHolder.getInstance().getMySurfaceView().focusLose();
                             SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                         }
                        break;
                    }
                    case 5: {
                        if(HistoryHolder.getInstance().future()) {
                            SurfaceViewHolder.getInstance().getMySurfaceView().focusLose();
                            SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                        }
                        break;
                    }
                }
            }
        });
    }


}


