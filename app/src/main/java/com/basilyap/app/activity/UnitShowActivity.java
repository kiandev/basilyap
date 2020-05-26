package com.basilyap.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UnitShowActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public static final String TAG = MainActivity.TAG;
    ArrayList<UnitBase> os_version_unitbase = new ArrayList<>();
    ArrayList<UnitImage> os_version_unitimage = new ArrayList<>();
    private JSONObject jsonObject;


    TextView txt_name, txt_type, txt_region, txt_price;

    private String[] urls = new String[] {
            "https://basilhome.com/basilyap/wp-content/uploads/2017/06/villas.jpg",
            "https://basilhome.com/basilyap/wp-content/uploads/2017/06/sleek-living-room-decor.jpg"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_show);

        txt_name = findViewById(R.id.name);
        txt_type = findViewById(R.id.type);
        txt_region = findViewById(R.id.region);
        txt_price = findViewById(R.id.price);

        Intent intent = getIntent();
        String get_name_from_another_activity = intent.getStringExtra("name");
        String get_type_from_another_activity = intent.getStringExtra("type");
        String get_region_from_another_activity = intent.getStringExtra("region");
        String get_price_from_another_activity = intent.getStringExtra("price");

        txt_name.setText(get_name_from_another_activity);
        txt_type.setText(get_type_from_another_activity);
        txt_region.setText(get_region_from_another_activity);
        txt_price.setText(get_price_from_another_activity);

        loadUnitImage();
        init();

    }

    private void init() {

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(UnitShowActivity.this,urls));
        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES = urls.length;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }
            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int pos) {
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

}
