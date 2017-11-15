package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import edu.example.dmitry.videoeditor.EditorActivity;
import edu.example.dmitry.videoeditor.holders.CurrentElementHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.MySurfaceView;
import edu.example.dmitry.videoeditor.items.RisunocItem;
import edu.example.dmitry.videoeditor.items.TextItem;
import edu.example.dmitry.videoeditor.Tools;

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
    private ImageButton sendButton;

    ElementRedactorFragment elementRedactorHeader;

    MySurfaceView mySurfaceView;

    public ImageFragment() {}


    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_image, container, false);
        ImageHolder.getInstance().tryInit(getActivity());


        sendButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_image_send_button));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap =  ImageHolder.getInstance().getBitmapWithElements();
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "image" , null);
                Tools.saveAndSendImage(bitmap, getActivity());
            }
        });
        rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_image_ok_button).setVisibility(View.GONE);
        rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_image_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditorActivity)(getActivity())).showDefaultImageHeader();
                getActivity().findViewById(edu.example.dmitry.videoeditor.R.id.fragment_image_ok_button).setVisibility(View.GONE);
            }
        });
        surfaceViewPos = (FrameLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.surface_view_pos));
        mySurfaceView = new MySurfaceView(surfaceViewPos.getContext());
        surfaceViewPos.addView(mySurfaceView);
        editText = ((EditorActivity)(getActivity())).editText;
        mySurfaceView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                        , FrameLayout.LayoutParams.MATCH_PARENT));
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
                if (CurrentElementHolder.getInstance().getCurrentElement() instanceof TextItem) {
                    ((EditorActivity)(getActivity())).showColors();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                    ((EditorActivity) (getActivity())).showRedactorItemHeader();
                }
                else if (CurrentElementHolder.getInstance().getCurrentElement() instanceof RisunocItem) {
                    ((EditorActivity) (getActivity())).showFragment(PanelRisunoc.class, edu.example.dmitry.videoeditor.R.id.header_pos);
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
