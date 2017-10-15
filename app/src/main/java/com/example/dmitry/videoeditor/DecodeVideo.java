package com.example.dmitry.videoeditor;

import android.util.Log;

import javax.inject.Inject;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class DecodeVideo {
    public enum Type{
        COPY,
        HIGH_QUALITY,
        LOW_QUALITY,
    }

    public static void decode(float start, float end, Type type){
        float duration = start - end;
        Decoder decoder = new Decoder();
        decoder.addCommand(Decoder.name_command.INPUT_FILE_FULL_PATH,"/");
        decoder.outputFile("/");
        decoder.addCommand(Decoder.name_command.START_CROP_VIDEO, String.valueOf(start));
        decoder.addCommand(Decoder.name_command.DURATION_CROP_VIDEO, String.valueOf(duration));
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

    @Inject
    private static FFmpeg ffmpeg;

    private static void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
              /*  @Override
                public void onFailure(String s) {
                  //  addTextViewToLayout("FAILED with output : "+s);
                }

                @Override
                public void onSuccess(String s) {
           //         addTextViewToLayout("SUCCESS with output : "+s);
                }

                @Override
                public void onProgress(String s) {
           //         Log.d(TAG, "Started command : ffmpeg "+command);
           //         addTextViewToLayout("progress : "+s);
           //         progressDialog.setMessage("Processing\n"+s);
                }

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

}
