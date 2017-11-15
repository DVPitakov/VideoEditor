package edu.example.dmitry.videoeditor.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.example.dmitry.videoeditor.models.IconWithText;

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
            ((ImageView) (convertView.findViewById(edu.example.dmitry.videoeditor.R.id.image_with_text_icon)))
                    .setImageResource(iconWithText.getIconResource());
            ((TextView) (convertView.findViewById(edu.example.dmitry.videoeditor.R.id.image_with_text_text)))
                    .setText(iconWithText.getText());
        }

        return convertView;
    }
}