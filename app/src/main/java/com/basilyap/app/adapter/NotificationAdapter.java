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
import com.basilyap.app.model.Notification;
import com.basilyap.app.model.UnitBase;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private ArrayList<Notification> os_version;
    private Context context;
    public static final String TAG = MainActivity.TAG;

    public NotificationAdapter(ArrayList<Notification> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Notification notification = os_version.get(position);
        holder.title.setText(notification.getTitle());
        holder.text.setText(notification.getText());
        holder.date.setText(notification.getDate());


//        holder.btnUnit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UnitShowActivity.class);
//                intent.putExtra("unit_id" , holder.unit_id);
//                intent.putExtra("project_id" , holder.project_id);
//                Log.d(TAG, "onClick: " + holder.unit_id);
//                intent.putExtra("name" , unitBase.getName());
//                intent.putExtra("type" , unitBase.getType());
//                intent.putExtra("region" , unitBase.getRegion());
//                intent.putExtra("price" , String.valueOf(unitBase.getPrice()));
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return os_version.size();
    }


    public void setFilter(ArrayList<Notification> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, text, date;
        LinearLayout btn_item;


        public ViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            btn_item = itemView.findViewById(R.id.btn_item);
        }
    }

}
