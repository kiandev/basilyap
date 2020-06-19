package com.basilyap.app.classes;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class fcm {

    public final static String AUTH_KEY_FCM = "AAAAsxGiyk0:APA91bF9BTIBrRr-2X4gI1kqKE-RUrAmT4cFCYIffapBVB_MHONqs7IS3f2AjEsTXCThnW4A_4InEyGGlhFp-uPjaMS6i7aEG8uthhcQu9JlduYUcew9S4kWhaJymzbii1q760UWpEA4";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static void sendNotification(final String Distination, final String Title, final String Text) throws Exception {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BufferedReader in = null;


                String authKey = AUTH_KEY_FCM;
                String FMCurl = API_URL_FCM;

                URL url = null;
                try {
                    url = new URL(FMCurl);
                } catch (MalformedURLException e) {
                    Log.i("error", "error: 1");
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    Log.i("error", "error: 2");
                    e.printStackTrace();
                }

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    Log.i("error", "error: 3");
                    e.printStackTrace();
                }
                conn.setRequestProperty("Authorization","key="+authKey);
                conn.setRequestProperty("Content-Type","application/json");

                JSONObject json = new JSONObject();
                try {
                    json.put("to",Distination.trim());
                } catch (JSONException e) {
                    Log.i("error", "error: 4");
                    e.printStackTrace();
                }
                JSONObject info = new JSONObject();
                try {
                    info.put("title", Title);   // Notification title
                } catch (JSONException e) {
                    Log.i("error", "error: 5");
                    e.printStackTrace();
                }
                try {
                    info.put("body", Text); // Notification body
                } catch (JSONException e) {
                    Log.i("error", "error: 6");
                    e.printStackTrace();
                }
                try {
                    json.put("notification", info);
                } catch (JSONException e) {
                    Log.i("error", "error: 7");
                    e.printStackTrace();
                }

                OutputStreamWriter wr = null;
                try {
                    wr = new OutputStreamWriter(conn.getOutputStream());
                } catch (IOException e) {
                    Log.i("error", "error: 8");
                    e.printStackTrace();
                }
                try {
                    wr.write(json.toString());
                } catch (IOException e) {
                    Log.i("error", "error: 9");
                    e.printStackTrace();
                }
                try {
                    wr.flush();
                } catch (IOException e) {
                    Log.i("error", "error: 10");
                    e.printStackTrace();
                }
                try {
                    conn.getInputStream();
                } catch (IOException e) {
                    Log.i("error", "error: 11");
                    e.printStackTrace();
                }

                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
