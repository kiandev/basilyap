package com.basilyap.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.basilyap.app.R;
import com.basilyap.app.fragment.Fragment_Home;
import com.basilyap.app.fragment.Fragment_Profile;
import com.basilyap.app.fragment.Fragment_Units;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private int currentId;
    private Boolean exit = false;
    public static final String TAG = "basilyap_tag";
    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorBN_status));
//            getWindow().setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorBN_status));
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }


        bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setSelectedItemId(R.id.item_home);

        fragmentManager = getSupportFragmentManager();
        fragment = new Fragment_Home();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content, fragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (currentId == item.getItemId()) {
                    return false;
                }
                currentId = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.item_home:
                        fragment = new Fragment_Home();
                        break;
                    case R.id.item_unit:
                        fragment = new Fragment_Units();
                        break;
                    case R.id.item_vitrin:
//                        fragment = new Admin_Fragment_Advisor();
                        break;
                    case R.id.item_profile:
                        fragment = new Fragment_Profile();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, fragment).commit();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                return true;
            }
        });
    }

    public void btnAbout_click (View view){
        startActivity(new Intent(MainActivity.this,AboutActivity.class));
    }

    public void btnComunication_click (View view){
        startActivity(new Intent(MainActivity.this,ComunicationActivity.class));
    }
}
