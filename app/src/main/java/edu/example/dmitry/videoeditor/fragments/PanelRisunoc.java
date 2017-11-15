package edu.example.dmitry.videoeditor.fragments;

import android.view.View;
import android.widget.AdapterView;

import edu.example.dmitry.videoeditor.adapters.ImageAdapter;
import edu.example.dmitry.videoeditor.EditorActivity;
import edu.example.dmitry.videoeditor.holders.CurrentElementHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.models.IconWithText;
import edu.example.dmitry.videoeditor.views.MySurfaceView;
import edu.example.dmitry.videoeditor.R;
import edu.example.dmitry.videoeditor.items.RisunocItem;
import edu.example.dmitry.videoeditor.Tools;

import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */

public class PanelRisunoc extends ImageAdapter {
    public PanelRisunoc() {
        super();
        arrayList = new ArrayList<IconWithText>();
        arrayList.add(new IconWithText(R.drawable.ic_check_white_24dp, "Принять"));
        arrayList.add(new IconWithText(R.drawable.paint_size_minus_24dp, "Уменьшить\nкисть"));
        arrayList.add(new IconWithText(R.drawable.ic_paint_size_plus_24dp, "Увеличить\nкисть"));
        arrayList.add(new IconWithText(R.drawable.ic_delete_white_24dp, "Удалить"));

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean b = false;
            int i = 0;
            int j = 2;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case 0: {
                        if (CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocItem) {
                            ((RisunocItem)CurrentElementHolder.getInstance().getCurrentElement()).setReady(true);
                            CurrentElementHolder.getInstance().removeCurrentElement();
                        }
                        ((EditorActivity) (getActivity())).removeFragment(PanelColors.class);
                        ((EditorActivity) (getActivity())).showDefaultImageHeader();
                        break;
                    }
                    case 1: {
                        j = Tools.max(1, (j - 1));
                        SurfaceViewHolder.getInstance().getMySurfaceView().setImageSize(j);
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                        break;
                    }
                    case 2: {
                        j = Tools.min(8, (j + 1));
                        SurfaceViewHolder.getInstance().getMySurfaceView().setImageSize(j);
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                        break;
                    }
                    case 3: {
                        ((RisunocItem) CurrentElementHolder.getInstance().getCurrentElement()).setReady(true);
                        mySurfaceView.deleteCurrentItem();
                        CurrentElementHolder.getInstance().removeCurrentElement();
                        ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);
                        ((EditorActivity)(getActivity())).showDefaultImageHeader();
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                        break;
                    }

                }
            }
        });
    }


}
