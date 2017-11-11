package edu.example.dmitry.videoeditor.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import edu.example.dmitry.videoeditor.Holders.CurrentVideoHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConvertingProgressFragment.OnConvertingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConvertingProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConvertingProgressFragment extends DialogFragment implements CurrentVideoHolder.UpdatedVideoLenChangedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;

    private OnConvertingFragmentInteractionListener mListener;

    public ConvertingProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConvertingProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConvertingProgressFragment newInstance(String param1, String param2) {
        ConvertingProgressFragment fragment = new ConvertingProgressFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
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
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_converting_progress, null);
        progressBar = (ProgressBar) (rootView.findViewById(edu.example.dmitry.videoeditor.R.id.converting_progress_bar));

        builder.setView(rootView);
        // Add action buttons
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        CurrentVideoHolder.getInstance().removeListener();
        mListener = null;
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
