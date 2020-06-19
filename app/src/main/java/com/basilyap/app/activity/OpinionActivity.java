package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.basilyap.app.utils.SharedContract;

import java.util.HashMap;
import java.util.Map;

public class OpinionActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    AppCompatEditText txt_gettext;
    CardView btn_send;
    ImageView btn_back;
    ProgressDialog progressDialog;
    public String manufacturer, model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);

        progressDialog = new ProgressDialog(OpinionActivity.this);

        try {
            getDeviceName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txt_gettext = findViewById(R.id.txt_gettext);
        btn_send = findViewById(R.id.btn_send);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_gettext.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OpinionActivity.this, "لطفا متن خود را وارد نمایید سپس دکمه ارسال را بفشارید", Toast.LENGTH_SHORT).show();
                } else if (txt_gettext.getText().length() < 10) {
                    Toast.makeText(OpinionActivity.this, "متن وارد شده کوتاه می باشد لطفا متن خود را کامل وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(OpinionActivity.this)) {
                        Toast.makeText(OpinionActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_send.setEnabled(false);
                        btn_send.setClickable(false);
                        try {
                            send_opinion();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void send_opinion() {
        progressDialog.setMessage("در حال ارسال ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String httpurl = HttpUrl.url + "opinion";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")) {
                            progressDialog.dismiss();
                            final Dialog dialog = new Dialog(OpinionActivity.this);
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("پیام شما با موفقیت ارسال گردید ، باتشکر ...!");
                            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            dialog.show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(OpinionActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                            btn_send.setEnabled(true);
                            btn_send.setClickable(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(OpinionActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        btn_send.setEnabled(true);
                        btn_send.setClickable(true);
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                String get_text_from_txt = txt_gettext.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", get_text_from_txt);
                params.put("model", manufacturer + " " + model);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public String getDeviceName() {
        manufacturer = Build.MANUFACTURER;
        Log.d(TAG, "getDeviceName: " + manufacturer);
        model = Build.MODEL;
        Log.d(TAG, "getModel: " + model);
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
