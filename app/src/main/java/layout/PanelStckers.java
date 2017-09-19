package layout;

import android.content.Context;

import com.example.dmitry.videoeditor.R;

/**
 * Created by dmitry on 17.09.17.
 */

public class PanelStckers extends ImageAdapter {
    public static final Integer[] ITEMS =  new Integer[] {
            R.drawable.s1,
            R.drawable.s2,
            R.drawable.s3,
            R.drawable.s4,
            R.drawable.s5,
            R.drawable.s6,
            R.drawable.s7,
            R.drawable.s8,
            R.drawable.s9,
            R.drawable.s10,
            R.drawable.s11,
            R.drawable.s12,
            R.drawable.s13,
            R.drawable.s14,
            R.drawable.s15,
            R.drawable.s16,
//            R.drawable.s17,
//            R.drawable.s18,
//            R.drawable.s19,
//            R.drawable.s20,
//            R.drawable.s21,
//            R.drawable.s22,
//            R.drawable.s23,
    };
    public PanelStckers(Context c) {
        super(c);
        layout = R.layout.fragment_sticker;
        id = R.id.stickerImage;
        mThumbIds = ITEMS;
        itemSize = R.dimen.stickerOnFragmentSize;

        mContacts = mThumbIds.clone();
    }
}
