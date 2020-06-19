package com.basilyap.app.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.activity.AdvisorListActivity;
import com.basilyap.app.activity.ComunicationActivity;
import com.basilyap.app.activity.ExchangeActivity;
import com.basilyap.app.activity.WeblogListActivity;
import com.basilyap.app.utils.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Fragment_Vitrin extends Fragment {

    LinearLayout btn_advisor, btn_instagram, btn_telegram, btn_weblog, btn_exchange;
    private String get_instagram, get_telegram;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vitrin, container, false);

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

        btn_advisor = view.findViewById(R.id.btn_advisor);
        btn_advisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdvisorListActivity.class));
            }
        });

        btn_weblog = view.findViewById(R.id.btn_weblog);
        btn_weblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WeblogListActivity.class));
            }
        });

        btn_exchange = view.findViewById(R.id.btn_exchange);
        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ExchangeActivity.class));
            }
        });

        btn_instagram = view.findViewById(R.id.btn_instagram);
        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(get_instagram);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(get_instagram)));
                }
            }
        });

        btn_telegram = view.findViewById(R.id.btn_telegram);
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

        return view;
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
                                get_instagram = jsonobject.getString("instagram");
                                get_telegram = jsonobject.getString("telegram");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
