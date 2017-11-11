package edu.example.dmitry.videoeditor.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
        //if (convertView == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            convertView = new ImageView(parent.getContext());

            ((ImageView)convertView)
                    .setImageBitmap(BitmapFactory
                            .decodeResource(parent.getResources()
                                    , stickers.get(position)
                                    , options));
            float imageSize = parent.getContext()
                    .getResources()
                    .getDimension(edu.example.dmitry.videoeditor.R.dimen.imageAdapterImageSize);
            convertView.setLayoutParams(new GridView.LayoutParams((int)imageSize, (int)imageSize));
            convertView.setPadding(8, 8, 8, 8);

        //}
        return convertView;
    }
}
