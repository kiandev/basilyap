package com.basilyap.app.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.activity.RegisterActivity;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Profile extends Fragment {

    public static final String TAG = MainActivity.TAG;
    CardView btn_login, btn_register;
    LinearLayout login_line, main_line;
    AppCompatEditText txt_getemail, txt_getpass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        txt_getemail = view.findViewById(R.id.txt_getemail);
        txt_getpass = view.findViewById(R.id.txt_getpass);

        String get_tokenid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Token_Id, null);
        Log.d(TAG, "onCreateView: " + get_tokenid);

        login_line = view.findViewById(R.id.login_line);
        main_line = view.findViewById(R.id.main_line);

        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_getemail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "لطفا ایمیل خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else if (txt_getpass.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "لطفا کلمه عبور خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetTest.yes(getActivity())) {
                        Toast.makeText(getActivity(),"لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        login();
                    }
                }
            }
        });

        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        String get_register_ok_from_shared = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Register_OK, "no");
        if (get_register_ok_from_shared.equals("yes")) {
            login_line.setVisibility(View.GONE);
            main_line.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    public void login() {
        String httpurl = HttpUrl.url + "user/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")) {
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Register_OK, "yes").apply();
                            Toast.makeText(getActivity(), "با موفقیت وارد حساب کاربری شدید", Toast.LENGTH_SHORT).show();
                            login_line.setVisibility(View.GONE);
                            main_line.setVisibility(View.VISIBLE);
                        } else {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                            TextView text = dialog.findViewById(R.id.text);
                            dialog.setCancelable(false);
                            text.setText("متاسفانه کاربری با این مشخصات یافت نشد");
                            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
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
                String getemail_from_txt = txt_getemail.getText().toString();
                String getpass_from_txt = txt_getpass.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", getemail_from_txt);
                params.put("password", getpass_from_txt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
