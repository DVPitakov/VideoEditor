package edu.example.dmitry.videoeditor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class CompressionModeFragment extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String WITHOUT_COMPRESS = "CompressionModeFragment.without_compress";
    public static final String FAST_COMPRESS = "CompressionModeFragment.fast_compress";
    public static final String QUALITY_COMPRESS = "CompressionModeFragment.quality_compress";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button normButton;
    private Button fastButton;
    private Button qualityButton;

    private OnCompressionModeFragmentInteractionListener mListener;

    public CompressionModeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompressionModeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompressionModeFragment newInstance(String param1, String param2) {
        CompressionModeFragment fragment = new CompressionModeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/


/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compression_mode, container, false);
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_compression_mode, null);
        normButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.without_compress_button));
        fastButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fast_compress_button));
        qualityButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.quality_compress_button));
        normButton.setOnClickListener(this);
        fastButton.setOnClickListener(this);
        qualityButton.setOnClickListener(this);
        builder.setView(rootView);
                // Add action buttons
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompressionModeFragmentInteractionListener) {
            mListener = (OnCompressionModeFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case edu.example.dmitry.videoeditor.R.id.fast_compress_button: {
                    mListener.onCompressionModeFragmentInteraction(FAST_COMPRESS);
                    break;
                }
                case edu.example.dmitry.videoeditor.R.id.quality_compress_button: {
                    mListener.onCompressionModeFragmentInteraction(QUALITY_COMPRESS);
                    break;
                }
                case edu.example.dmitry.videoeditor.R.id.without_compress_button: {
                    Log.d("1014", String.valueOf(mListener));
                    mListener.onCompressionModeFragmentInteraction(WITHOUT_COMPRESS);
                    break;
                }
            }
        }
    }


    public void setOnCompressionModeFragmentInteractionListener(OnCompressionModeFragmentInteractionListener listener) {
        mListener = listener;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCompressionModeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCompressionModeFragmentInteraction(String buttonType);
    }
}
