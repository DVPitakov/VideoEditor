package layout;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.dmitry.videoeditor.EditorActivity;
import com.example.dmitry.videoeditor.Holders.CurrentElementHolder;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.ImageEditor;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.MySurfaceView;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Tools;
import com.example.dmitry.videoeditor.Vidgets.RisunocImage;
import com.example.dmitry.videoeditor.Vidgets.TextImage;

import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */

public class PanelRisunoc extends ImageAdapter{
    public PanelRisunoc() {
        super();
        arrayList = new ArrayList<IconWithText>();
        arrayList.add(new IconWithText(R.drawable.ic_check_white_24dp, "Принять"));
        arrayList.add(new IconWithText(R.drawable.ic_paint_size_white_24dp, "Размер"));
        arrayList.add(new IconWithText(R.drawable.ic_delete_white_24dp, "Удалить"));

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean b = false;
            int i = 0;
            int j = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case 0: {
                        if (CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocImage) {
                            ((RisunocImage)CurrentElementHolder.getInstance().getCurrentElement()).setReady(true);
                            CurrentElementHolder.getInstance().removeCurrentElement();
                        }
                        ((EditorActivity) (getActivity())).removeFragment(PanelColors.class);
                        ((EditorActivity) (getActivity())).showDefaultImageHeader();
                        break;
                    }
                    case 1: {
                        SurfaceViewHolder.getInstance().getMySurfaceView().setImageSize(j);
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                        j = (j + 1) % 5;
                        break;
                    }
                    case 2: {
                        ((RisunocImage) CurrentElementHolder.getInstance().getCurrentElement()).setReady(true);
                        mySurfaceView.deleteCurrentItem();
                        CurrentElementHolder.getInstance().removeCurrentElement();
                        ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);
                        ((EditorActivity)(getActivity())).showDefaultImageHeader();
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                        break;
                    }

                }
            }
        });
    }


}
