package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.basilyap.app.classes.fcm_to_panel;
import com.basilyap.app.model.Chat;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.TAG;
    ImageView btnBack;
    private String unit_id, unit_name;
    TextView txt_title;
    AppCompatEditText txt_gettext;
    CardView btn_send;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        progressDialog = new ProgressDialog(ChatActivity.this);

        txt_title = findViewById(R.id.txt_title);
        txt_gettext = findViewById(R.id.txt_gettext);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        unit_id = intent.getStringExtra("unit_id");
        unit_name = intent.getStringExtra("unit_name");

        txt_title.setText("شما در حال پرسش سوال درباره واحد شماره " + unit_name + " می باشید");

        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_gettext.getText().toString().trim().isEmpty()){
                    Toast.makeText(ChatActivity.this, "لطفا سوال خود را وارد نمایید سپس دکمه ارسال را بفشارید", Toast.LENGTH_SHORT).show();
                } else if (txt_gettext.getText().length() < 10){
                    Toast.makeText(ChatActivity.this, "متن وارد شده کوتاه می باشد لطفا متن خود را کامل وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(ChatActivity.this)) {
                        Toast.makeText(ChatActivity.this,"لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_send.setEnabled(false);
                        btn_send.setClickable(false);
                        send_question();
                    }
                }
            }
        });

    }

    public void send_question(){
        progressDialog.setMessage("در حال ارسال ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String httpurl = HttpUrl.url + "chat";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")){
                            progressDialog.dismiss();

                            try {
                                fcm_to_panel sendnotif = new fcm_to_panel();
                                sendnotif.sendNotification("/topics/UnitQuestion", "پرسش جدید", "یک پرسش جدید درباره واحد شماره " + unit_name + " " + "مطرح شده است");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            final Dialog dialog = new Dialog(ChatActivity.this);
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("سوال شما با موفقیت ارسال گردید ، لطفا منتظر پاسخگویی از سوی کارشناسان ما باشید . شما می توانید از قسمت پروفایل من و از بخش آرشیو پیام های ارسالی پاسخ سوالات خود را مرور نمایید . باتشکر ...!");
                            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            dialog.show();
                        }  else {
                            progressDialog.dismiss();
                            Toast.makeText(ChatActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                            btn_send.setEnabled(true);
                            btn_send.setClickable(true);                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ChatActivity.this, "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        btn_send.setEnabled(true);
                        btn_send.setClickable(true);                       }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                String get_email_from_shared = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this).getString(SharedContract.Email,"");
                String get_text_from_txt = txt_gettext.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", get_email_from_shared);
                params.put("title", unit_id);
                params.put("question", get_text_from_txt);
                params.put("answer", "0");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
