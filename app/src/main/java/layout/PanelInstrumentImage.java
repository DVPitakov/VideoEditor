package layout;

import android.content.Context;
import android.widget.GridView;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

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
                R.drawable.item_scale
        };
        gridView.setAlpha(0.5f);
    }

}
