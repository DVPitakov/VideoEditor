package edu.example.dmitry.videoeditor.fragments;

import android.view.View;
import android.widget.AdapterView;

import edu.example.dmitry.videoeditor.adapters.ImageAdapter;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.models.IconWithText;
import edu.example.dmitry.videoeditor.views.MySurfaceView;
import edu.example.dmitry.videoeditor.R;

import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */

public class PanelKrop extends ImageAdapter {
    public final static int KROP_BUTTON = 0;
    public final static int TEXT_BUTTON = 1;
    public final static int EFFECT_BUTTON = 2;
    static boolean b = false;
    final int MY_PERMISION = 1;
    public PanelKrop() {
        super();
        arrayList = new ArrayList<IconWithText>();
        arrayList.add(new IconWithText(R.drawable.ic_check_white_24dp, "Готово"));
        arrayList.add(new IconWithText(R.drawable.ic_cancel_black_24dp, "Отмена"));
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case 0: {
                        editorActivity.showFragment(PanelInstrumentImage.class, R.id.header_pos);
                        mySurfaceView.doKrop();
                        break;
                    }
                    case 1: {
                        editorActivity.showFragment(PanelInstrumentImage.class, R.id.header_pos);
                        mySurfaceView.canselKrop();
                        break;
                    }


                }
            }
        });
    }


}
