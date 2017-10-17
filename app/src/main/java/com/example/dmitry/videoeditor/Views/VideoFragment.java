package com.example.dmitry.videoeditor.Views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.dmitry.videoeditor.DecodeVideo;
import com.example.dmitry.videoeditor.Decoder;
import com.example.dmitry.videoeditor.ImageHolder;
import com.example.dmitry.videoeditor.MySurfaceView;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.Tools;
import com.example.dmitry.videoeditor.UrlHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment
        implements VideoPlayerFragment.OnVideoPlayerFragmentInteractionListener
        , CompressionModeFragment.OnCompressionModeFragmentInteractionListener
        , ConvertingProgressFragment.OnConvertingFragmentInteractionListener {



    private MySurfaceView mySurfaceView;
    private VideoPlayerFragment videoPlayerFragment;
    private CompressionModeFragment compressionModeFragment;
    private Decoder decoder;
    private FrameLayout surfaceViewPos;
    private ImageHolder imageHolder;
    private ConvertingProgressFragment convertingProgressFragment;
    private Handler handler = new Handler();

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle args = new Bundle();
        videoFragment.setArguments(args);
        return videoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        decoder = new Decoder();
        imageHolder = ImageHolder.getInstance();
        imageHolder.tryInit(getActivity());

        final float imageSize = getResources().getDimension(R.dimen.mySurfaceViewSize);
        surfaceViewPos = (FrameLayout)(rootView.findViewById(R.id.video_surface_view_pos));
        mySurfaceView = new MySurfaceView(getActivity().getBaseContext(), null);
        surfaceViewPos.addView(mySurfaceView);
        mySurfaceView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)imageSize));

        videoPlayerFragment = new VideoPlayerFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.video_footer_pos, videoPlayerFragment)
                .commit();
        videoPlayerFragment.setOnVideoPlayerFragmentInteractionListener(this);


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
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (videoPlayerFragment != null) {
            fragmentTransaction.remove(videoPlayerFragment);
        }
        fragmentTransaction.commit();
        mListener = null;
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onVideoPlayerFragmentInteraction(boolean bul) {
            if(mySurfaceView.mediaPlayerIsPlaying()) {
                Log.d("1999", "mySurfaceView.mediaPlayerIsPlaying() true");
                mySurfaceView.mediaPlayerPause();
            }
            else {
                Log.d("1999", "mySurfaceView.mediaPlayerIsPlaying() false");
                mySurfaceView.mediaPlayerStart();
            }
    }


    @Override
    public void ready(Object object) {
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if(videoPlayerFragment != null)
                       videoPlayerFragment.updateProgess(mySurfaceView.getMediaPlayerCurrentPosition(),
                                 mySurfaceView.getMediaPlayerDuration());
                       handler.postDelayed(this, 100);
               }
           });
    }


    @Override
    public void show_compression_menu_request() {
             compressionModeFragment = new CompressionModeFragment();
             compressionModeFragment.setOnCompressionModeFragmentInteractionListener(this);
             compressionModeFragment.show(getActivity().getFragmentManager(), "123");
    }

    //TODO для обрезки видео leftCur, leftMax - текущее положение (leftCur / (rightMax - leftMax))
    @Override
    public void doVideoKrop(int leftCur, int rightCur, int leftMax, int rightMax) {
        int len =  rightMax - leftMax;
        int len_video = mySurfaceView.getMediaPlayerDuration();
        float time_end = (rightCur - leftMax) *len_video/ len / 1000;
        float time_start = (leftCur - leftMax) * len_video / len / 1000;

        if (time_end < time_start){
            float time = time_end;
            time_end=time_start;
            time_start=time;
        }
        new DecodeVideo(getActivity(), time_start,time_end,DecodeVideo.Type.COPY);
    }

    @Override
    public void onConvertingFragmentInteraction(Uri uri) {

    }

    //TODO
    //            convertingProgressFragment.setProgess(); - отображает прогресс

    //TODO устанавливает режим конвертации видео
    @Override
    public void onCompressionModeFragmentInteraction(String buttonType) {
        convertingProgressFragment = new ConvertingProgressFragment();
        convertingProgressFragment.setOnConvertingFragmentInteractionListener(this);
        compressionModeFragment.dismiss();
        switch (buttonType) {
            case CompressionModeFragment.FAST_COMPRESS: {
                convertingProgressFragment.show(getActivity().getFragmentManager(), "456");
                decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH, UrlHolder.getInpurUrl());
                decoder.outputFile(UrlHolder.getInpurUrl() + ".mp4");
                decoder.setVideoCodec(Decoder.name_video_codec.MPEG4);

                //mySurfaceView.updateVideo(Uri.parse(inputUri.toString() + ".mp4"));
                break;
            }
            case CompressionModeFragment.QUALITY_COMPRESS: {
                convertingProgressFragment.show(getActivity().getFragmentManager(), "456");
                break;
            }
            case CompressionModeFragment.WITHOUT_COMPRESS: {
                //convertingProgressFragment.show(EditorActivity.this.getFragmentManager(), "456");
                break;
            }
        }
    }
}
