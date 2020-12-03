package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.info.CodyInfo;
import com.example.myapplication.info.ReadBoardInfo;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CodyAdapter extends RecyclerView.Adapter<CodyAdapter.CustomViewHolder> {

    private ArrayList<CodyInfo> arrayList;
    private Context context;

    public CodyAdapter(ArrayList<CodyInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_codyitem,parent,false);//item연결
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {//아이템 매칭
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.imageviewcody1);
        holder.textviewcody.setText(arrayList.get(position).getId());
        holder.textviewcody1.setText(arrayList.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageviewcody1;
        TextView textviewcody, textviewcody1;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageviewcody1 = itemView.findViewById(R.id.imageviewcody1);
            this.textviewcody = itemView.findViewById(R.id.textviewcody);
            this.textviewcody1 = itemView.findViewById(R.id.textviewcodycon);
        }
    }
}
