package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import edu.example.dmitry.videoeditor.DecodeVideo;
import edu.example.dmitry.videoeditor.holders.CurrentVideoHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.holders.UrlHolder;
import edu.example.dmitry.videoeditor.R;
import edu.example.dmitry.videoeditor.SettingsVideo;
import edu.example.dmitry.videoeditor.views.VideoCropView;


public class VideoPlayerFragment extends Fragment implements VideoCropView.VideoCropViewListener {
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
    private boolean isPlaying = false;
    private boolean isUpdatable = true;
    ImageButton sendButton;
    ImageButton playButton;
    ImageButton convertButton;
    ImageButton crosButton;
    LinearLayout buttonsLayout;
    LinearLayout mainLinearLayout;
    Button assertButton;
    Button cancelButton;
    VideoCropView vc;

    private OnVideoPlayerFragmentInteractionListener mListener;

    public VideoPlayerFragment() {}


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
        rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_video_player, container, false);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        Bitmap bitmap = null;
        long videolen = 0;
        try {
            retriever.setDataSource(getContext(), UrlHolder._getInputUri());
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            videolen = 1000;
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        videolen = 20000;
        Log.d("GHGH", "bitmap" + bitmap);
        vc = new VideoCropView(rootView.getContext(), 0, 100);
        vc.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT));
        vc.setVideoCropViewListener(this);

        ((FrameLayout)rootView.findViewById(R.id.test_pos)).addView(vc);

        float counter = (int)vc.calculateShownVideoMoments(new BitmapDrawable(getResources(), bitmap),
                getActivity().getWindowManager().getDefaultDisplay().getWidth(),
                (int)getResources().getDimension(R.dimen.video_crop_view_height));

        float step =  1.0f * videolen / counter;
        for (int i = 0; i - 1 < counter; i++) {
            MyTask myTask = new MyTask();
            myTask.execute((float)i, step);
        }

        mainLinearLayout = (LinearLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_main_linear_layout));
        assertButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_assert_button));
        cancelButton = (Button)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_cancel_button));
        buttonsLayout = (LinearLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_video_player_buttons));
        playButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.playButton));
        convertButton = (ImageButton)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.show_convert_menu));
        sendButton = (ImageButton)(rootView.findViewById(R.id.send_button));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConvertingProgressFragment().show(getActivity().getSupportFragmentManager()
                        , ConvertingProgressFragment.class.getName());
                Log.d("2010", "leftCur" + leftCur);
                Log.d("2010", "rightCur" + rightCur);
                new DecodeVideo(getActivity(), leftCur,rightCur
                        , CurrentVideoHolder.getInstance().getCompressType());

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = !isPlaying;
                if(isPlaying) {
                    playButton.setImageDrawable(rootView.getResources().getDrawable(edu.example.dmitry.videoeditor.R.drawable.ic_pause_circle_filled_black_24dp));
                }
                else {
                    playButton.setImageDrawable(rootView.getResources().getDrawable(edu.example.dmitry.videoeditor.R.drawable.ic_play_circle_outline_black_24dp));
                }
                if (mListener != null) {
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsLayout.setVisibility(View.GONE);
                mainLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        assertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


    public void updateProgess(float cur) {
        if (isUpdatable) {
            vc.setProgress(cur);
        }

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

    @Override
    public void onPolzunocMove(float newPos) {
        isUpdatable = false;
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        videoEnd();
    }

    @Override
    public void onLeftCurMove(float newPos) {
        isUpdatable = false;
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        videoEnd();
    }

    @Override
    public void onRightCurMove(float newPos) {
        isUpdatable = true;
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        videoEnd();
    }

    @Override
    public void onPolzunocMoveEnd(float newPos) {
        isUpdatable = true;
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        SurfaceViewHolder.getInstance().getMySurfaceView().continueVideo(newPos);
    }

    public float leftCur = 0;
    public float rightCur = 0;
    @Override
    public void onLeftCurMoveEnd(float newPos) {
        isUpdatable = true;
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        SurfaceViewHolder.getInstance().getMySurfaceView().continueVideo(newPos);
        leftCur = newPos / 100 * CurrentVideoHolder.getInstance().getVideoLen();
    }

    @Override
    public void onRightCurMoveEnd(float newPos) {
        isUpdatable = true;
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        SurfaceViewHolder.getInstance().getMySurfaceView().continueVideo(newPos);
        videoEnd();
        rightCur = newPos / 100 * CurrentVideoHolder.getInstance().getVideoLen();
    }

    @Override
    public void leftOverflow(float newPos) {
        SurfaceViewHolder.getInstance().getMySurfaceView().continueVideo(newPos);
    }

    @Override
    public void rightOverflow(float newPos) {
        SurfaceViewHolder.getInstance().getMySurfaceView().pauseVideo();
        videoEnd();
    }


    public interface OnVideoPlayerFragmentInteractionListener {
        // TODO: Update argument type and name
        void onVideoPlayerFragmentInteraction(boolean bul);
        void ready(Object object);
        void show_compression_menu_request();
        void doVideoKrop(int leftCur, int rightCur, int leftMax, int curMax);
    }

    private class BitmapAndInteger {
        BitmapAndInteger(Bitmap bitmap, Integer integer) {
            this.bitmap = bitmap;
            this.integer = integer;
        }
        public Bitmap bitmap;
        public Integer integer;
    }
    private class MyTask extends AsyncTask<Float, BitmapAndInteger, BitmapAndInteger> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BitmapAndInteger doInBackground(Float... v) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Bitmap bitmap = null;
            try {
                retriever.setDataSource(getContext(), SettingsVideo.getInput());
                bitmap = retriever.getFrameAtTime((int)(1000 * v[1] * v[0]));
                Log.d("2317", "1000v0 " + (int)(v[1] * v[0]));
                Log.d("2317", "v1" + (v[1]));
                Log.d("2317", "v0" + (v[0]));
            } catch (IllegalArgumentException e) {
            }
            retriever.release();
            Log.d("BITMAAP:", "bitmaap:" + bitmap);
            return new BitmapAndInteger(bitmap, (v[0]).intValue());
        }

        @Override
        protected void onPostExecute(BitmapAndInteger result) {
            super.onPostExecute(result);
            vc.addVideoMoment(result.integer, result.bitmap);
        }
    }
}
