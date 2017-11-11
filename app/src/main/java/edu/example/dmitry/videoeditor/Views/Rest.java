package edu.example.dmitry.videoeditor.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import edu.example.dmitry.videoeditor.Holders.ImageHolder;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by dmitry on 21.03.17.
 */

public class Rest {
    private final static String METHOD_URL = "http://gpu-external01.i.smailru.net:12177";
    private static Rest instance;


    private Rest(){};

    public static Rest getInstance() {
        if (instance == null) {
            instance = new Rest();
        }
        return instance;
    }

    public Bitmap sendRequest(Bitmap withMan) throws IOException {
        new DownloadTask().execute(withMan);
        return null;
    }

    private static Bitmap inputStreamToString(final InputStream is) throws IOException {
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
            InputStream is = null;
            DataOutputStream request = null;
            try {
                String attachmentName = "image";
                String attachmentFileName = "image.png";
                String crlf = "\r\n";
                String twoHyphens = "--";
                String boundary =  "*****";


                final String uri = Uri.parse(METHOD_URL  + "/drop_bg")
                        .buildUpon()
                        .build()
                        .toString();

                HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setReadTimeout(40000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

                request = new DataOutputStream(conn.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                byte[] pixels = new byte[withMan.getWidth() * withMan.getHeight()];
                for (int i = 0; i < withMan.getWidth(); ++i) {
                    for (int j = 0; j < withMan.getHeight(); ++j) {
                        pixels[i + j] = (byte) ((withMan.getPixel(i, j) & 0x80) >> 7);
                    }
                }
                request.write(pixels);

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary +
                        twoHyphens + crlf);

                Log.d("292017", "begin conn");
                conn.connect();


                Log.d("292017", "after conn");
                Log.d("292017", conn.toString());



                Log.d("292017", "connResponse:" + conn.getResponseCode());
                int responseCode = conn.getResponseCode();
                is = conn.getInputStream();
                if (responseCode == 200) {
                    return inputStreamToString(is);
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                    try {
                        if (request != null) {
                            request.flush();
                            request.close();
                        }
                        if  (is != null) {
                                is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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