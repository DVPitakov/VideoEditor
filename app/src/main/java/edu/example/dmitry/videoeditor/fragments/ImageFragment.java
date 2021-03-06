package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import edu.example.dmitry.videoeditor.holders.CurrentVideoHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.views.MySurfaceView;
import edu.example.dmitry.videoeditor.items.RisunocItem;
import edu.example.dmitry.videoeditor.items.TextItem;
import edu.example.dmitry.videoeditor.Tools;


public class ImageFragment extends Fragment {
    private EditText editText;
    private FrameLayout surfaceViewPos;
    private OnFragmentInteractionListener mListener;
    private ImageButton sendButton;
    private View rootView;

    ElementRedactorFragment elementRedactorHeader;

    MySurfaceView mySurfaceView;

    public MySurfaceView updateSurface() {
        surfaceViewPos = (FrameLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.surface_view_pos));
        surfaceViewPos.removeAllViews();
        mySurfaceView = new MySurfaceView(surfaceViewPos.getContext());
        SurfaceViewHolder.getInstance().setMySurfaceView(mySurfaceView);
        mySurfaceView.setImageParceErrorListener(new MySurfaceView.ImageParceErrorListener() {
            @Override
            public void onImageEpsent() {
                getActivity().showDialog(777);
                // getActivity().onBackPressed();
            }
        });
        return mySurfaceView;
    }
    public ImageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_image, container, false);
        ImageHolder.getInstance().tryInit(getActivity());


        sendButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_image_send_button));
        sendButton.setOnClickListener(new View.OnClickListener() {
            long lastCallTime = 0;
            @Override
            public void onClick(View view) {
                long callTime = System.currentTimeMillis();
                if (callTime - lastCallTime > 1500) {
                    Bitmap bitmap = ImageHolder.getInstance().getBitmapWithElements();
                    Tools.saveAndSendImage(bitmap, getActivity());
                }
                lastCallTime = callTime;
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
        surfaceViewPos.removeAllViews();
        mySurfaceView = new MySurfaceView(surfaceViewPos.getContext());
        SurfaceViewHolder.getInstance().setMySurfaceView(mySurfaceView);
        mySurfaceView.setImageParceErrorListener(new MySurfaceView.ImageParceErrorListener() {
            @Override
            public void onImageEpsent() {
                getActivity().showDialog(777);
               // getActivity().onBackPressed();
            }
        });

        surfaceViewPos.addView(mySurfaceView);
        editText = ((EditorActivity)(getActivity())).editText;
        mySurfaceView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                        , FrameLayout.LayoutParams.MATCH_PARENT));
        mySurfaceView.setFocusListener(
                new MySurfaceView.FocusListener() {
            @Override
            public void focusLosed(boolean setDefaultMenu) {
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                if (setDefaultMenu) {
                    ((EditorActivity) (getActivity())).showDefaultImageHeader();
                }
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

        handler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(mySurfaceView != null) {
                        mySurfaceView.draw();
                    }
                }
                catch (Exception e) {
                }

                handler.postDelayed(this, 500);
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        SurfaceViewHolder.getInstance().setMySurfaceView(mySurfaceView);
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
