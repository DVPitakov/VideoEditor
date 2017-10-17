package com.example.dmitry.videoeditor.Views;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.edmodo.rangebar.RangeBar;
import com.example.dmitry.videoeditor.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoPlayerFragment.OnVideoPlayerFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int max = 1;
    private int cur = 0;
    private boolean redyForUpdating = false;
    private View rootView;
    private SeekBar seekBar;
    private boolean isPlaying = false;
    ImageButton playButton;
    ImageButton convertButton;
    ImageButton crosButton;
    ImageButton showKroper;
    RangeBar rangebar;
    private OnVideoPlayerFragmentInteractionListener mListener;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoPlayerFragment newInstance(String param1, String param2) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        playButton = (ImageButton)(rootView.findViewById(R.id.playButton));
        convertButton = (ImageButton)(rootView.findViewById(R.id.show_convert_menu));
        seekBar = (SeekBar)(rootView.findViewById(R.id.videoProgress));
        rangebar = (RangeBar)(rootView.findViewById(R.id.rangebar1));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("1996", "I am here 0849");
                isPlaying = !isPlaying;
                if(isPlaying) {
                    playButton.setImageDrawable(rootView.getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
                else {
                    playButton.setImageDrawable(rootView.getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
                }
                if (mListener != null) {
                    Log.d("1996", "I am here 1524");
                    mListener.onVideoPlayerFragmentInteraction(isPlaying);
                }
            }
        });
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.show_compression_menu_request();
                }
            }
        });
        crosButton = (ImageButton)(rootView.findViewById(R.id.crosButton));
        crosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    int ll = rangebar.getLeftIndex();
                    int rr = rangebar.getRightIndex();
                    int left = rangebar.getLeft();
                    int right = rangebar.getRight();
                    mListener.doVideoKrop(left, right, ll, rr);
                }
            }
        });
        showKroper = (ImageButton)(rootView.findViewById(R.id.show_kroper));
        showKroper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangebar.getVisibility() == View.GONE) {
                    rangebar.setVisibility(View.VISIBLE);
                }
                else {
                    rangebar.setVisibility(View.GONE);
                }
            }
        });
        redyForUpdating = true;
        requestReady(this);
        return rootView;
    }

    public void videoEnd() {
        isPlaying = false;
        playButton.setImageDrawable(rootView.getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
    }


    public void updateProgess(int cur, int max) {
        seekBar.setMax(max);
        seekBar.setProgress(cur);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVideoPlayerFragmentInteractionListener) {
            Log.d("1999", "I am 1521");
            mListener = (OnVideoPlayerFragmentInteractionListener) context;
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

    public void requestReady(Object object) {
        if (redyForUpdating && (mListener != null)) {
            mListener.ready(object);
        }
    }

    public void setOnVideoPlayerFragmentInteractionListener(OnVideoPlayerFragmentInteractionListener listener) {
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
    public interface OnVideoPlayerFragmentInteractionListener {
        // TODO: Update argument type and name
        void onVideoPlayerFragmentInteraction(boolean bul);
        void ready(Object object);
        void show_compression_menu_request();
        void doVideoKrop(int leftCur, int rightCur, int leftMax, int curMax);
    }
}
