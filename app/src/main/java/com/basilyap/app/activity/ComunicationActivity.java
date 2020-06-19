package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

public class ComunicationActivity extends AppCompatActivity {

    TextView txt_text, txt_call;
    ImageView btn_back;
    LinearLayout no_internet, progressbar;
    ScrollView main_line;
    Button btn_again;
    private String get_text, get_call;
    private String get_instagram, get_telegram, get_facebook;
    private String get_gmail, get_linkedin, get_youtube;
    LinearLayout btn_instagram, btn_telegram, btn_facebook;
    LinearLayout btn_gmail, btn_linkedin, btn_youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunication);

        no_internet = findViewById(R.id.no_internet);
        progressbar = findViewById(R.id.progressbar);
        main_line = findViewById(R.id.main_line);
        btn_again = findViewById(R.id.btn_again);

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(getApplicationContext())) {
                    Toast.makeText(ComunicationActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
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
        txt_call = findViewById(R.id.call);
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

        btn_instagram = findViewById(R.id.btn_instagram);
        btn_telegram = findViewById(R.id.btn_telegram);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_gmail = findViewById(R.id.btn_gmail);
        btn_linkedin = findViewById(R.id.btn_linkedin);
        btn_youtube = findViewById(R.id.btn_youtube);


        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(get_instagram);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(get_instagram)));
                }
            }
        });

        btn_telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(get_telegram));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(get_facebook));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(get_youtube));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(get_linkedin));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + get_gmail));
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "ارسال پیام به بازیل هوم");
//                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ComunicationActivity.this, "متاسفانه اپلیکیشن مناسب جهت ارسال ایمیل یافت نشد", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void getdata() {
        String URL = HttpUrl.url + "comunication";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                get_text = jsonobject.getString("text");
                                get_call = jsonobject.getString("call");
                                get_instagram = jsonobject.getString("instagram");
                                get_telegram = jsonobject.getString("telegram");
                                get_facebook = jsonobject.getString("facebook");
                                get_gmail = jsonobject.getString("gmail");
                                get_linkedin = jsonobject.getString("linkedin");
                                get_youtube = jsonobject.getString("youtube");
                                progressbar.setVisibility(View.GONE);
                                txt_text.setText(get_text);
                                txt_call.setText(get_call);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        btn_again.setEnabled(true);
                        btn_again.setClickable(true);
                        Toast.makeText(ComunicationActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ComunicationActivity.this);
        requestQueue.add(stringRequest);
    }

}
