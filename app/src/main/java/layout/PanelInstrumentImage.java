package layout;

import android.content.Context;
import android.util.Log;

import com.example.dmitry.videoeditor.R;

/**
 * Created by alexandr on 10.09.17.
 */

public class PanelInstrumentImage extends ImageAdapter{
    public PanelInstrumentImage(Context c) {
        super(c);
        mThumbIds = new Integer[]{
                R.drawable.item_crop,
                R.drawable.item_text_fild,
                R.drawable.item_negative,
                R.drawable.item_scale,
                R.drawable.item_save,
                R.drawable.item_back
        };
        mContacts = mThumbIds.clone();
    }

}
