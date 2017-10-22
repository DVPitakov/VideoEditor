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
import com.example.dmitry.videoeditor.EditorActivity;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.ArrayList;


public class ImageAdapter extends Fragment {
    public ArrayList<IconWithText> arrayList = new ArrayList<>();
    protected int layout = R.layout.fragment_instrumen_panel_list;
    protected int id = R.id.gridPanelImage;
    protected int itemSize = R.dimen.imageAdapterImageSize;
    //protected Context mContext;
    protected ImageWithTextAdapter dataAdapter;
    protected EditorActivity editorActivity;
    AdapterView.OnItemClickListener onItemClickListener;

    // references to our images
    public	Integer[] mThumbIds;

    public ImageAdapter() {
        super();
    }

    //public ImageAdapter(Context c) {
    //    super();
    //    mContext = c;
    //
    //}

    public int getCount() {
        return mThumbIds.length;

    }


    public long getItemId(int position) {
        return position;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof EditorActivity) {
            editorActivity = (EditorActivity) getActivity();
        }
        else {
            throw new RuntimeException(context.toString()
                    + "shud called by editorActivity err id 001");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        editorActivity = (EditorActivity) getActivity();
        dataAdapter = new ImageWithTextAdapter(getActivity().getBaseContext()
                , R.layout.item_image_with_text, arrayList);
        GridView listView = new GridView(container.getContext());
        GridView.LayoutParams layoutParams = new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT
                , GridView.LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(layoutParams);
        listView.setNumColumns(arrayList.size());
        listView.setAdapter(dataAdapter);
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