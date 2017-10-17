package layout;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import com.example.dmitry.videoeditor.Adapters.ImageWithTextAdapter;
import com.example.dmitry.videoeditor.Adapters.StickerAdapter;
import com.example.dmitry.videoeditor.R;

import java.util.ArrayList;

/**
 * Created by dmitry on 17.09.17.
 */

public class PanelStckers extends Fragment {

    public interface OnStickerClickListener {
        void onStickerClick(int sticker);
    }

    OnStickerClickListener onStickerClickListener;
    private ArrayList<Integer> arrayList = new ArrayList<>();
    public static final Integer[] ITEMS =  new Integer[] {

    };
    public PanelStckers(Context c) {
        arrayList.add( R.drawable.s1);
        arrayList.add( R.drawable.s2);
        arrayList.add( R.drawable.s3);
        arrayList.add( R.drawable.s4);
        arrayList.add( R.drawable.s5);
        arrayList.add( R.drawable.s6);
        arrayList.add( R.drawable.s7);
        arrayList.add( R.drawable.s8);
        arrayList.add( R.drawable.s9);
        arrayList.add( R.drawable.s10);
        arrayList.add( R.drawable.s11);
        arrayList.add( R.drawable.s12);
        arrayList.add( R.drawable.s13);
        arrayList.add( R.drawable.s14);
        arrayList.add( R.drawable.s15);
        arrayList.add( R.drawable.s16);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StickerAdapter dataAdapter = new StickerAdapter(container.getContext()
                , R.layout.item_image_with_text
                , arrayList);
        GridView gridView = new GridView(container.getContext());
        gridView.setNumColumns(5);
        gridView.setAdapter(dataAdapter);
        //container.addView(gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onStickerClickListener != null) {
                    onStickerClickListener.onStickerClick(arrayList.get(i));
                }
            }
        });
        return gridView;

    }

    public void setOnStickerClickListener(OnStickerClickListener onStickerClickListener) {
        this.onStickerClickListener = onStickerClickListener;

    }
}
