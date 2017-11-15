package edu.example.dmitry.videoeditor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import edu.example.dmitry.videoeditor.models.SettingsData;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
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

        ViewGroup root = (ViewGroup)
                inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_settings, container, false);
        final SettingsData settingsData = new SettingsData(getContext());
        ((CheckBox)root.findViewById(edu.example.dmitry.videoeditor.R.id.show_frame)).setChecked(settingsData.isShowFrame());
        ((CheckBox)root.findViewById(edu.example.dmitry.videoeditor.R.id.show_frame)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingsData.setShowFrame(b);
                settingsData.saveSettingsData();
            }
        });
        ((CheckBox)root.findViewById(edu.example.dmitry.videoeditor.R.id.show_history)).setChecked(settingsData.isHistory());
        ((CheckBox)root.findViewById(edu.example.dmitry.videoeditor.R.id.show_history)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingsData.setHistory(b);
                settingsData.saveSettingsData();
            }
        });
        return root;
    }


}
