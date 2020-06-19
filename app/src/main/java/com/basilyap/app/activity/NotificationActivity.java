package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.adapter.NotificationAdapter;
import com.basilyap.app.model.Notification;
import com.basilyap.app.model.UnitBase;
import com.basilyap.app.model.UnitImage;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    ArrayList<Notification> os_version = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView btn_back;
    private NotificationAdapter mAdapter;
    LinearLayoutManager layoutManager;
    LinearLayout no_internet, progressbar;
    Button btn_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        no_internet = findViewById(R.id.no_internet);
        progressbar = findViewById(R.id.progressbar);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter = new NotificationAdapter(os_version);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
        layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());

        if (!NetTest.yes(getApplicationContext())) {
//            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
        } else {
            no_internet.setVisibility(View.GONE);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        getdata();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        btn_again = findViewById(R.id.btn_again);
        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(NotificationActivity.this)) {
                    Toast.makeText(NotificationActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    btn_again.setEnabled(false);
                    btn_again.setClickable(false);
                    no_internet.setVisibility(View.GONE);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getdata();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    private void getdata() {
        String URL = HttpUrl.url + "notification";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject notification = array.getJSONObject(i);
                                os_version.add(new Notification(
                                        notification.getInt("id"),
                                        notification.getString("title"),
                                        notification.getString("text"),
                                        notification.getString("date")
                                ));
                            }
                            progressbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificationActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        btn_again.setEnabled(true);
                        btn_again.setClickable(true);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        requestQueue.add(stringRequest);

    }
}
