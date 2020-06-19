package com.basilyap.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.basilyap.app.R;
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.activity.UnitShowActivity;
import com.basilyap.app.classes.GlideApp;
import com.basilyap.app.model.UnitBase;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class UnitBaseAdapter extends RecyclerView.Adapter<UnitBaseAdapter.ViewHolder> {


    public static final String TAG = MainActivity.TAG;
    private ArrayList<UnitBase> os_version;
    private Context context;

    public UnitBaseAdapter(ArrayList<UnitBase> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UnitBase unitBase = os_version.get(position);
        holder.unit_id = unitBase.getUnit_id();
        holder.project_id = unitBase.getProject_id();
        Log.d(TAG, "onBindViewHolder: " + holder.unit_id);
        holder.name.setText("کد : " + unitBase.getName());
        holder.region.setText(unitBase.getRegion());
        holder.price.setText(String.valueOf(unitBase.getPrice()) + " لیر");

        GlideApp
                .with(context)
                .load(unitBase.getImage())
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
                Intent intent = new Intent(context, UnitShowActivity.class);
                intent.putExtra("unit_id", holder.unit_id);
                intent.putExtra("project_id", holder.project_id);
                Log.d(TAG, "onClick: " + holder.unit_id);
                intent.putExtra("name", unitBase.getName());
                intent.putExtra("type", unitBase.getType());
                intent.putExtra("region", unitBase.getRegion());
                intent.putExtra("price", String.valueOf(unitBase.getPrice()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<UnitBase> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String unit_id, project_id;
        TextView name, region, price;
        ImageView image;
        RelativeLayout btnItem;
        ProgressBar progressbar;


        public ViewHolder(View view) {
            super(view);
            progressbar = itemView.findViewById(R.id.progressbar);
            name = itemView.findViewById(R.id.name);
            region = itemView.findViewById(R.id.region);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            btnItem = itemView.findViewById(R.id.btnItem);
        }
    }

}
