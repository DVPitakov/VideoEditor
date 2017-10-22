package com.example.dmitry.videoeditor.Views;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitry.videoeditor.EditorActivity;
import com.example.dmitry.videoeditor.Holders.CurrentElementHolder;
import com.example.dmitry.videoeditor.Holders.ImageHolder;
import com.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Vidgets.ImageElement;
import com.example.dmitry.videoeditor.Vidgets.TextImage;

import layout.PanelColors;

public class ElementRedactorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton cancelButton;
    private ImageButton saveButton;
    private ImageButton fontButton;
    private ImageButton italicButton;
    private ImageButton boldButton;
    private ImageButton colorButton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRedactorFragmentInteractionListener mListener;

    public ElementRedactorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ElementRedactorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElementRedactorFragment newInstance(String param1, String param2) {
        ElementRedactorFragment fragment = new ElementRedactorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_element_redactor, container, false);
        cancelButton = (ImageButton) rootView.findViewById(R.id.fragment_element_redactor_cancel_button);
        saveButton = (ImageButton) rootView.findViewById(R.id.fragment_element_redactor_ok_button);
        fontButton = (ImageButton) rootView.findViewById(R.id.fragment_element_redactor_font_button);
        boldButton = (ImageButton) rootView.findViewById(R.id.fragment_element_redactor_bold_button);
        italicButton = (ImageButton) rootView.findViewById(R.id.fragment_element_redactor_italic_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);
                ((EditorActivity)(getActivity())).showDefaultImageHeader();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SurfaceViewHolder.getInstance().getMySurfaceView().deleteCurrentItem();
                ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);
                ((EditorActivity)(getActivity())).showDefaultImageHeader();
                ImageHolder.getInstance().setBitmapWithElements(null);
                SurfaceViewHolder.getInstance().getMySurfaceView().draw();
            }
        });


        fontButton.setOnClickListener(new View.OnClickListener() {
            int curFont = 0;
            @Override
            public void onClick(View view) {
                ImageElement imageElement = CurrentElementHolder.getInstance().getCurrentElement();
                if (CurrentElementHolder.getInstance().getCurrentElement().getClass() == TextImage.class) {
                    ((TextImage)imageElement).setFont(curFont);
                    ImageHolder.getInstance().setBitmapWithElements(null);
                    SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                }
                curFont = (curFont + 1) % 3;
            }
        });

        italicButton.setOnClickListener(new View.OnClickListener() {
            boolean italic = true;
            @Override
            public void onClick(View view) {
                ImageElement imageElement = CurrentElementHolder.getInstance().getCurrentElement();
                if (CurrentElementHolder.getInstance().getCurrentElement().getClass() == TextImage.class) {
                    ((TextImage)imageElement).setItalic(italic);
                    italic = !italic;
                    ImageHolder.getInstance().setBitmapWithElements(null);
                    SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                }
            }
        });

        boldButton.setOnClickListener(new View.OnClickListener() {
            boolean bold = true;
            @Override
            public void onClick(View view) {
                ImageElement imageElement = CurrentElementHolder.getInstance().getCurrentElement();
                if (CurrentElementHolder.getInstance().getCurrentElement().getClass() == TextImage.class) {
                    ((TextImage)imageElement).setBold(bold);
                    bold = !bold;
                    ImageHolder.getInstance().setBitmapWithElements(null);
                    SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                }
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRedactorFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRedactorFragmentInteractionListener) {
            mListener = (OnRedactorFragmentInteractionListener) context;
        } else {
 //           throw new RuntimeException(context.toString()
 //                   + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void setOnFragmentInteractionListener(OnRedactorFragmentInteractionListener listener) {
        mListener = listener;
    }

    public interface OnRedactorFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRedactorFragmentInteraction(Uri uri);
    }
}
