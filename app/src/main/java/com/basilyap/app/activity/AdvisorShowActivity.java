package com.basilyap.app.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basilyap.app.R;
import com.basilyap.app.classes.GlideApp;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class AdvisorShowActivity extends AppCompatActivity {

    TextView name, title, email, phone;
    ImageView image;
    CardView btn_phone, btn_email;
    private String get_phone, get_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_show);

        name = findViewById(R.id.name);
        title = findViewById(R.id.title);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        image = findViewById(R.id.image);

        Intent intent = getIntent();
        String get_persian = intent.getStringExtra("persian");
        String get_english = intent.getStringExtra("english");
        String get_title = intent.getStringExtra("title");
        get_phone = intent.getStringExtra("phone");
        get_email = intent.getStringExtra("email");
        String get_image = intent.getStringExtra("image");

        name.setText(get_persian + " / " + get_english);
        title.setText(get_title);
        phone.setText(get_phone);
        email.setText(get_email);

        GlideApp
                .with(AdvisorShowActivity.this)
                .load(get_image)
                .circleCrop()
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        holder.progressbar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.progressbar.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);

        btn_phone = findViewById(R.id.btn_phone);
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_email = findViewById(R.id.btn_email);
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + get_email));
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "ارسال پیام به بازیل هوم");
//                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(intent);
                } catch(Exception e) {
                    Toast.makeText(AdvisorShowActivity.this, "متاسفانه اپلیکیشن مناسب جهت ارسال ایمیل یافت نشد", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }
}
