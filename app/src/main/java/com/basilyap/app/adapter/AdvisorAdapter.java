package com.basilyap.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.basilyap.app.activity.UnitShowActivity;
import com.basilyap.app.classes.GlideApp;
import com.basilyap.app.model.Advisor;
import com.basilyap.app.model.UnitBase;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class AdvisorAdapter extends RecyclerView.Adapter<AdvisorAdapter.ViewHolder> {


    private ArrayList<Advisor> os_version;
    private Context context;
    public static final String TAG = MainActivity.TAG;

    public AdvisorAdapter(ArrayList<Advisor> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advisor, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Advisor advisor = os_version.get(position);
        holder.id = String.valueOf(advisor.getId());
        holder.name.setText(advisor.getPersian() + " / " + advisor.getEnglish());
        holder.title.setText(advisor.getTitle());

        GlideApp
                .with(context)
                .load(advisor.getImage())
                .circleCrop()
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
                Intent intent = new Intent(context, AdvisorShowActivity.class);
                intent.putExtra("id" , holder.id);
                intent.putExtra("persian" , advisor.getPersian());
                intent.putExtra("english" , advisor.getEnglish());
                intent.putExtra("title" , advisor.getTitle());
                intent.putExtra("email" , advisor.getEmail());
                intent.putExtra("phone" , advisor.getPhone());
                intent.putExtra("image" , advisor.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Advisor> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        TextView name, title;
        ImageView image;
        RelativeLayout btnItem;
        ProgressBar progressbar;


        public ViewHolder(View view) {
            super(view);
            progressbar = itemView.findViewById(R.id.progressbar);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            btnItem = itemView.findViewById(R.id.btnItem);
        }
    }

}
