package com.example.dmitry.videoeditor;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.dmitry.videoeditor.Holders.UrlHolder;

public class MainActivity extends AppCompatActivity {

    Button inputPathButton;
    Button outputPathButton;
    Button editButton;

    TextView inputPathView;
    TextView outputPathView;

    Uri inputUri;
    Uri outputUri;




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


        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }

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
                Log.d("1996", inputUri.toString());
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);//////////////////
                UrlHolder.setInputUrl(inputUri, getApplicationContext());
                UrlHolder.setOutputUrl(UrlHolder.getInpurUrl() +"tmp.mp4");
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
