package edu.example.dmitry.videoeditor.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import edu.example.dmitry.videoeditor.EditorActivity;
import edu.example.dmitry.videoeditor.holders.CurrentElementHolder;
import edu.example.dmitry.videoeditor.holders.ImageHolder;
import edu.example.dmitry.videoeditor.holders.SurfaceViewHolder;
import edu.example.dmitry.videoeditor.items.BaseItem;
import edu.example.dmitry.videoeditor.items.TextItem;

public class ElementRedactorFragment extends Fragment {


    private ImageButton cancelButton;
    private ImageButton saveButton;
    private ImageButton fontButton;
    private ImageButton italicButton;
    private ImageButton boldButton;



    public ElementRedactorFragment() {
        // Required empty public constructor
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((EditorActivity)(getActivity())).editText.getWindowToken(), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(edu.example.dmitry.videoeditor.R.layout.fragment_element_redactor, container, false);
        cancelButton = (ImageButton) rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_element_redactor_cancel_button);
        saveButton = (ImageButton) rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_element_redactor_ok_button);
        fontButton = (ImageButton) rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_element_redactor_font_button);
        boldButton = (ImageButton) rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_element_redactor_bold_button);
        italicButton = (ImageButton) rootView.findViewById(edu.example.dmitry.videoeditor.R.id.fragment_element_redactor_italic_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);
                ((EditorActivity)(getActivity())).showDefaultImageHeader();
                SurfaceViewHolder.getInstance().getMySurfaceView().draw();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                SurfaceViewHolder.getInstance().getMySurfaceView().deleteCurrentItem();
                ((EditorActivity)(getActivity())).removeFragment(PanelColors.class);
                ((EditorActivity)(getActivity())).showDefaultImageHeader();
                ImageHolder.getInstance().setBitmapWithElements(null);
                SurfaceViewHolder.getInstance().getMySurfaceView().draw();
            }
        });



        fontButton.setOnClickListener(new View.OnClickListener() {
            int curFont = 0;
            @Override
            public void onClick(View view) {
                BaseItem baseItem = CurrentElementHolder.getInstance().getCurrentElement();
                if (CurrentElementHolder.getInstance().getCurrentElement() instanceof TextItem) {
                    ((TextItem) baseItem).setFont(curFont);
                    ImageHolder.getInstance().setBitmapWithElements(null);
                    SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                }
                curFont = (curFont + 1) % 3;
            }
        });

        italicButton.setOnClickListener(new View.OnClickListener() {
            boolean italic = true;
            @Override
            public void onClick(View view) {
                BaseItem baseItem = CurrentElementHolder.getInstance().getCurrentElement();
                if (CurrentElementHolder.getInstance().getCurrentElement().getClass() == TextItem.class) {
                    ((TextItem) baseItem).setItalic(italic);
                    italic = !italic;
                    ImageHolder.getInstance().setBitmapWithElements(null);
                    SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                }
            }
        });

        boldButton.setOnClickListener(new View.OnClickListener() {
            boolean bold = true;
            @Override
            public void onClick(View view) {
                BaseItem baseItem = CurrentElementHolder.getInstance().getCurrentElement();
                if (CurrentElementHolder.getInstance().getCurrentElement().getClass() == TextItem.class) {
                    ((TextItem) baseItem).setBold(bold);
                    bold = !bold;
                    ImageHolder.getInstance().setBitmapWithElements(null);
                    SurfaceViewHolder.getInstance().getMySurfaceView().draw();
                }
            }
        });
        return rootView;
    }

}
