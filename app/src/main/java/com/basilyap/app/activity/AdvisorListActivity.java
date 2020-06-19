package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.adapter.AdvisorAdapter;
import com.basilyap.app.adapter.ChatAdapter;
import com.basilyap.app.model.Advisor;
import com.basilyap.app.model.Chat;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvisorListActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    ArrayList<Advisor> os_version = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView btn_back;
    private AdvisorAdapter mAdapter;
    LinearLayoutManager layoutManager;
    LinearLayout no_internet, progressbar;
    Button btn_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_list);

        no_internet = findViewById(R.id.no_internet);
        progressbar = findViewById(R.id.progressbar);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter = new AdvisorAdapter(os_version);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdvisorListActivity.this, LinearLayoutManager.VERTICAL, false));
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
                if (!NetTest.yes(AdvisorListActivity.this)) {
                    Toast.makeText(AdvisorListActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
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
        String URL = HttpUrl.url + "advisor";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject advisor = array.getJSONObject(i);
                                os_version.add(new Advisor(
                                        advisor.getInt("id"),
                                        advisor.getString("persian"),
                                        advisor.getString("english"),
                                        advisor.getString("title"),
                                        advisor.getString("email"),
                                        advisor.getString("phone"),
                                        advisor.getString("image")
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
                        Toast.makeText(AdvisorListActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        btn_again.setEnabled(true);
                        btn_again.setClickable(true);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(AdvisorListActivity.this);
        requestQueue.add(stringRequest);

    }
}
