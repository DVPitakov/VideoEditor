package edu.example.dmitry.videoeditor.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.example.dmitry.videoeditor.Holders.ImageHolder;
import edu.example.dmitry.videoeditor.Holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.ImageEditor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HorizontalImagesScrallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HorizontalImagesScrallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorizontalImagesScrallFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener mListener;

    public HorizontalImagesScrallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HorizontalImagesScrallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HorizontalImagesScrallFragment newInstance(String param1, String param2) {
        HorizontalImagesScrallFragment fragment = new HorizontalImagesScrallFragment();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_horizontal_images_scrall, container, false);
        LinearLayout itemsContainer =
                ( LinearLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_horizontal_images_scrall_list));
        Bitmap smappImg = ImageHolder.getInstance().getSmallImage(getActivity());
        Bitmap[] bms = {
                smappImg,
                ImageEditor.bombit(smappImg),
                ImageEditor.bombit2(smappImg),
                ImageEditor.inversion(smappImg),
                ImageEditor.whiteBlack(smappImg),
                ImageEditor.lessRed(smappImg),
                ImageEditor.lessGreen(smappImg),
                ImageEditor.lessBlue(smappImg)
        };

        String[] sts = {
                "Оригинал",
                "Свет1",
                "Свет2",
                "Инверсия",
                "Черно\nбелый",
                "Без\nкрасного",
                "Без\nзеленого",
                "Без\nсинего"
        };
        getActivity().findViewById(edu.example.dmitry.videoeditor.R.id.fragment_image_ok_button).setVisibility(View.VISIBLE);
        for(int i = 0; i <  bms.length; i++) {
            View view = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.item_image_with_text_fix_width, null);
            ((ImageView)(view.findViewById(edu.example.dmitry.videoeditor.R.id.image_with_text_icon))).setImageBitmap(bms[i]);
            ((TextView)(view.findViewById(edu.example.dmitry.videoeditor.R.id.image_with_text_text))).setText(sts[i]);
            view.setOnClickListener(this);
            view.setTag(i);
            itemsContainer.addView(view);
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onClick(View view) {
        ImageHolder.getInstance().setFreshBitmap(null);
        SurfaceViewHolder.getInstance().getMySurfaceView().setEffect((Integer)view.getTag());
        SurfaceViewHolder.getInstance().getMySurfaceView().draw();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
