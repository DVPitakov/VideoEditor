package com.example.dmitry.videoeditor.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.List;

/**
 * Created by dmitry on 17.10.17.
 */

public class ImageWithTextAdapter extends ArrayAdapter<IconWithText> {


    private int resource;
    public ImageWithTextAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<IconWithText> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconWithText iconWithText = getItem(position);

        if (true) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(resource, parent, false);
            ((ImageView) (convertView.findViewById(R.id.image_with_text_icon)))
                    .setImageResource(iconWithText.getIconResource());
            ((TextView) (convertView.findViewById(R.id.image_with_text_text)))
                    .setText(iconWithText.getText());
        }

        return convertView;
    }
}