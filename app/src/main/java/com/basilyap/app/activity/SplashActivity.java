package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.basilyap.app.R;
import com.basilyap.app.classes.MyFirebaseMessagingService;
import com.basilyap.app.utils.SharedContract;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    private static String tokenId;
    public static final String TAG = MainActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String general = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SharedContract.General, "no");
                if (general.equals("no")) {
                    FirebaseMessaging.getInstance().subscribeToTopic("general").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.General, "yes").apply();
                        }
                    });
                }

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
        subscribeUserToParse();
    }


    private void subscribeUserToParse() {
        tokenId = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(tokenId)) {
            Intent intent = new Intent(this, MyFirebaseMessagingService.class);
            startService(intent);
            return;
        }
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Token_Id, tokenId).apply();
        Log.d(TAG, "subscribeUserToParse: " + tokenId);
    }
}
