package com.example.dmitry.videoeditor.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 17.10.17.
 */

public class StickerAdapter extends ArrayAdapter<Integer> {
    private int resource;
    private List<Integer> stickers;
    public  StickerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.stickers = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(parent.getContext());
            ((ImageView)convertView).setImageResource(stickers.get(position));
            float imageSize = parent.getContext()
                    .getResources()
                    .getDimension(R.dimen.imageAdapterImageSize);
            convertView.setLayoutParams(new GridView.LayoutParams((int)imageSize, (int)imageSize));
            convertView.setPadding(8, 8, 8, 8);

        }
        return convertView;
    }
}
