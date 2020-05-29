package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basilyap.app.R;
import com.basilyap.app.adapter.SlidingImage_Adapter;
import com.basilyap.app.model.UnitBase;
import com.basilyap.app.model.UnitImage;
import com.basilyap.app.utils.HttpUrl;
import com.basilyap.app.utils.NetTest;
import com.basilyap.app.utils.SharedContract;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class UnitShowActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public static final String TAG = MainActivity.TAG;
    ArrayList<UnitImage> os_version_unitimage = new ArrayList<>();
    private JSONObject jsonObject;
    private SlidingImage_Adapter mAdapter_unitiamge;
    CirclePageIndicator indicator;
    TextView txt_name, txt_type, txt_region, txt_price;
    TextView txt_measure, txt_room, txt_floor, txt_status;
    TextView txt_project_measure, txt_project_unit, txt_project_floor, txt_project_born;
    TextView txt_project_facility, txt_project_distance;
    String unit_id, project_id, unit_name;
    ProgressBar pb_unitadvance, pb_project;
    CardView line_unitadvance, line_project;
    LinearLayout no_internet;
    ScrollView main_line;
    Button btn_again, btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_show);

        no_internet = findViewById(R.id.no_internet);
        main_line = findViewById(R.id.main_line);
        btn_again = findViewById(R.id.btn_again);

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetTest.yes(getApplicationContext())) {
                    Toast.makeText(UnitShowActivity.this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    no_internet.setVisibility(View.GONE);
                    main_line.setVisibility(View.VISIBLE);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadUnitImage();

                        }
                    });
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadUnitAdvances();
                        }
                    });
                }

            }
        });


        pb_unitadvance = findViewById(R.id.pb_unitadvance);
        pb_project = findViewById(R.id.pb_project);
        line_unitadvance = findViewById(R.id.line_unitadvance);
        line_project = findViewById(R.id.line_project);

        mAdapter_unitiamge = new SlidingImage_Adapter(getApplicationContext(), os_version_unitimage);
        indicator = findViewById(R.id.indicator);

        txt_name = findViewById(R.id.name);
        txt_type = findViewById(R.id.type);
        txt_region = findViewById(R.id.region);
        txt_price = findViewById(R.id.price);

        mPager = findViewById(R.id.pager);

        txt_measure = findViewById(R.id.measure);
        txt_room = findViewById(R.id.room);
        txt_floor = findViewById(R.id.floor);
        txt_status = findViewById(R.id.status);

        txt_project_measure = findViewById(R.id.project_measure);
        txt_project_unit = findViewById(R.id.project_unit);
        txt_project_floor = findViewById(R.id.project_floor);
        txt_project_born = findViewById(R.id.project_born);
        txt_project_facility = findViewById(R.id.project_facility);
        txt_project_distance = findViewById(R.id.project_distance);

        Intent intent = getIntent();
        unit_id = intent.getStringExtra("unit_id");
        project_id = intent.getStringExtra("project_id");
        String get_name_from_another_activity = intent.getStringExtra("name");
        unit_name = intent.getStringExtra("name");
        String get_type_from_another_activity = intent.getStringExtra("type");
        String get_region_from_another_activity = intent.getStringExtra("region");
        String get_price_from_another_activity = intent.getStringExtra("price");

        txt_name.setText(get_name_from_another_activity);
        txt_type.setText(get_type_from_another_activity);
        txt_region.setText(get_region_from_another_activity);
        txt_price.setText(get_price_from_another_activity);

        if (!NetTest.yes(getApplicationContext())) {
//            Toast.makeText(this, "لطفا ابتدا دستگاه خود را به اینترنت متصل نمایید", Toast.LENGTH_SHORT).show();
        } else {
            no_internet.setVisibility(View.GONE);
            main_line.setVisibility(View.VISIBLE);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    loadUnitImage();

                }
            });
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    loadUnitAdvances();
                }
            });
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    loadProject();
                }
            });
        }

        btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_register_form_share = PreferenceManager.getDefaultSharedPreferences(UnitShowActivity.this).getString(SharedContract.Register_OK, "no");
                if (get_register_form_share.equals("no")) {
                    final Dialog dialog = new Dialog(UnitShowActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);
//                            dialog.setTitle("Title...");
                    TextView text = dialog.findViewById(R.id.text);
                    dialog.setCancelable(false);
                    text.setText("جهت ارسال سوال لطفا ابتدا از طریق بخش پروفایل کاربری وارد حساب کاربری خود شوید");
                    Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Intent intent = new Intent(UnitShowActivity.this, ChatActivity.class);
                    intent.putExtra("unit_id", unit_id);
                    intent.putExtra("unit_name", unit_name);
                    startActivity(intent);
                }
            }
        });

    }

    private void loadUnitImage() {
        String URL = HttpUrl.url + "unit/image";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d(TAG, "onResponse: " + response);
                            for (int i = 0; i < array.length(); i++) {
                                jsonObject = array.getJSONObject(i);
                                os_version_unitimage.add(new UnitImage(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("unit_id"),
                                        jsonObject.getString("link")
                                ));
                            }
                            slider_image();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UnitShowActivity.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(UnitShowActivity.this);
        requestQueue.add(stringRequest);

    }

    public void slider_image() {
        mPager.setAdapter(mAdapter_unitiamge);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES = os_version_unitimage.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 5000, 5000);
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
////                                    currentPage = position;
//            }
//            @Override
//            public void onPageScrolled(int pos, float arg1, int arg2) {
//            }
//            @Override
//            public void onPageScrollStateChanged(int pos) {
//            }
//        });
    }

    public void loadUnitAdvances() {
        String httpurl = HttpUrl.url + "unit/advance_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String get_measure = jsonobject.getString("measure");
                                String get_room = jsonobject.getString("room");
                                String get_floor = jsonobject.getString("floor");
                                String get_status = jsonobject.getString("status");

                                txt_measure.setText(get_measure);
                                txt_room.setText(get_room);
                                txt_floor.setText(get_floor);
                                txt_status.setText(get_status);

                                pb_unitadvance.setVisibility(View.GONE);
                                line_unitadvance.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UnitShowActivity.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d(TAG, "getParams: " + unit_id);
                params.put("unit_id", unit_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UnitShowActivity.this);
        requestQueue.add(stringRequest);
    }

    public void loadProject() {
        String httpurl = HttpUrl.url + "project/detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String get_measure = jsonobject.getString("measure");
                                String get_unit = jsonobject.getString("unit");
                                String get_floor = jsonobject.getString("floor");
                                String get_born = jsonobject.getString("born");
                                String get_facility = jsonobject.getString("facility");
                                String get_distance = jsonobject.getString("distance");

                                txt_project_measure.setText(get_measure);
                                txt_project_unit.setText(get_unit);
                                txt_project_floor.setText(get_floor);
                                txt_project_born.setText(get_born);
                                txt_project_facility.setText(get_facility);
                                txt_project_distance.setText(get_distance);

                                pb_project.setVisibility(View.GONE);
                                line_project.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UnitShowActivity.this, "متاسفانه خطایی نامشخصی رخ داده است ، لطفا بعدا مجددا تلاش نمایید", Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d(TAG, "getParams: " + project_id);
                params.put("project_id", project_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UnitShowActivity.this);
        requestQueue.add(stringRequest);
    }

}
