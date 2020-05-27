package com.basilyap.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.adapter.UnitBaseAdapter;
import com.basilyap.app.adapter.UnitBaseAdapterHome;
import com.basilyap.app.model.UnitBase;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_Units extends Fragment {
    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager_unit;
    ArrayList<UnitBase> os_version_unitbase = new ArrayList<>();
    UnitBaseAdapter mAdapter_unitbase;
    LinearLayout no_internet, progressbar;
    Button btn_again;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_units, container, false);
        no_internet = view.findViewById(R.id.no_internet);
        progressbar = view.findViewById(R.id.progressbar);
        btn_again = view.findViewById(R.id.btn_again);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        layoutManager_unit = ((LinearLayoutManager) recyclerView.getLayoutManager());

        mAdapter_unitbase = new UnitBaseAdapter(os_version_unitbase);


        if (!NetTest.yes(getActivity())){
//            Toast.makeText(getActivity(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
        } else {
            no_internet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    loadUnitBase();
                }
            });
        }

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(getActivity())) {
                    Toast.makeText(getActivity(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    no_internet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadUnitBase();
                        }
                    });
                }
            }
        });

        return view;
    }

    private void loadUnitBase() {
        String URL = HttpUrl.url + "unit/base";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject unitbase = array.getJSONObject(i);
                                os_version_unitbase.add(new UnitBase(
                                        unitbase.getInt("id"),
                                        unitbase.getString("unit_id"),
                                        unitbase.getString("name"),
                                        unitbase.getInt("price"),
                                        unitbase.getString("image"),
                                        unitbase.getString("type"),
                                        unitbase.getString("region"),
                                        unitbase.getString("project_id"),
                                        unitbase.getInt("special")
                                ));
                            }
                            progressbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(mAdapter_unitbase);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
}
