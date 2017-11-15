package edu.example.dmitry.videoeditor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;

import edu.example.dmitry.videoeditor.adapters.ImageVideoSelectAdapter;


public class MainActivity extends FragmentActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0b001;
    private static final int REQUEST_FILE_PICKED = 0b010;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0b011;
    private static final int REQUEST_VIDEO_CAPTURE = 0b100;

    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;

    TextView inputPathView;
    TextView outputPathView;

    Uri inputUri;
    Uri outputUri;

    File directory;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        inputPathView = (TextView)findViewById(R.id.inputPathView);
        outputPathView = (TextView)findViewById(R.id.outputPathView);
        getPerrmisions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            continueDrawActivity();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Tools.setForceVideo(false);
            Log.d("debugshit", "inputUri " + inputUri);
            outputUri = inputUri;
            inputPathView.setText(inputUri.toString());
            outputPathView.setText(outputUri.toString());
            SettingsVideo.setInput(inputUri);
            SettingsVideo.setOutput(SettingsVideo.getInput("") + "tmp.mp4");
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            intent.putExtra(EditorActivity.INPUT_URI, inputUri);
            intent.putExtra(EditorActivity.OUTPUT_URI, outputUri);
            startActivityForResult(intent, 3);
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Tools.setForceVideo(true);
            Log.d("debugshit", "inputUri " + inputUri);
            outputUri = inputUri;
            inputPathView.setText(inputUri.toString());
            outputPathView.setText(outputUri.toString());
            SettingsVideo.setInput(inputUri);
            SettingsVideo.setOutput(SettingsVideo.getInput("") + "tmp.mp4");
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            intent.putExtra(EditorActivity.INPUT_URI, inputUri);
            intent.putExtra(EditorActivity.OUTPUT_URI, outputUri);
            startActivityForResult(intent, 3);
        }
        else if(requestCode == REQUEST_FILE_PICKED) {
            if (resultCode == RESULT_OK) {
                inputUri = data.getData();
                inputPathView.setText(inputUri.toString());
                outputUri = data.getData();
                outputPathView.setText(outputUri.toString());
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                SettingsVideo.setInput(inputUri);
                SettingsVideo.setOutput(SettingsVideo.getInput("") + "tmp.mp4");
                intent.putExtra(EditorActivity.INPUT_URI, inputUri);
                intent.putExtra(EditorActivity.OUTPUT_URI, outputUri);
                startActivityForResult(intent, 3);
            }

        }

    }


    private void getPerrmisions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("1996", "i am here 2");
                    requestPermissions(new String[]{Manifest.permission.CAMERA
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    , Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CAMERA);
            }
            else {
                continueDrawActivity();
            }
        }
        else {
            continueDrawActivity();
        }
    }

    private void continueDrawActivity() {

        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyFolder");
        if (!directory.exists())
            directory.mkdirs();

        findViewById(R.id.galerey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickerIntent.setType("*/*");
                String[] mimetypes = {"image/*", "video/*"};
                pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(pickerIntent, REQUEST_FILE_PICKED);
                SettingsVideo.setContext(getApplicationContext());
            }
        });


        findViewById(R.id.make_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.automat);
                String str = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver()
                        , bitmap
                        , "image.jpg"
                        , null);
                inputUri = Uri.parse(str);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, inputUri);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        findViewById(R.id.make_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.automat);
                String str = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver()
                        , bitmap
                        , "video.mp4"
                        , null);
                inputUri = Uri.parse(str);
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, inputUri);
                startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE);
            }
        });

        GridView gv = (GridView) (findViewById(R.id.image_video_grid_view));
        gv.setAdapter(
                new ImageVideoSelectAdapter(this
                        , R.layout.photo_video_tip_rect
                        , Tools.getAllImages(this)));

        findViewById(R.id.hello).setVisibility(View.GONE);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                inputUri = Uri.parse((String) view.getTag());
                outputUri = inputUri;
                inputPathView.setText(inputUri.toString());
                outputPathView.setText(outputUri.toString());
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                SettingsVideo.setInput(inputUri);
                SettingsVideo.setOutput(SettingsVideo.getInput("") + "tmp.mp4");
                intent.putExtra(EditorActivity.INPUT_URI, inputUri);
                intent.putExtra(EditorActivity.OUTPUT_URI, outputUri);
                startActivityForResult(intent, 3);
            }
        });

    }


}

