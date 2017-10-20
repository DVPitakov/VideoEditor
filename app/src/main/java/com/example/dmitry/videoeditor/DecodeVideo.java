package com.example.dmitry.videoeditor;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class DecodeVideo {
    public enum Type{
        COPY,
        HIGH_QUALITY,
        LOW_QUALITY,
    }

    public DecodeVideo( Context context, float start, float end, Type type){
        ffmpeg = FFmpeg.getInstance(context);
        loadFFMpegBinary();
        float duration =end - start;
        Decoder decoder = new Decoder();
        decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH,UrlHolder.getInpurUrl());
        decoder.outputFile(UrlHolder.getOutputUrl());
        decoder.addCommand(Decoder.name_command.START_CROP_VIDEO, String.valueOf(start));
        decoder.addCommand(Decoder.name_command.DURATION_CROP_VIDEO, String.valueOf(duration));
        decoder.addCommand(Decoder.name_command.OVERWRITE_FILE,"");
        decoder.setUltraFast();
        switch (type){
            case COPY:
                decoder.setVideoCodec(Decoder.name_video_codec.COPY);
                break;
            case  HIGH_QUALITY:
                decoder.setVideoCodec(Decoder.name_video_codec.H264);
                break;
            case LOW_QUALITY:
                decoder.setVideoCodec(Decoder.name_video_codec.MPEG4);
                break;

        }
        String[] command = decoder.getComplexCommand();
        if (command.length != 0) {
            execFFmpegBinary(command);
        }
    }

//    @Inject
    private FFmpeg ffmpeg;

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
              /*  @Override
                public void onFailure(String s) {
                  //  addTextViewToLayout("FAILED with output : "+s);
                }
    */
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    CurrentVideoHolder.getInstance()
                            .setUpdatedVideoLen(CurrentVideoHolder.getInstance().getVideoLen());
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
                }
/*
                @Override
                public void onStart() {
          //          outputLayout.removeAllViews();

          //          Log.d(TAG, "Started command : ffmpeg " + command);
          //          progressDialog.setMessage("Processing...");
          //          progressDialog.show();
                }

                @Override
                public void onFinish() {
           //         Log.d(TAG, "Finished command : ffmpeg "+command);
           //         progressDialog.dismiss();
                }*/
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

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
    }
}
