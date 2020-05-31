package com.basilyap.app.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.basilyap.app.R;
import com.basilyap.app.classes.GlideApp;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class WeblogShowActivity extends AppCompatActivity {

    TextView txt_text, txt_title, txt_date;
    ImageView btn_back, image;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weblog_show);

        progressbar = findViewById(R.id.progressbar);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_date = findViewById(R.id.date);
        txt_title = findViewById(R.id.title);
        txt_text = findViewById(R.id.text);
        image = findViewById(R.id.image);

        Intent intent = getIntent();
        String get_title = intent.getStringExtra("title");
        String get_text = intent.getStringExtra("text");
        String get_date = intent.getStringExtra("date");
        String get_image = intent.getStringExtra("image");

        txt_title.setText(get_title);
        txt_text.setText(get_text);
        txt_date.setText(get_date);

        GlideApp
                .with(WeblogShowActivity.this)
                .load(get_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);

    }
}
