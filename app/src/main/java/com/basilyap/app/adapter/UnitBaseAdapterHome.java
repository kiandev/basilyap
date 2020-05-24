package com.basilyap.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.basilyap.app.R;
import com.basilyap.app.classes.GlideApp;
import com.basilyap.app.model.UnitBase;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class UnitBaseAdapterHome extends RecyclerView.Adapter<UnitBaseAdapterHome.ViewHolder> {


    private ArrayList<UnitBase> os_version;
    private Context context;

    public UnitBaseAdapterHome(ArrayList<UnitBase> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit_home, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UnitBase unitBase = os_version.get(position);
        holder.name.setText("کد : " + unitBase.getName());
        holder.price.setText(String.valueOf(unitBase.getPrice()) + " لیر");

        GlideApp
                .with(context)
                .load(unitBase.getImage())
                .placeholder(context.getResources().getDrawable(R.drawable.basifood_logo_placeholder))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);

//        holder.btnFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ShowFoodActivity.class);
//                intent.putExtra("name" , food.getName());
//                intent.putExtra("image" , food.getImage());
//                intent.putExtra("text" , food.getText());
//                intent.putExtra("price" , String.valueOf(food.getPrice()));
//                context.startActivity(intent);
//            }
//        });
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

        TextView name, price;
        ImageView image;
        LinearLayout btnUnit;


        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            btnUnit = itemView.findViewById(R.id.btnUnit);
        }
    }

}
