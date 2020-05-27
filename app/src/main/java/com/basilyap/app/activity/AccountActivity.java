package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.os.AsyncTask;
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
import com.basilyap.app.model.UnitImage;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AccountActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    AppCompatEditText txt_name, txt_phone;
    CardView btn_ok;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        txt_name = findViewById(R.id.txt_getname);
        txt_phone = findViewById(R.id.txt_getphone);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AccountActivity.this, "لطفا نام خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else if (txt_phone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AccountActivity.this, "لطفا شماره تماس خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else if (txt_phone.getText().length() < 10) {
                    Toast.makeText(AccountActivity.this, "لطفا شماره تماس خود را به صورت کامل وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(AccountActivity.this)) {
                        Toast.makeText(AccountActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        change_account_detail();
                    }
                }
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                loadAccountDetail();
            }
        });

    }

    public void change_account_detail() {
        String httpurl = HttpUrl.url + "user/update_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")) {
                            PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).edit().putString(SharedContract.Name,txt_name.getText().toString()).apply();
                            PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).edit().putString(SharedContract.Phone,txt_phone.getText().toString()).apply();
                            final Dialog dialog = new Dialog(AccountActivity.this);
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("اطلاعات حساب کاربری با موفقیت بروزآوری شد");
                            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            dialog.show();
                        } else {
                            Toast.makeText(AccountActivity.this, "متاسفانه خطایی در ارسال ایمیل اتفاق افتاده است ، لطفا بعدا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(AccountActivity.this, "متاسفانه خطایی رخ داده است ، لطفا مجددا بعدا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String get_email_from_shared = PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).getString(SharedContract.Email, "");
                String get_name_from_txt = txt_name.getText().toString();
                String get_phone_from_txt = txt_phone.getText().toString();
                params.put("email", get_email_from_shared);
                params.put("name", get_name_from_txt);
                params.put("phone", get_phone_from_txt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void loadAccountDetail() {
        String URL = HttpUrl.url + "user/get_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String get_name = jsonobject.getString("name");
                                String get_phone = jsonobject.getString("phone");
                                txt_name.setText(get_name);
                                txt_phone.setText(get_phone);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountActivity.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String get_email_from_shared = PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).getString(SharedContract.Email, "");
                params.put("email", get_email_from_shared);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
