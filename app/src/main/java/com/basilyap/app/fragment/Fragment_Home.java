package com.basilyap.app.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.activity.NotificationActivity;
import com.basilyap.app.activity.OpinionActivity;
import com.basilyap.app.activity.TeamActivity;
import com.basilyap.app.adapter.UnitBaseAdapterHome;
import com.basilyap.app.adapter.UnitBaseAdapterHomeSpecial;
import com.basilyap.app.classes.GlideApp;
import com.basilyap.app.model.UnitBase;
import com.basilyap.app.model.UnitBaseSpecial;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_Home extends Fragment {

    public static final String TAG = MainActivity.TAG;
    RecyclerView recyclerView_unit, recyclerView_special;
    LinearLayoutManager layoutManager_unit, layoutManager_unit_special;
    ArrayList<UnitBase> os_version_unitbase = new ArrayList<>();
    ArrayList<UnitBaseSpecial> os_version_unitbase_special = new ArrayList<>();
    UnitBaseAdapterHome mAdapter_unitbase;
    UnitBaseAdapterHomeSpecial mAdapter_unitbase_special;
    ProgressBar pb_unitbase, pb_unitspecial;
    ScrollView main_line;
    LinearLayout no_internet;
    Button btn_again;
    LinearLayout btnMessage, btnTeam, btnSend;
    DrawerLayout drawerLayout;
    ImageView btnMenu, btnNotification;
    ImageView top_banner;
    ProgressBar pb_top_banner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        top_banner = view.findViewById(R.id.top_banner);
        pb_top_banner = view.findViewById(R.id.pb_top_banner);
        String get_url_top_banner = HttpUrl.top_banner;

        GlideApp
                .with(getActivity())
                .load(get_url_top_banner)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pb_top_banner.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pb_top_banner.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(top_banner);


        drawerLayout = view.findViewById(R.id.drawer_layout);

        btnNotification = view.findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });

        btnMenu = view.findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        no_internet = view.findViewById(R.id.no_internet);
        main_line = view.findViewById(R.id.main_line);
        btn_again = view.findViewById(R.id.btn_again);

        btnMessage = view.findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OpinionActivity.class));
            }
        });

        btnTeam = view.findViewById(R.id.btnTeam);
        btnTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TeamActivity.class));
            }
        });

        btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"اپلیکیشن بازیل یاپ را با دیگران به اشتراک بگذارید : https://play.google.com/store/apps/details?id=com.android.chrome");
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(getActivity())) {
                    Toast.makeText(getActivity(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    no_internet.setVisibility(View.GONE);
                    main_line.setVisibility(View.VISIBLE);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadUnitBase();
                        }
                    });
                }
            }
        });

        pb_unitbase = view.findViewById(R.id.pb_unitbase);
        pb_unitspecial = view.findViewById(R.id.pb_unitspecial);
        recyclerView_unit = view.findViewById(R.id.recyclerView_unit);
        recyclerView_special = view.findViewById(R.id.recyclerView_special);


        recyclerView_unit.setHasFixedSize(true);
        recyclerView_unit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        layoutManager_unit = ((LinearLayoutManager) recyclerView_unit.getLayoutManager());

        recyclerView_special.setHasFixedSize(true);
        recyclerView_special.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        layoutManager_unit_special = ((LinearLayoutManager) recyclerView_special.getLayoutManager());

        mAdapter_unitbase = new UnitBaseAdapterHome(os_version_unitbase);
        mAdapter_unitbase_special = new UnitBaseAdapterHomeSpecial(os_version_unitbase_special);

        if (!NetTest.yes(getActivity())) {
//            Toast.makeText(getActivity(), "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
        } else {
            no_internet.setVisibility(View.GONE);
            main_line.setVisibility(View.VISIBLE);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    loadUnitBase();
                }
            });
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    loadUnitBaseSpecial();
                }
            });
        }

        return view;
    }

    private void loadUnitBase() {
        String URL = HttpUrl.url + "unit/base_limit";
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
                            pb_unitbase.setVisibility(View.GONE);
                            recyclerView_unit.setVisibility(View.VISIBLE);
                            recyclerView_unit.setAdapter(mAdapter_unitbase);
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

    private void loadUnitBaseSpecial() {
        String URL = HttpUrl.url + "unit/base_special";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse2: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject unitbasespecial = array.getJSONObject(i);
                                os_version_unitbase_special.add(new UnitBaseSpecial(
                                        unitbasespecial.getInt("id"),
                                        unitbasespecial.getString("unit_id"),
                                        unitbasespecial.getString("name"),
                                        unitbasespecial.getInt("price"),
                                        unitbasespecial.getString("image"),
                                        unitbasespecial.getString("type"),
                                        unitbasespecial.getString("region"),
                                        unitbasespecial.getString("project_id"),
                                        unitbasespecial.getInt("special")
                                ));
                            }
                            pb_unitspecial.setVisibility(View.GONE);
                            recyclerView_special.setVisibility(View.VISIBLE);
                            recyclerView_special.setAdapter(mAdapter_unitbase_special);
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
