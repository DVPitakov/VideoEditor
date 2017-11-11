package edu.example.dmitry.videoeditor.Views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;


import org.florescu.android.rangeseekbar.RangeSeekBar;


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
    RangeSeekBar rangebar;
    LinearLayout buttonsLayout;
    LinearLayout mainLinearLayout;
    Button assertButton;
    Button cancelButton;
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
        rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_video_player, container, false);
        mainLinearLayout = (LinearLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_main_linear_layout));
        assertButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_assert_button));
        cancelButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_cancel_button));
        buttonsLayout = (LinearLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_buttons));
        playButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.playButton));
        convertButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.show_convert_menu));
        seekBar = (SeekBar)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.videoProgress));
        rangebar = (RangeSeekBar)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.rangebar1));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("1996", "I am here 0849");
                isPlaying = !isPlaying;
                if(isPlaying) {
                    playButton.setImageDrawable(rootView.getResources().getDrawable(edu.example.dmitry.videoeditor.R.drawable.ic_pause_circle_filled_black_24dp));
                }
                else {
                    playButton.setImageDrawable(rootView.getResources().getDrawable(edu.example.dmitry.videoeditor.R.drawable.ic_play_circle_outline_black_24dp));
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
        crosButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.crosButton));
        crosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    int ll = rangebar.getSelectedMinValue().intValue();
                    int rr = rangebar.getSelectedMaxValue().intValue();
                    int left = rangebar.getAbsoluteMinValue().intValue();
                    int right = rangebar.getAbsoluteMaxValue().intValue();
                    mListener.doVideoKrop(ll, rr, left, right);
                }
            }
        });
        showKroper = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.show_kroper));
        showKroper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangebar.getVisibility() == View.GONE) {
                    rangebar.setVisibility(View.VISIBLE);
                    buttonsLayout.setVisibility(View.VISIBLE);
                    mainLinearLayout.setVisibility(View.GONE);
                }
                else {
                    rangebar.setVisibility(View.GONE);
                    buttonsLayout.setVisibility(View.GONE);
                    mainLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rangebar.setVisibility(View.GONE);
                buttonsLayout.setVisibility(View.GONE);
                mainLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        assertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rangebar.setVisibility(View.GONE);
                buttonsLayout.setVisibility(View.GONE);
                mainLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        redyForUpdating = true;
        requestReady(this);
        return rootView;
    }

    public void videoEnd() {
        isPlaying = false;
        playButton.setImageDrawable(rootView.getResources().getDrawable(edu.example.dmitry.videoeditor.R.drawable.ic_play_circle_outline_black_24dp));
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
