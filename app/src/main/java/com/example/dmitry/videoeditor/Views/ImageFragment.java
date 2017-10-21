package com.example.dmitry.videoeditor.Views;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.dmitry.videoeditor.EditorActivity;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.Vidgets.IconImage;
import com.example.dmitry.videoeditor.ImageEditor;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.MySurfaceView;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Vidgets.RisunocImage;
import com.example.dmitry.videoeditor.Vidgets.TextImage;
import com.example.dmitry.videoeditor.Tools;

import layout.PanelColors;
import layout.PanelInstrumentImage;
import layout.PanelRisunoc;
import layout.PanelStckers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ImageFragment extends Fragment {
    private EditText editText;
    private FrameLayout surfaceViewPos;
    private OnFragmentInteractionListener mListener;

    ElementRedactorFragment elementRedactorHeader;

    MySurfaceView mySurfaceView;


    void showDefaultHeader() {

    }

    void showTextRedactorHeader() {

    }

    void showImageRedactorHeader() {

    }

    public ImageFragment() {}


    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        ImageHolder.getInstance().tryInit(getActivity());

        surfaceViewPos = (FrameLayout)(rootView.findViewById(R.id.surface_view_pos));
        mySurfaceView = new MySurfaceView(surfaceViewPos.getContext());
        surfaceViewPos.addView(mySurfaceView);
        editText = ((EditorActivity)(getActivity())).editText;
        final float imageSize = getResources().getDimension(R.dimen.mySurfaceViewSize);
        mySurfaceView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)imageSize));
        mySurfaceView.setFocusListener(
                new MySurfaceView.FocusListener() {
            @Override
            public void focusLosed() {
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                ((EditorActivity)(getActivity())).showDefaultImageHeader();
                ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);

            }

            @Override
            public void focusTaken() {

            }

            @Override
            public void doubleClick() {
                if (SurfaceViewHolder.getInstance().getMySurfaceView().selectedImageElement instanceof TextImage) {
                    ((EditorActivity)(getActivity())).showColors();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                    ((EditorActivity) (getActivity())).showRedactorItemHeader();
                }
                else if (SurfaceViewHolder.getInstance().getMySurfaceView().selectedImageElement instanceof RisunocImage) {
                    ((EditorActivity) (getActivity())).showFragment(PanelRisunoc.class, R.id.header_pos);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
