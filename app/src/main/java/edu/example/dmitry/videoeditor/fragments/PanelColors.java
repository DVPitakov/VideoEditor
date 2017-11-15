package edu.example.dmitry.videoeditor.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import edu.example.dmitry.videoeditor.adapters.ColorAdapter;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.R;

import java.util.ArrayList;

/**
 * Created by dmitry on 16.09.17.
 */

public class PanelColors extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.fragment_image_ok_button).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_image_send_button).setVisibility(View.GONE);

    }

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
        this.onItemClickListener = new AdapterView.OnItemClickListener() {
            int[] Colors = {
                    Color.WHITE,
                    Color.BLUE,
                    Color.RED,
                    Color.GREEN,
                    Color.GRAY,
                    Color.YELLOW};

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SurfaceViewHolder.getInstance().getMySurfaceView().setImageColor(Colors[i]);
                ImageHolder.getInstance().setBitmapWithElements(null);
                SurfaceViewHolder.getInstance().getMySurfaceView().draw();

            }
        };
        return listView;

    }

}
