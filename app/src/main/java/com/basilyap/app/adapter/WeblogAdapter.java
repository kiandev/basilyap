package com.basilyap.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.basilyap.app.R;
import com.basilyap.app.activity.AdvisorShowActivity;
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.activity.WeblogShowActivity;
import com.basilyap.app.classes.GlideApp;
import com.basilyap.app.model.Advisor;
import com.basilyap.app.model.Weblog;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class WeblogAdapter extends RecyclerView.Adapter<WeblogAdapter.ViewHolder> {


    private ArrayList<Weblog> os_version;
    private Context context;
    public static final String TAG = MainActivity.TAG;

    public WeblogAdapter(ArrayList<Weblog> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weblog, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Weblog weblog = os_version.get(position);
        holder.id = String.valueOf(weblog.getId());
        holder.title.setText(weblog.getTitle());
        holder.text.setText(weblog.getText());
        holder.date.setText(weblog.getDate());

        GlideApp
                .with(context)
                .load(weblog.getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);

        holder.btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WeblogShowActivity.class);
                intent.putExtra("title" , weblog.getTitle());
                intent.putExtra("text" , weblog.getText());
                intent.putExtra("date" , weblog.getDate());
                intent.putExtra("image" , weblog.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Weblog> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView title, text, date;
        ImageView image;
        RelativeLayout btnItem;
        ProgressBar progressbar;


        public ViewHolder(View view) {
            super(view);
            progressbar = itemView.findViewById(R.id.progressbar);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            btnItem = itemView.findViewById(R.id.btnItem);
        }
    }

}
