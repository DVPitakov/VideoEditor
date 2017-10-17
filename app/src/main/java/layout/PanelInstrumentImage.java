package layout;

import android.content.Context;
import android.util.Log;

import com.example.dmitry.videoeditor.Adapters.ImageWithTextAdapter;
import com.example.dmitry.videoeditor.Models.IconWithText;
import com.example.dmitry.videoeditor.R;

import java.util.ArrayList;

/**
 * Created by alexandr on 10.09.17.
 */

public class PanelInstrumentImage extends ImageAdapter{
    public PanelInstrumentImage(Context c) {
        super(c);
        arrayList = new ArrayList<IconWithText>();
        arrayList.add(new IconWithText(R.drawable.ic_crop_white_24dp, "Кроп"));
        arrayList.add(new IconWithText(R.drawable.ic_text_fields_white_24dp, "Текст"));
        arrayList.add(new IconWithText(R.drawable.ic_photo_filter_white_24dp, "Эффект"));
        arrayList.add(new IconWithText(R.drawable.ic_image_white_24dp, "Стикер"));
        arrayList.add(new IconWithText(R.drawable.ic_save_white_24dp, "Готово"));
        arrayList.add(new IconWithText(R.drawable.ic_cancel_black_24dp, "Отмена"));
    }
}
