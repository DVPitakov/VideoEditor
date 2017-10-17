package com.example.dmitry.videoeditor.Views;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.dmitry.videoeditor.IconImage;
import com.example.dmitry.videoeditor.ImageEditor;
import com.example.dmitry.videoeditor.ImageHolder;
import com.example.dmitry.videoeditor.MySurfaceView;
import com.example.dmitry.videoeditor.R;
import com.example.dmitry.videoeditor.TextImage;
import com.example.dmitry.videoeditor.Tools;

import layout.PanelColors;
import layout.PanelInstrumentImage;
import layout.PanelStckers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ImageFragment extends Fragment {
    final int MY_PERMISION = 1;

    private EditText editText;
    private FrameLayout surfaceViewPos;
    private OnFragmentInteractionListener mListener;

    ImageHolder imageHolder;
    ElementRedactorFragment elementRedactorHeader;

    MySurfaceView mySurfaceView;
    PanelColors fragment2;
    PanelStckers panelStckers;
    PanelInstrumentImage fragment;

    public ImageFragment() {}


    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        imageHolder = ImageHolder.getInstance();
        imageHolder.tryInit(getActivity());

        surfaceViewPos = (FrameLayout)(rootView.findViewById(R.id.surface_view_pos));
        mySurfaceView = new MySurfaceView(surfaceViewPos.getContext(), imageHolder);
        surfaceViewPos.addView(mySurfaceView);

        editText = (EditText) (rootView.findViewById(R.id.edutText));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mySurfaceView.setImageText(charSequence.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mySurfaceView.setImageText(charSequence.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mySurfaceView.setImageText(editable.toString());
                mySurfaceView.imageHolder.setBitmapWithElements(null);

            }
        });

        final float imageSize = getResources().getDimension(R.dimen.mySurfaceViewSize);
        mySurfaceView.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)imageSize));
        mySurfaceView.setFocusListener(
                new MySurfaceView.FocusListener() {
            @Override
            public void focusLosed() {
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment2);
                fragmentTransaction.commit();

            }

            @Override
            public void focusTaken() {

            }

            @Override
            public void doubleClick() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

                FragmentManager fragmentManager = getActivity().getFragmentManager();
                if(fragmentManager.findFragmentById(R.id.image_header_pos) == null) {
                    elementRedactorHeader = new ElementRedactorFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.slide_anim, 0)
                            .replace(R.id.image_header_pos, elementRedactorHeader)
                            .add(R.id.image_footer_pos, fragment2)
                            .commit();
                }
            }
        });
        fragment = new  PanelInstrumentImage(getActivity().getBaseContext());
        fragment2 = new PanelColors(getActivity().getBaseContext());
        panelStckers = new PanelStckers(getActivity().getBaseContext());

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_anim, R.animator.slide_anim)
                .replace(R.id.image_header_pos, fragment)
                .commit();

        final int KROP_ELEMENT = 0;
        final int FILTER_ELEMENT = 2;
        final int TEXT_ELEMENT = 1;
        final int IMAGE_ELEMENT = 3;
        fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean b =false;
            int i = 0;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case KROP_ELEMENT: {
                        if (b) {
                            mySurfaceView.kropUnset();
                            Rect rect = mySurfaceView.getKropRect();
                            Bitmap kropedBitmap = imageHolder.getKropedBitmap();
                            if (kropedBitmap == null) {
                                kropedBitmap = imageHolder.getDefaultBitmap();
                            }
                            kropedBitmap = ImageEditor.krop(kropedBitmap,
                                    rect.left, rect.top, rect.right, rect.bottom);
                            imageHolder.setKropedBitmap(kropedBitmap);
                            mySurfaceView.kropClear();
                            mySurfaceView.draw();
                        }
                        else {
                            mySurfaceView.kropSet();
                        }
                        b = !b;
                        break;
                    }
                    case FILTER_ELEMENT: {
                        this.i = (this.i + 1) % 4;
                        imageHolder.setFreshBitmap(null);
                        mySurfaceView.setEffect(this.i);
                        mySurfaceView.draw();
                        break;
                    }
                    case TEXT_ELEMENT: {
                        mySurfaceView.selectedImageElement = null;
                        editText.setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                        mySurfaceView.addImageElement(new TextImage("Новый текст", 60, 60));
                        Log.d("step", "step1");
                        FragmentManager fragmentManager = getFragmentManager();
                        if(fragmentManager.findFragmentById(R.id.image_footer_pos) == null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.image_footer_pos, fragment2);
                            fragmentTransaction.commit();
                        }
                        mySurfaceView.imageHolder.setBitmapWithElements(null);
                        mySurfaceView.draw();
                        break;
                    }
                    case IMAGE_ELEMENT: {
                        FragmentManager fragmentManager = getFragmentManager();
                        if(fragmentManager.findFragmentById(R.id.stickersLayout) == null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.stickersLayout, panelStckers);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 4: {
                        if (Build.VERSION.SDK_INT >= 23) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISION);

                        }
                        else {
                            Bitmap bitmap =  imageHolder.getBitmapWithElements();
                            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "image" , null);
                            Tools.saveAndSendImage(bitmap, getActivity());
                        }
                        break;
                    }
                    case 5: {
                        mySurfaceView.imageHolder.setKropedBitmap(null);
                        mySurfaceView.imageEditorQueue.clear();
                        mySurfaceView.draw();
                        break;
                    }
                }
            }
        });
        fragment2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int []Colors = {
                    Color.WHITE,
                    Color.BLUE,
                    Color.RED,
                    Color.GREEN,
                    Color.GRAY,
                    Color.YELLOW};
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mySurfaceView.setImageColor(Colors[i]);
                mySurfaceView.imageHolder.setBitmapWithElements(null);
                mySurfaceView.draw();

            }
        });

        panelStckers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mySurfaceView.addImageElement(new IconImage(PanelStckers.ITEMS[i], mySurfaceView, 60, 60));
                mySurfaceView.imageHolder.setBitmapWithElements(null);
                mySurfaceView.draw();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(panelStckers);
                fragmentTransaction.commit();
            }
        });
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
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        if (fragment2 != null) {
            fragmentTransaction.remove(fragment2);
        }
        if (panelStckers != null && panelStckers.isAdded()) {
            fragmentTransaction.remove(panelStckers);
        }
        fragmentTransaction.commit();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
