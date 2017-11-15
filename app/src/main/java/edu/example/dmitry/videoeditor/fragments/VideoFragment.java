package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import edu.example.dmitry.videoeditor.holders.CurrentVideoHolder;
import edu.example.dmitry.videoeditor.DecodeVideo;
import edu.example.dmitry.videoeditor.Decoder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.views.MySurfaceView;
import edu.example.dmitry.videoeditor.SettingsVideo;

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
        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_video, container, false);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;
        decoder = new Decoder();
        imageHolder = ImageHolder.getInstance();
        imageHolder.tryInit(getActivity());

        final float imageSize = getResources().getDimension(edu.example.dmitry.videoeditor.R.dimen.mySurfaceViewSize);
        surfaceViewPos = (FrameLayout)(rootView.findViewById(edu.example.dmitry.videoeditor.R.id.video_surface_view_pos));
        mySurfaceView = new MySurfaceView(getActivity().getBaseContext());
        surfaceViewPos.addView(mySurfaceView);
        mySurfaceView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)imageSize));

        videoPlayerFragment = new VideoPlayerFragment();
        getFragmentManager()
                .beginTransaction()
                .add(edu.example.dmitry.videoeditor.R.id.video_footer_pos, videoPlayerFragment)
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onVideoPlayerFragmentInteraction(boolean bul) {
            if(mySurfaceView.mediaPlayerIsPlaying()) {
                mySurfaceView.mediaPlayerPause();
            }
            else {
                mySurfaceView.mediaPlayerStart();
            }
    }

    private boolean deleted = false;
    @Override
    public void onDestroy() {
        deleted = true;
        super.onDestroy();
    }

    @Override
    public void ready(Object object) {
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if(videoPlayerFragment != null && !deleted) {
                       videoPlayerFragment.updateProgess(
                               100f *
                                       mySurfaceView.getMediaPlayerCurrentPosition() /
                                       CurrentVideoHolder.getInstance().getVideoLen()
                       );
                   }
                   handler.postDelayed(this, 10);
               }
           });
    }


    @Override
    public void show_compression_menu_request() {
             compressionModeFragment = new CompressionModeFragment();
             compressionModeFragment.setOnCompressionModeFragmentInteractionListener(this);
             compressionModeFragment.show(getActivity().getSupportFragmentManager()
                     , CompressionModeFragment.class.getName());
    }

    //TODO для обрезки видео leftCur, leftMax - текущее положение (leftCur / (rightMax - leftMax))
    @Override
    public void doVideoKrop(int leftCur, int rightCur, int leftMax, int rightMax) {
        int len =  rightMax - leftMax;
        int len_video =(int)CurrentVideoHolder.getInstance().getVideoLen();
        float time_end = (rightCur - leftMax) * len_video/ len / 1000;
        float time_start = (leftCur - leftMax) * len_video / len / 1000;

        if (time_end < time_start){
            float time = time_end;
            time_end=time_start;
            time_start=time;
        }

        ConvertingProgressFragment convertingProgressFragment
                = new ConvertingProgressFragment();
        convertingProgressFragment
                .show(getFragmentManager(), ConvertingProgressFragment.class.getName());
        new DecodeVideo(getActivity(), time_start,time_end
                , CurrentVideoHolder.getInstance().getCompressType());
    }

    @Override
    public void onConvertingFragmentInteraction(Uri uri) {

    }

    //TODO устанавливает режим конвертации видео
    @Override
    public void onCompressionModeFragmentInteraction(String buttonType) {
        convertingProgressFragment = new ConvertingProgressFragment();
        convertingProgressFragment.setOnConvertingFragmentInteractionListener(this);
        compressionModeFragment.dismiss();
         switch (buttonType) {
            case CompressionModeFragment.FAST_COMPRESS: {
                Log.d("1150", "FAST");
                CurrentVideoHolder.getInstance().setCompressType(DecodeVideo.Type.LOW_QUALITY);
                decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH, SettingsVideo.getInput(""));
                decoder.outputFile(SettingsVideo.getInput("") + ".mp4");
                decoder.setVideoCodec(Decoder.name_video_codec.MPEG4);
                break;
            }
            case CompressionModeFragment.QUALITY_COMPRESS: {
                Log.d("1150", "QUALITY_COMPRESS");
                CurrentVideoHolder.getInstance().setCompressType(DecodeVideo.Type.MEDIUM_QUALITY);
                decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH, SettingsVideo.getInput(""));
                decoder.outputFile(SettingsVideo.getInput("") + ".mp4");
                decoder.setVideoCodec(Decoder.name_video_codec.H264);
                break;
            }
            case CompressionModeFragment.WITHOUT_COMPRESS: {
                CurrentVideoHolder.getInstance().setCompressType(DecodeVideo.Type.HIGK_QUALITY);
                Log.d("1150", "WITHOUT_COMPRESS");
                decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH, SettingsVideo.getInput(""));
                decoder.outputFile(SettingsVideo.getInput("") + ".mp4");
                decoder.setVideoCodec(Decoder.name_video_codec.COPY);
                break;
            }
        }
    }
}
