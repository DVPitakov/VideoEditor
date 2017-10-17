package layout;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.dmitry.videoeditor.Adapters.ImageWithTextAdapter;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.ArrayList;


class ImageAdapter extends Fragment {
    public ArrayList<IconWithText> arrayList = new ArrayList<>();
    protected int layout = R.layout.fragment_instrumen_panel_list;
    protected int id = R.id.gridPanelImage;
    protected int itemSize = R.dimen.imageAdapterImageSize;
    protected Context mContext;
    protected ImageWithTextAdapter dataAdapter;
    AdapterView.OnItemClickListener onItemClickListener;

    // references to our images
    public	Integer[] mThumbIds;

    public ImageAdapter(Context c) {
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

        dataAdapter = new ImageWithTextAdapter(mContext, R.layout.item_image_with_text, arrayList);
        //container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //        ViewGroup.LayoutParams.WRAP_CONTENT ));
        GridView listView = new GridView(container.getContext());
        GridView.LayoutParams layoutParams = new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT
                , GridView.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(layoutParams);
        listView.setNumColumns(arrayList.size());
        listView.setAdapter(dataAdapter);
        //container.addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, i, l);
                }
            }
        });
        return listView;

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }
}