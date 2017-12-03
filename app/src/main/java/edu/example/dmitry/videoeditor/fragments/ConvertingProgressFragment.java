package edu.example.dmitry.videoeditor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import edu.example.dmitry.videoeditor.holders.CurrentVideoHolder;

public class ConvertingProgressFragment extends DialogFragment implements CurrentVideoHolder.UpdatedVideoLenChangedListener {

    private ProgressBar progressBar;

    private OnConvertingFragmentInteractionListener mListener;

    public ConvertingProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//
        }
    }
/*
    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_converting_progress, container, false);
    }
*/
    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConvertingFragmentInteractionListener) {
            mListener = (OnConvertingFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void setProgess() {
        if(progressBar != null) {
            progressBar.setMax((int)CurrentVideoHolder.getInstance().getVideoLen());
            progressBar.setProgress((int)CurrentVideoHolder.getInstance().getUpdatedVideoLen());
            if(CurrentVideoHolder.getInstance().getVideoLen()
                    == CurrentVideoHolder.getInstance().getUpdatedVideoLen()) {
                ConvertingProgressFragment.this.dismiss();
                CurrentVideoHolder.getInstance().showNewVideo();
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CurrentVideoHolder.getInstance().setListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_converting_progress, null);
        progressBar = (ProgressBar) (rootView.findViewById(edu.example.dmitry.videoeditor.R.id.converting_progress_bar));

        builder.setView(rootView);
        setCancelable(false);
        // Add action buttons
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        CurrentVideoHolder.getInstance().removeListener();
        mListener = null;
    }


    public void setOnConvertingFragmentInteractionListener(OnConvertingFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void updatedVideoLenChanged(long updatedVideoLen) {
        setProgess();
    }

    public interface OnConvertingFragmentInteractionListener {
        // TODO: Update argument type and name
        void onConvertingFragmentInteraction(Uri uri);
    }
}
