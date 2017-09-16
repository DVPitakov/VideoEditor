package layout;

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
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dmitry.videoeditor.R;

/**
 * Created by dmitry on 16.09.17.
 */

public class PanelColors extends ImageAdapter{

    public  PanelColors(Context c) {
        super(c);
        mThumbIds = new Integer[]{
                Color.WHITE,
                Color.BLUE,
                Color.RED,
                Color.GREEN,
                Color.GRAY,
                Color.YELLOW
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataAdapter = new DataAdapter(mContext, R.id.gridPanelImage){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    Log.d("here", "were");
                    view = new View(getActivity()){
                        @Override
                        protected void onDraw(Canvas canvas) {
                            Resources res = getResources();
                            float imageSize = res.getDimension(R.dimen.imageAdapterImageSize);
                            Paint paint = new Paint();
                            paint.setColor( mThumbIds[position]);
                            paint.setStyle(Paint.Style.FILL_AND_STROKE);
                            canvas.drawCircle((int)(imageSize/2),(int)(imageSize/2),(int)(imageSize/2), paint);
                        }
                    };
                    Resources res = getResources();
                    float imageSize = res.getDimension(R.dimen.imageAdapterImageSize);
                    view.setLayoutParams(new GridView.LayoutParams((int)imageSize, (int)imageSize));
                    view.setPadding(8, 8, 8, 8);

                } else {
                    view =  convertView;
                }
                return view;
            }
        };
        View view = inflater.inflate(R.layout.fragment_instrumen_panel_list, null);
        gridView = (GridView) view.findViewById(R.id.gridPanelImage);
        gridView.setAdapter(dataAdapter);
        //gridView.setAlpha(0.5f);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, i, l);
                }
            }
        });
        return view;

    }



}
