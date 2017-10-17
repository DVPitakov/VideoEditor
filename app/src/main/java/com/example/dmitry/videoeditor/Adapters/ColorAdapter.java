package com.example.dmitry.videoeditor.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.List;

/**
 * Created by dmitry on 17.10.17.
 */

public class ColorAdapter extends ArrayAdapter<Integer> {
    private int resource;
    private List<Integer> colors;
    public  ColorAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.colors = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new View(parent.getContext()){
                @Override
                protected void onDraw(Canvas canvas) {
                    Resources res = getResources();
                    float imageSize = res.getDimension(R.dimen.imageAdapterImageSize);
                    Paint paint = new Paint();
                    paint.setColor(colors.get(position));
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawCircle((int)(imageSize/2),(int)(imageSize/2),(int)(imageSize/2), paint);
                }
            };
            float imageSize = parent.getContext()
                    .getResources()
                    .getDimension(R.dimen.imageAdapterImageSize);
            convertView.setLayoutParams(new GridView.LayoutParams((int)imageSize, (int)imageSize));
            convertView.setPadding(8, 8, 8, 8);

        }
        return convertView;
    }
}
