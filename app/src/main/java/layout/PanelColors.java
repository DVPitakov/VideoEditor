package layout;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.dmitry.videoeditor.Adapters.ColorAdapter;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.ArrayList;

/**
 * Created by dmitry on 16.09.17.
 */

public class PanelColors extends Fragment {
    private ArrayList<Integer> arrayList = new ArrayList<>();
    AdapterView.OnItemClickListener onItemClickListener;
    public  PanelColors() {
        arrayList.add(Color.WHITE);
        arrayList.add(Color.BLUE);
        arrayList.add(Color.RED);
        arrayList.add(Color.GREEN);
        arrayList.add(Color.GRAY);
        arrayList.add(Color.YELLOW);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ColorAdapter colorAdapter = new ColorAdapter(container.getContext(), R.layout.item_color
                , arrayList);
        GridView listView = new GridView(container.getContext());
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setNumColumns(arrayList.size());
        listView.setAdapter(colorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, i, l);
                }
            }
        });
       // container.addView(listView);
        return listView;

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

}
