package com.basilyap.app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.basilyap.app.utils.SharedContract;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Profile extends Fragment {

    public static final String TAG = MainActivity.TAG;
    CardView btn_login, btn_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        String get_tokenid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SharedContract.Token_Id,null);
        Log.d(TAG, "onCreateView: " + get_tokenid);

        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
