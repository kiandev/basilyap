package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.media.Image;
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
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgetActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    AppCompatEditText txt_getemail;
    CardView btn_getemail;
    private String password;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        txt_getemail = findViewById(R.id.txt_getemail);
        btn_getemail = findViewById(R.id.btn_getemail);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_getemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_getemail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ForgetActivity.this, "لطفا ایمیل خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(ForgetActivity.this)) {
                        Toast.makeText(ForgetActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                send_password();
                            }
                        });
                    }
                }
            }
        });

    }

    private void send_password() {
        String httpurl = HttpUrl.url + "user/forget";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("[]")) {
                            final Dialog dialog = new Dialog(ForgetActivity.this);
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("حساب کاربری با این ایمیل یافت نشد ، شما می توانید از بخش پروفایل کاربری نسبت به ساخت حساب کاربری جدید اقدام نمایید");
                            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            dialog.show();
                        } else {
                            try {
                                JSONArray jsonarray = new JSONArray(response);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    password = jsonobject.getString("password");
                                    sendEmail();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                String get_email_from_txt = txt_getemail.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", get_email_from_txt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void sendEmail() {
        String httpurl = HttpUrl.email;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        final Dialog dialog = new Dialog(ForgetActivity.this);
                        dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                        TextView text = dialog.findViewById(R.id.text);
                        dialog.setCancelable(false);
                        text.setText("کلمه عبور به ایمیل شما ارسال گردید ، در صورت عدم مشاهده لطفا پوشه spam را بررسی نمایید");
                        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                        dialog.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgetActivity.this, "متاسفانه خطایی در ارسال ایمیل اتفاق افتاده است ، لطفا بعدا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", txt_getemail.getText().toString());
                params.put("subject", "ارسال کلمه عبور");
                params.put("body", "کلمه عبور شما عبارت است از : " + " : " + password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
