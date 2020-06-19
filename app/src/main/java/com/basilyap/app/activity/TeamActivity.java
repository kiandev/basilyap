package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamActivity extends AppCompatActivity {

    TextView txt_text;
    ImageView btn_back;
    LinearLayout no_internet, progressbar;
    ScrollView main_line;
    Button btn_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        no_internet = findViewById(R.id.no_internet);
        progressbar = findViewById(R.id.progressbar);
        main_line = findViewById(R.id.main_line);
        btn_again = findViewById(R.id.btn_again);

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(getApplicationContext())) {
                    Toast.makeText(TeamActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    btn_again.setEnabled(false);
                    btn_again.setClickable(false);
                    no_internet.setVisibility(View.GONE);
                    main_line.setVisibility(View.VISIBLE);
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

        txt_text = findViewById(R.id.text);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!NetTest.yes(getApplicationContext())) {
//            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
        } else {
            no_internet.setVisibility(View.GONE);
            main_line.setVisibility(View.VISIBLE);
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

    public void getdata() {
        String URL = HttpUrl.url + "team";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String get_text = jsonobject.getString("text");
                                progressbar.setVisibility(View.GONE);
                                txt_text.setText(get_text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(TeamActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(TeamActivity.this);
        requestQueue.add(stringRequest);
    }

}
