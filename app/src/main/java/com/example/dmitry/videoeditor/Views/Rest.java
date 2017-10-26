package com.example.dmitry.videoeditor.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.dmitry.videoeditor.Holders.ImageHolder;

import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dmitry on 21.03.17.
 */

public class Rest {
    private final static String METHOD_URL = "http://95.165.134.46:12177/replace_bg";
    private static Rest instance;

    private Rest(){};

    public static Rest getInstance() {
        if (instance == null) {
            instance = new Rest();
        }
        return instance;
    }

    public Bitmap sendRequest(Bitmap withMan, Bitmap fone) throws IOException {
        Log.d("2152", withMan.toString());
        Log.d("2152", fone.toString());
        new DownloadTask().execute(withMan, fone);
        return null;
    }

    private static Bitmap inputStreamToString(final InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        BufferedInputStream bis = new BufferedInputStream(is);
        Bitmap bitmap = BitmapFactory.decodeStream(bis);
        ImageHolder.getInstance().setKropedBitmap(bitmap);
        return bitmap;
    }


    class DownloadTask extends AsyncTask<Bitmap, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {}

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            Bitmap withMan = params[0];
            Bitmap fone = params[1];
            InputStream is = null;
            try {
                String attachmentName = "image";
                String attachmentFileName = "image.png";
                String attachmentName2 = "background";
                String attachmentFileName2 = "fon.png";
                String crlf = "\r\n";
                String twoHyphens = "--";
                String boundary =  "----WebKitFormBoundarym63O9wxrj46pz9SK";


                final String uri = Uri.parse(METHOD_URL  + "/replace_bg")
                        .buildUpon()
                        .build()
                        .toString();
                HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
                conn.setReadTimeout(40000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");



                DataOutputStream request = new DataOutputStream(
                        conn.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                byte[] pixels = new byte[withMan.getWidth() * withMan.getHeight()];
                for (int i = 0; i < withMan.getWidth(); ++i) {
                    for (int j = 0; j < withMan.getHeight(); ++j) {
                        //we're interested only in the MSB of the first byte,
                        //since the other 3 bytes are identical for B&W images
                        pixels[i + j] = (byte) ((withMan.getPixel(i, j) & 0x80) >> 7);
                    }
                }

                request.write(pixels);


                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName2 + "\";filename=\"" +
                        attachmentFileName2 + "\"" + crlf);
                request.writeBytes(crlf);

                byte[] pixels2 = new byte[fone.getWidth() *fone.getHeight()];
                for (int i = 0; i < fone.getWidth(); ++i) {
                    for (int j = 0; j < fone.getHeight(); ++j) {
                        //we're interested only in the MSB of the first byte,
                        //since the other 3 bytes are identical for B&W images
                        pixels[i + j] = (byte) ((fone.getPixel(i, j) & 0x80) >> 7);
                    }
                }

                request.write(pixels2);

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary +
                        twoHyphens + crlf);






                conn.connect();


                request.flush();
                request.close();


                int responseCode = conn.getResponseCode();
                is = conn.getInputStream();
                Log.d("2152", String.valueOf(conn.getResponseCode()));
               // if (responseCode == 200) {

                    return inputStreamToString(is);
              //  }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
           // ((ImageView)(rootView.findViewById(R.id.fragment_facebook_img))).setImageBitmap(result);

        }

        @Override
        protected void onCancelled() {

        }
    }



}