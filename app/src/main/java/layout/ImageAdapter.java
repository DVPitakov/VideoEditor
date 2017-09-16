package layout;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitry.videoeditor.R;


public class ImageAdapter extends Fragment {
    public final static  Integer[] mContacts = {1, 2, 3, 4};

    private class DataAdapter extends ArrayAdapter<Integer> {
        Context mContext;

        public DataAdapter(Context context, int textViewResourceId) {

            super(context, textViewResourceId, mContacts);
            // TODO Auto-generated constructor stub
            this.mContext = context;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        @Override
        public Integer getItem(int position) {
            return mThumbIds[position];
        }

    }
    private Context mContext;
    protected GridView gridView;
    private DataAdapter dataAdapter;
    AdapterView.OnItemClickListener onItemClickListener;

    // references to our images
    public	Integer[] mThumbIds;

    public ImageAdapter(Context c) {
        Log.d("step", "i'm here 6");

        mContext = c;

    }

    public int getCount() {
        return mThumbIds.length;

    }


    public long getItemId(int position) {
        return position;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataAdapter = new DataAdapter(container.getContext(), R.id.gridPanelImage);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instrumen_panel_list, null);
        gridView = (GridView) view.findViewById(R.id.gridPanelImage);
        gridView.setAdapter(dataAdapter);
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
    


    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }
}