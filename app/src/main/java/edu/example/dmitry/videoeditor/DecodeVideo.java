package edu.example.dmitry.videoeditor;

import android.content.Context;

import edu.example.dmitry.videoeditor.Holders.CurrentVideoHolder;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import edu.vincent.videocompressor.VideoCompress;
import edu.vincent.videocompressor.VideoController;


public class DecodeVideo {
    public enum Type{
        HIGK_QUALITY,
        MEDIUM_QUALITY,
        LOW_QUALITY,
    }

    public static float getDuration() {
        return duration;
    }

    public static void setDuration(float duration) {
        DecodeVideo.duration = duration;
    }

    private static float duration;

    private Context _context;

    public DecodeVideo( Context context, float start, float end, Type type){
        /*
        ffmpeg = FFmpeg.getInstance(context);
        loadFFMpegBinary();

        Decoder decoder = new Decoder();
        */
        _context = context;
        long size = 1000000;
        long scale = 1;
        setDuration((end - start)*scale);
        VideoController.setStartTime((long) start*size);
        VideoController.setEndTime((long) end*size);
       //   VideoController.setStartTime(-1);
       //   VideoController.setEndTime(-1);
        switch (type){
            case HIGK_QUALITY:
                //decoder.setVideoCodec(Decoder.name_video_codec.HIGK_QUALITY);
                VideoCompress.compressVideoHigh(SettingsVideo.getInput(""),SettingsVideo.getOutput(""),newListener());
                break;
            case MEDIUM_QUALITY:
                //decoder.setVideoCodec(Decoder.name_video_codec.H264);
                VideoCompress.compressVideoMedium(SettingsVideo.getInput(""),SettingsVideo.getOutput(""),newListener());
                break;
            case LOW_QUALITY:
                //decoder.setVideoCodec(Decoder.name_video_codec.MPEG4);
                VideoCompress.compressVideoLow(SettingsVideo.getInput(""),SettingsVideo.getOutput(""),newListener());
                break;

        }
        /*
        decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH, SettingsVideo.getInput());
        decoder.outputFile(SettingsVideo.getOutput(""));
        decoder.addCommand(Decoder.name_command.START_CROP_VIDEO, String.valueOf(start));
        decoder.addCommand(Decoder.name_command.DURATION_CROP_VIDEO, String.valueOf(duration));
        decoder.addCommand(Decoder.name_command.OVERWRITE_FILE,"");
        decoder.setUltraFast();

        String[] command = decoder.getComplexCommand();
        if (command.length != 0) {
            execFFmpegBinary(command);
        }
        */
    }

//    @Inject
//    private FFmpeg ffmpeg;

    private VideoCompress.CompressListener newListener(){
        return new VideoCompress.CompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                Tools.sendVideo(_context);
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onProgress(float percent) {
   //             long time = (long)getDuration() * 1000;
   //             time *= percent;
                CurrentVideoHolder.getInstance().setUpdatedVideoLen(percent);
            }
        };
    }
/*
    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
              /*  @Override
                public void onFailure(String s) {
                  //  addTextViewToLayout("FAILED with output : "+s);
                }
    */ /*
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    CurrentVideoHolder.getInstance()
                            .setUpdatedVideoLen(CurrentVideoHolder.getInstance().getVideoLen());
                    Tools.sendVideo(_context);
                }
                @Override
                public void onProgress(String s) {
                    super.onProgress(s);
                    System.out.println(s);

                    String [] list_1 = s.split("time=");
                    if (list_1.length==1) return;
                    String [] list_2 = list_1[1].split(" ");
                    String time_full = list_2[0];
                    String [] time_1 = time_full.split("\\.");
                    String [] time_2 = time_1[0].split(":");
                    String num;
                    long times = 0;
                    for (int i =0; i <time_2.length; i++){
                        times *= 60;
                        num = time_2[i];
                        times += Long.parseLong(num);
                    }
                    times *= 1000;
                    times += Long.parseLong(time_1[1]);
                    CurrentVideoHolder.getInstance().setUpdatedVideoLen(times);
                   /// Tools.sendVideo(_context);
                }
/*
                @Override
                public void onStart() {
          //          outputLayout.removeAllViews();

          //          Log.d(TAG, "Started command : ffmpeg " + command);
          //          progressDialog.setMessage("Processing...");
          //          progressDialog.show();
          ///          Tools.sendVideo(_context);
                }

                @Override
                public void onFinish() {
           //         Log.d(TAG, "Finished command : ffmpeg "+command);
           //         progressDialog.dismiss();
                }*//*
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }
/*
    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
               //     showUnsupportedExceptionDialog();
                }
            });
        } catch (FFmpegNotSupportedException e) {
        //    showUnsupportedExceptionDialog();
        }
    }*/
}
