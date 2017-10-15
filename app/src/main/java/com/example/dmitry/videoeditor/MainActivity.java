package com.example.dmitry.videoeditor;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    Button inputPathButton;
    Button outputPathButton;
    Button editButton;

    TextView inputPathView;
    TextView outputPathView;

    Uri inputUri;
    Uri outputUri;

    public static String getFilePath(Context context, Uri uri)  {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        inputPathButton = (Button)findViewById(R.id.inputPathButton);
        outputPathButton = (Button)findViewById(R.id.outputPathButton);
        editButton = (Button)findViewById(R.id.editButton);
        inputPathView = (TextView)findViewById(R.id.inputPathView);
        outputPathView = (TextView)findViewById(R.id.outputPathView);

        outputPathButton.setEnabled(true);
        editButton.setEnabled(true);

        inputPathButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent pickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickerIntent.setType("*/*");
                String[] mimetypes = {"image/*", "video/*"};
                pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(pickerIntent, 1);
            }
        });

        outputPathButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent pickerIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                pickerIntent .setType("*/*");
                String[] mimetypes = {"image/*", "video/*"};
                pickerIntent .putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(pickerIntent, 2);



            }
        });

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);//////////////////
                UrlHolder.setInputUrl(getFilePath(MainActivity.this.getApplicationContext(), inputUri));
                UrlHolder.setOutputUrl(getFilePath(MainActivity.this.getApplicationContext(), inputUri) +"tmp.mp4");
                intent.putExtra(EditorActivity.INPUT_URI, inputUri);
                intent.putExtra(EditorActivity.OUTPUT_URI, outputUri);
                startActivityForResult(intent, 3);

            }
        });

        Intent pickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickerIntent.setType("*/*");
        String[] mimetypes = {"image/*", "video/*"};
        pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(pickerIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    inputUri = data.getData();
                    inputPathView.setText(inputUri.toString());
                    outputPathButton.setEnabled(true);

                    editButton.callOnClick();
                }
                break;
            }
            case 2:
            {
                if (resultCode == RESULT_OK)
                {
                    outputUri = data.getData();
                    outputPathView.setText(outputUri.toString());
                    editButton.setEnabled(true);
                }
                break;
            }
            case 3:
            {

            }
        }
    }
}
