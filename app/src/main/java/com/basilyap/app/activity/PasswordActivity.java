package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
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

public class PasswordActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    AppCompatEditText txt_old, txt_new;
    CardView btn_ok;
    ImageView btn_back;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        progressDialog = new ProgressDialog(PasswordActivity.this);

        txt_old = findViewById(R.id.txt_getold);
        txt_new = findViewById(R.id.txt_getnew);

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
                String get_password_from_shared = PreferenceManager.getDefaultSharedPreferences(PasswordActivity.this).getString(SharedContract.Password, "");
                Log.d(TAG, "onClick: " + get_password_from_shared);
                if (txt_old.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PasswordActivity.this, "لطفا کلمه عبور فعلی را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else if (txt_new.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PasswordActivity.this, "لطفا کلمه عبور جدید را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else if (!txt_old.getText().toString().equals(get_password_from_shared)) {
                    Toast.makeText(PasswordActivity.this, "کلمه عبور فعلی اشتباه می باشد", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(PasswordActivity.this)) {
                        Toast.makeText(PasswordActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_ok.setEnabled(false);
                        btn_ok.setClickable(false);
                        change_account_password();
                    }
                }
            }
        });
    }

    private void change_account_password() {
        progressDialog.setMessage("در حال ارسال ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String httpurl = HttpUrl.url + "user/change_pass";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")) {
                            progressDialog.dismiss();
                            final Dialog dialog = new Dialog(PasswordActivity.this);
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("کلمه عبور با موفقیت بروزآوری شد");
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
                            Toast.makeText(PasswordActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                            btn_ok.setEnabled(true);
                            btn_ok.setClickable(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(PasswordActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        btn_ok.setEnabled(true);
                        btn_ok.setClickable(true);
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String get_email_from_shared = PreferenceManager.getDefaultSharedPreferences(PasswordActivity.this).getString(SharedContract.Email, "");
                String get_password_from_txt = txt_new.getText().toString();
                params.put("email", get_email_from_shared);
                params.put("password", get_password_from_txt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
