package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutActivity extends AppCompatActivity {

    TextView txt_about, txt_which, txt_why;
    ImageView btn_back;
    LinearLayout no_internet, progressbar;
    ScrollView main_line;
    Button btn_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            getWindow().setStatusBarColor(ContextCompat.getColor(AboutActivity.this,R.color.colorBN_status));
//            getWindow().setNavigationBarColor(ContextCompat.getColor(AboutActivity.this,R.color.colorBN_status));
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

        no_internet = findViewById(R.id.no_internet);
        progressbar = findViewById(R.id.progressbar);
        main_line = findViewById(R.id.main_line);
        btn_again = findViewById(R.id.btn_again);

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(getApplicationContext())) {
                    Toast.makeText(AboutActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    no_internet.setVisibility(View.GONE);
                    main_line.setVisibility(View.VISIBLE);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            getdata();
                        }
                    });
                }

            }
        });

        txt_about = findViewById(R.id.about);
        txt_which = findViewById(R.id.which);
        txt_why = findViewById(R.id.why);
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
                    getdata();
                }
            });
        }
    }

    public void getdata() {
        String URL = HttpUrl.url + "about";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String get_about = jsonobject.getString("about");
                                String get_which = jsonobject.getString("which");
                                String get_why = jsonobject.getString("why");
                                progressbar.setVisibility(View.GONE);
                                txt_about.setText(get_about);
                                txt_which.setText(get_which);
                                txt_why.setText(get_why);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AboutActivity.this, "متاسفانه خطایی نامشخصی رخ داده است", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(AboutActivity.this);
        requestQueue.add(stringRequest);
    }
}
