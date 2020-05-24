package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.work.impl.model.Preference;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.utils.HttpUrl;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        get_email = findViewById(R.id.get_email);
        get_code = findViewById(R.id.get_code);
        get_password = findViewById(R.id.get_password);


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
                    String get_email_from_txt = txt_getemail.getText().toString();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Email, get_email_from_txt).apply();
                    sendEmail();
                    get_email.setVisibility(View.GONE);
                    get_code.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_code_from_shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SharedContract.Code,null);
                if (txt_getcode.getText().toString().equals(get_code_from_shared)) {
                    get_code.setVisibility(View.GONE);
                    get_password.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(RegisterActivity.this, "کد تایید وارد شده اشتباه می باشد", Toast.LENGTH_SHORT).show();
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Random r = new Random();
                randomNumber = r.nextInt(9999);
                String random_key = String.valueOf(randomNumber);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SharedContract.Code,random_key).apply();
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
}
