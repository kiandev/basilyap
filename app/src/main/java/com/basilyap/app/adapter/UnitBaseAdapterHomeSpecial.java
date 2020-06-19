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
import com.basilyap.app.model.UnitBaseSpecial;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class UnitBaseAdapterHomeSpecial extends RecyclerView.Adapter<UnitBaseAdapterHomeSpecial.ViewHolder> {


    public static final String TAG = MainActivity.TAG;
    private ArrayList<UnitBaseSpecial> os_version;
    private Context context;

    public UnitBaseAdapterHomeSpecial(ArrayList<UnitBaseSpecial> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit_home_special, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UnitBaseSpecial unitBaseSpecial = os_version.get(position);
        holder.unit_id = unitBaseSpecial.getUnit_id();
        holder.project_id = unitBaseSpecial.getProject_id();
        holder.name.setText("کد : " + unitBaseSpecial.getName());
        holder.price.setText(String.valueOf(unitBaseSpecial.getPrice()) + " لیر");

        GlideApp
                .with(context)
                .load(unitBaseSpecial.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);

        holder.btnUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UnitShowActivity.class);
                intent.putExtra("unit_id", holder.unit_id);
                intent.putExtra("project_id", holder.project_id);
                Log.d(TAG, "onClick: " + holder.unit_id);
                intent.putExtra("name", unitBaseSpecial.getName());
                intent.putExtra("type", unitBaseSpecial.getType());
                intent.putExtra("region", unitBaseSpecial.getRegion());
                intent.putExtra("price", String.valueOf(unitBaseSpecial.getPrice()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<UnitBaseSpecial> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        String unit_id, project_id;
        TextView name, price;
        ImageView image;
        RelativeLayout btnUnit;


        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            btnUnit = itemView.findViewById(R.id.btnUnit);
        }
    }

}
