package com.basilyap.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.basilyap.app.R;
import com.basilyap.app.activity.MainActivity;
import com.basilyap.app.model.Chat;
import com.basilyap.app.model.Notification;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    public static final String TAG = MainActivity.TAG;
    private ArrayList<Chat> os_version;
    private Context context;

    public ChatAdapter(ArrayList<Chat> arrayList) {
        os_version = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Chat chat = os_version.get(position);
        holder.question.setText(chat.getQuestion());
        if (chat.getAnswer().equals("0")) {
            holder.answer.setText("متاسفانه هنوز پاسخی از سوی کارشناسان دریافت نشده است");
        } else {
            holder.answer.setText(chat.getAnswer());
            holder.answer.setTextColor(Color.parseColor("#4CAF50"));
        }


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


    public void setFilter(ArrayList<Chat> arrayList) {
        os_version = new ArrayList<>();
        os_version.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView question, answer;
        LinearLayout btn_item;


        public ViewHolder(View view) {
            super(view);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            btn_item = itemView.findViewById(R.id.btn_item);
        }
    }

}
