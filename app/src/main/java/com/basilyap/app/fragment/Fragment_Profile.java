package com.basilyap.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.basilyap.app.activity.AccountActivity;
import com.basilyap.app.activity.ChatListActivity;
import com.basilyap.app.activity.ForgetActivity;
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.activity.OpinionActivity;
import com.basilyap.app.activity.PasswordActivity;
import com.basilyap.app.activity.RegisterActivity;
import com.basilyap.app.classes.MyFirebaseMessagingService;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Profile extends Fragment {

    public static final String TAG = MainActivity.TAG;
    String tokenId;
    CardView btn_login, btn_register;
    LinearLayout login_line, main_line;
    AppCompatEditText txt_getemail, txt_getpass;
    TextView txt_profile_name, btnForget;
    LinearLayout btnAccount, btnExit, btnPassword, btnChat;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        String get_tokenid_from_shared = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Token_Id,"0");
        if (get_tokenid_from_shared.equals("0")){
            subscribeUserToParse();
        }

        progressDialog = new ProgressDialog(getActivity());

        txt_getemail = view.findViewById(R.id.txt_getemail);
        txt_getpass = view.findViewById(R.id.txt_getpass);

        txt_profile_name = view.findViewById(R.id.txt_profile_name);

        final String get_tokenid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Token_Id, null);
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
                        Toast.makeText(getActivity(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_login.setEnabled(false);
                        btn_login.setClickable(false);
                        try {
                            login();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

        btnForget = view.findViewById(R.id.btnForget);
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ForgetActivity.class));
            }
        });

        btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatListActivity.class));
            }
        });

        btnAccount = view.findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }
        });

        btnExit = view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog_question);
                TextView text = dialog.findViewById(R.id.text);
                dialog.setCancelable(false);
                text.setText("آیا مطمئین هستید که میخواهید از حساب کاربری خارج شوید؟");
                Button yesButton = dialog.findViewById(R.id.dialogButtonYes);
                Button noButton = dialog.findViewById(R.id.dialogButtonNo);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Register_OK, "no").apply();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Email, "").apply();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Password, "").apply();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Name, "").apply();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Phone, "").apply();
                        Toast.makeText(getActivity(), "با موفقیت از حساب کاربری خارج شدید", Toast.LENGTH_SHORT).show();
                        txt_profile_name.setText("");
                        login_line.setVisibility(View.VISIBLE);
                        main_line.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btnPassword = view.findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PasswordActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        String get_register_ok_from_shared = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Register_OK, "no");
        if (get_register_ok_from_shared.equals("yes")) {
            String get_email_from_shared = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Email, "");
            txt_profile_name.setText(get_email_from_shared);
            login_line.setVisibility(View.GONE);
            main_line.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    public void login() {
        progressDialog.setMessage("در حال بررسی ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String httpurl = HttpUrl.url + "user/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("[]")) {
                            progressDialog.dismiss();
                            btn_login.setEnabled(true);
                            btn_login.setClickable(true);
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
                        } else {
                            progressDialog.dismiss();
                            btn_login.setEnabled(true);
                            btn_login.setClickable(true);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        change_account_detail();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            try {
                                JSONArray jsonarray = new JSONArray(response);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String get_name = jsonobject.getString("name");
                                    String get_email = jsonobject.getString("email");
                                    String get_password = jsonobject.getString("password");
                                    String get_phone = jsonobject.getString("phone");
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Name, get_name).apply();
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Email, get_email).apply();
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Password, get_password).apply();
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Phone, get_phone).apply();
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Register_OK, "yes").apply();
                                    try {
                                        hideSoftKeyboard();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getActivity(), "با موفقیت وارد حساب کاربری شدید", Toast.LENGTH_SHORT).show();
                                    txt_getemail.setText("");
                                    txt_getpass.setText("");
                                    txt_profile_name.setText(get_email);
                                    login_line.setVisibility(View.GONE);
                                    main_line.setVisibility(View.VISIBLE);
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
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "متاسفانه خطایی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                        btn_login.setEnabled(true);
                        btn_login.setClickable(true);
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

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public void change_account_detail() {
        String httpurl = HttpUrl.url + "user/update_tokenid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("1")) {
                            Log.d(TAG, "onResponse: " + "tokenid was updated");
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + "error");
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String get_email_from_txt = txt_getemail.getText().toString();
                String get_tokenid_from_shared = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Token_Id, "");
                params.put("email", get_email_from_txt);
                params.put("tokenid", get_tokenid_from_shared);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void subscribeUserToParse() {
        tokenId = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(tokenId)) {
            Intent intent = new Intent(getActivity(), MyFirebaseMessagingService.class);
            getActivity().startService(intent);
            return;
        }
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SharedContract.Token_Id, tokenId).apply();
        Log.d(TAG, "subscribeUserToParse: " + tokenId);
    }

}
