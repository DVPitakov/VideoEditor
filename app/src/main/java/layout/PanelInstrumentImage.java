package layout;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.dmitry.videoeditor.Holders.CurrentElementHolder;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.MySurfaceView;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Items.RisunocItem;
import com.example.dmitry.videoeditor.Items.TextItem;
import com.example.dmitry.videoeditor.Views.HorizontalImagesScrallFragment;
import com.example.dmitry.videoeditor.Views.Rest;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */


public class PanelInstrumentImage extends ImageAdapter{
    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.fragment_image_ok_button).setVisibility(View.GONE);
        getActivity().findViewById(R.id.fragment_image_send_button).setVisibility(View.VISIBLE);

    }

    public final static int KROP_BUTTON = 0;
    public final static int TEXT_BUTTON = 1;
    public final static int EFFECT_BUTTON = 2;
    static boolean b = false;
    final int MY_PERMISION = 1;

    public PanelInstrumentImage() {
        super();
        arrayList = new ArrayList<IconWithText>();
        arrayList.add(new IconWithText(R.drawable.ic_crop_white_24dp, "Кроп"));
        arrayList.add(new IconWithText(R.drawable.ic_text_fields_white_24dp, "Текст"));
        arrayList.add(new IconWithText(R.drawable.ic_photo_filter_white_24dp, "Эффект"));
        arrayList.add(new IconWithText(R.drawable.ic_image_white_24dp, "Стикер"));
        arrayList.add(new IconWithText(R.drawable.ic_cancel_black_24dp, "Отмена"));
        arrayList.add(new IconWithText(R.drawable.ic_mode_edit_white_24dp, "Рисовать"));
       // arrayList.add(new IconWithText(R.drawable.ic_mode_edit_white_24dp, "Фон"));
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case KROP_BUTTON: {
                        mySurfaceView.kropSet();
                        editorActivity.showFragment(PanelKrop.class, R.id.header_pos);
                        break;
                    }
                    case TEXT_BUTTON: {
                        CurrentElementHolder.getInstance().removeCurrentElement();
                        ((EditText)(editorActivity.findViewById(R.id.edutText))).setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(((EditText)(editorActivity.findViewById(R.id.edutText))), InputMethodManager.SHOW_FORCED);
                        PointF pf = SurfaceViewHolder.getInstance().getMySurfaceView().getCenter();
                        mySurfaceView.addImageElement(new TextItem(SurfaceViewHolder.getInstance().getMySurfaceView(),"Новый текст", (int)pf.x, (int)pf.y));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        mySurfaceView.draw();
                        editorActivity.showColors();
                        editorActivity.showRedactorItemHeader();
                        break;
                    }
                    case EFFECT_BUTTON: {
                        editorActivity.showFragment(HorizontalImagesScrallFragment.class, R.id.header_pos);
                        break;
                    }
                    case 3: {
                        editorActivity.showStickers();
                        break;
                    }
                    case 4: {
                        ImageHolder.getInstance().setKropedBitmap(null);
                        mySurfaceView.imageEditorQueue.clear();
                        mySurfaceView.draw();
                        break;
                    }
                    case 5: {
                        CurrentElementHolder.getInstance().removeCurrentElement();
                        mySurfaceView.addImageElement(
                                new RisunocItem(SurfaceViewHolder.getInstance().getMySurfaceView()
                                        , 0
                                        , 0));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        mySurfaceView.draw();
                        editorActivity.showColors();
                        editorActivity.showFragment(PanelRisunoc.class, R.id.header_pos);
                        break;
                    }
                    case 6: {

                        try {

                            Rest.getInstance()
                                    .sendRequest(ImageHolder
                                            .getInstance()
                                            .getDefaultBitmap());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


}


