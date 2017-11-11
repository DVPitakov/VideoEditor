package layout;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import edu.example.dmitry.videoeditor.Adapters.StickerAdapter;
import edu.example.dmitry.videoeditor.EditorActivity;
import edu.example.dmitry.videoeditor.Holders.ImageHolder;
import edu.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.MySurfaceView;
import edu.example.dmitry.videoeditor.R;
import edu.example.dmitry.videoeditor.Items.ImageItem;

import java.util.ArrayList;

/**
 * Created by dmitry on 17.09.17.
 */

public class PanelStckers extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.fragment_image_ok_button).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_image_send_button).setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        getActivity().findViewById(R.id.fragment_image_ok_button).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_image_send_button).setVisibility(View.VISIBLE);
        super.onStop();
    }

    public interface OnStickerClickListener {
        void onStickerClick(int sticker);
    }

    OnStickerClickListener onStickerClickListener;
    private ArrayList<Integer> arrayList = new ArrayList<>();
    public static final Integer[] ITEMS =  new Integer[] {

    };
    public PanelStckers() {
        arrayList.add( R.drawable.shapka1);
        arrayList.add( R.drawable.shapka2);
        arrayList.add( R.drawable.usi);
        arrayList.add( R.drawable.boroda_i_usi);
        arrayList.add( R.drawable.gepard);
        arrayList.add( R.drawable.automat);
        arrayList.add( R.drawable.vodka);
        arrayList.add( R.drawable.happy);
        arrayList.add( R.drawable.s1);
        arrayList.add( R.drawable.s2);
        arrayList.add( R.drawable.s3);
        arrayList.add( R.drawable.s4);
        arrayList.add( R.drawable.s5);
        arrayList.add( R.drawable.s6);
        arrayList.add( R.drawable.s7);
        arrayList.add( R.drawable.s8);
        arrayList.add( R.drawable.s9);
        arrayList.add( R.drawable.s10);
        arrayList.add( R.drawable.s11);
        arrayList.add( R.drawable.s12);
        arrayList.add( R.drawable.s13);
        arrayList.add( R.drawable.s14);
        arrayList.add( R.drawable.s15);
        arrayList.add( R.drawable.s16);
        arrayList.add( R.drawable.s17);
        arrayList.add( R.drawable.s18);
        arrayList.add( R.drawable.s19);
        arrayList.add( R.drawable.s20);
        arrayList.add( R.drawable.s21);
        arrayList.add( R.drawable.s22);
        arrayList.add( R.drawable.s23);
        arrayList.add( R.drawable.s24);
        arrayList.add( R.drawable.s25);
        arrayList.add( R.drawable.s26);
        arrayList.add( R.drawable.s27);
        arrayList.add( R.drawable.s28);
        arrayList.add( R.drawable.s29);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StickerAdapter dataAdapter = new StickerAdapter(container.getContext()
                , R.layout.item_image_with_text
                , arrayList);
        GridView gridView = new GridView(container.getContext());
        ViewGroup.LayoutParams layoutParams
                = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT
                        , (int)getResources().getDimension(R.dimen.view_with_stickers_height));
        gridView.setLayoutParams(layoutParams);
        gridView.setBackgroundColor(0xA0000000);
        gridView.setNumColumns(5);
        gridView.setAdapter(dataAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(onStickerClickListener != null) {
                    onStickerClickListener.onStickerClick(arrayList.get(i));
                }
            }
        });
        this.onStickerClickListener = new PanelStckers.OnStickerClickListener() {
            @Override
            public void onStickerClick(int sticker) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                PointF pf = SurfaceViewHolder.getInstance().getMySurfaceView().getCenter();
                mySurfaceView.addImageElement(new ImageItem(sticker
                        , mySurfaceView
                        , (int)pf.x
                        , (int)pf.y));
                ImageHolder.getInstance().setBitmapWithElements(null);
                mySurfaceView.draw();
                ((EditorActivity)(getActivity())).removeFragment(PanelStckers.class);
            }
        };
        return gridView;

    }
}
