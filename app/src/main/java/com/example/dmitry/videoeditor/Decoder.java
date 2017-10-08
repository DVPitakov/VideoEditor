package com.example.dmitry.videoeditor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexandr on 09.10.17.
 */

public class Decoder {
    private String complexCommand;

    public enum name_command  {
        OVERWRITE_FILE,
        INPUT_FILE_FULL_PATH,
        AUDIO_SAMPLING_RETEAR,
        BITRATE_AUDIO,
        FORMAT_FILE,
        BITRATE_VIDEO,
        FPS,
        VIDEO_SCALE,
        KEYFRAME_DENSITY,
        VFRAMES,
        START_CROP_VIDEO,
        DURATION_CROP_VIDEO
    }

    private static final Map<name_command,String> command_list =  new HashMap<name_command , String>() {
        {
            put(name_command.OVERWRITE_FILE, "-y");
            put(name_command.INPUT_FILE_FULL_PATH, "-i");
            put(name_command.AUDIO_SAMPLING_RETEAR, "-ar");
            put(name_command.BITRATE_AUDIO, "-b:a");
            put(name_command.FORMAT_FILE, "-f");
            put(name_command.BITRATE_VIDEO, "-b:v");
            put(name_command.FPS, "-r");
            put(name_command.VIDEO_SCALE, "-s");
            put(name_command.KEYFRAME_DENSITY, "-g");
            put(name_command.VFRAMES, "-vframes");
            put(name_command.START_CROP_VIDEO, "-ss");
            put(name_command.DURATION_CROP_VIDEO, "-t");
        }
    };

    private Map<String,String> command_map;
    private String output_name_file;

    public Decoder(String input_file, String output_file) {
        command_map = new HashMap<>();
        output_name_file = output_file;
        addCommand(name_command.INPUT_FILE_FULL_PATH,input_file);
    }

    public String getComplexCommand(){
        Set<String> com = command_map.keySet();
        complexCommand ="";
        for (int i=0; i<com.size(); i++){
            complexCommand += command_map.get(com.toArray()[i]);
            complexCommand +=" ";
        }
        complexCommand += output_name_file;
        return complexCommand;
    }

    public void addCommand(name_command command,String value){
        if (command==name_command.OVERWRITE_FILE){
            command_map.put(command_list.get(command), command_list.get(command));
        }else {
            command_map.put(command_list.get(command), command_list.get(command)+" "+value);
        }
    }

    public void delCommand(name_command command){
        if (command==name_command.INPUT_FILE_FULL_PATH)
            return;
        command_map.remove(command_list.get(command));
    }

    public void inputFile(String path_name){
        output_name_file = path_name;
    }
}