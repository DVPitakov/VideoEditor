package layout;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dmitry.videoeditor.Adapters.ImageWithTextAdapter;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.ImageEditor;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.MySurfaceView;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Tools;
import com.example.dmitry.videoeditor.Vidgets.RisunocImage;
import com.example.dmitry.videoeditor.Vidgets.TextImage;
import com.example.dmitry.videoeditor.Views.ElementRedactorFragment;

import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */

public class PanelInstrumentImage extends ImageAdapter{
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

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySurfaceView mySurfaceView = SurfaceViewHolder.getInstance().getMySurfaceView();
                switch (i) {
                    case KROP_BUTTON: {
                        if (b) {
                            mySurfaceView.kropUnset();
                            Rect rect = mySurfaceView.getKropRect();
                            Log.d("1806", "!!!" +rect.toString());
                            Bitmap kropedBitmap = ImageHolder.getInstance().getKropedBitmap();
                            if (kropedBitmap == null) {
                                kropedBitmap = ImageHolder.getInstance().getDefaultBitmap();
                            }
                            kropedBitmap = ImageEditor.krop(kropedBitmap,
                                    rect.left, rect.top, rect.right, rect.bottom);
                            ImageHolder.getInstance().setKropedBitmap(kropedBitmap);
                            mySurfaceView.kropClear();
                            mySurfaceView.draw();
                        }
                        else {
                            mySurfaceView.kropSet();
                        }
                        b = !b;
                        break;
                    }
                    case TEXT_BUTTON: {
                        mySurfaceView.selectedImageElement = null;
                        ((EditText)(editorActivity.findViewById(R.id.edutText))).setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(((EditText)(editorActivity.findViewById(R.id.edutText))), InputMethodManager.SHOW_FORCED);
                        PointF pf = SurfaceViewHolder.getInstance().getMySurfaceView().getCenter();
                        mySurfaceView.addImageElement(new TextImage("Новый текст", (int)pf.x, (int)pf.y));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        mySurfaceView.draw();
                        editorActivity.showColors();
                        editorActivity.showRedactorItemHeader();
                        break;
                    }
                    case EFFECT_BUTTON: {
                        this.i = (this.i + 1) % 4;
                        ImageHolder.getInstance().setFreshBitmap(null);
                        mySurfaceView.setEffect(this.i);
                        mySurfaceView.draw();
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
                        mySurfaceView.selectedImageElement = null;
                        mySurfaceView.addImageElement(
                                new RisunocImage(SurfaceViewHolder.getInstance().getMySurfaceView()
                                        , 0
                                        , 0));
                        ImageHolder.getInstance().setBitmapWithElements(null);
                        mySurfaceView.draw();
                        editorActivity.showColors();
                        editorActivity.showFragment(PanelRisunoc.class, R.id.header_pos);
                        break;
                    }
                }
            }
        });
    }


}
