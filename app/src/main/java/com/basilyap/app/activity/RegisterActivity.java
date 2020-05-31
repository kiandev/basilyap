package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.work.impl.model.Preference;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    AppCompatEditText txt_getemail, txt_getcode, txt_getpass;
    CardView btn_getemail, btn_getcode, btn_getpass;
    LinearLayout get_email, get_code, get_password;
    public String email, code, password;
    public int randomNumber;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        get_email = findViewById(R.id.get_email);
        get_code = findViewById(R.id.get_code);
        get_password = findViewById(R.id.get_password);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        txt_getemail = findViewById(R.id.txt_getemail);
        txt_getcode = findViewById(R.id.txt_getcode);
        txt_getpass = findViewById(R.id.txt_getpass);

        btn_getemail = findViewById(R.id.btn_getemail);
        btn_getcode = findViewById(R.id.btn_getcode);
        btn_getpass = findViewById(R.id.btn_getpass);

        btn_getemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_getemail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "لطفا ایمیل خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(RegisterActivity.this)) {
                        Toast.makeText(RegisterActivity.this,"لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        check_email_exist();
                    }
                }
            }
        });

        btn_getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_code_from_shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SharedContract.Code, null);
                if (txt_getcode.getText().toString().equals(get_code_from_shared)) {
                    get_code.setVisibility(View.GONE);
                    get_password.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(RegisterActivity.this, "کد تایید وارد شده اشتباه می باشد", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_getpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_getpass.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "لطفا کلمه عبور خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else if (txt_getpass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "کلمه عبور شما باید حداقل 6 عدد باشد", Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Password,txt_getpass.getText().toString()).apply();
                    if (!NetTest.yes(RegisterActivity.this)) {
                        Toast.makeText(RegisterActivity.this,"لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else  {
                        register_user();
                    }
                }
            }
        });
    }

    public void sendEmail() {
        String httpurl = HttpUrl.email;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        get_email.setVisibility(View.GONE);
                        get_code.setVisibility(View.VISIBLE);
//                        if (response.equals("1")){
//
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "متاسفانه خطایی در ارسال ایمیل اتفاق افتاده است ، لطفا بعدا تلاش نمایید", Toast.LENGTH_SHORT).show();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "متاسفانه خطایی در ارسال ایمیل اتفاق افتاده است ، لطفا بعدا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Random r = new Random();
                randomNumber = r.nextInt(9999);
                String random_key = String.valueOf(randomNumber);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Code, random_key).apply();
                Log.d(TAG, "onClick: " + randomNumber);
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", txt_getemail.getText().toString());
                params.put("subject", "ارسال کد تایید");
                params.put("body", "کد تایید برای شما" + " : " + random_key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void register_user() {
        String httpurl = HttpUrl.url + "user";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")){
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Register_OK, "yes").apply();
                            Toast.makeText(RegisterActivity.this, "حساب کاربری با موفقیت ایجاد شد", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {

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
                String get_email_from_shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SharedContract.Email, null);
                String get_password_from_shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SharedContract.Password, null);
                String get_tokenid_from_shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SharedContract.Token_Id, null);
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "0");
                params.put("email", get_email_from_shared);
                params.put("password", get_password_from_shared);
                params.put("image", "0");
                params.put("tokenid", get_tokenid_from_shared);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void check_email_exist(){
        String httpurl = HttpUrl.url + "user/email";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")){
                            final Dialog dialog = new Dialog(RegisterActivity.this);
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("کاربری با این ایمیل قبلا ثبت نام نموده است. در صورتیکه این ایمیل متعلق به شماست با وارد نمودن کلمه عبور وارد حساب کاربری شوید");
                            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            dialog.show();
                        } else {
                            String get_email_from_txt = txt_getemail.getText().toString();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Email, get_email_from_txt).apply();
                            sendEmail();
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

}
