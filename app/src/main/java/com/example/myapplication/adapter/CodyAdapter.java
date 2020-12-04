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
        Glide.with(context)
                .load(arrayList.get(position).getProfile())
//              .load("https://firebasestorage.googleapis.com/v0/b/test-ae7be.appspot.com/o/Cody%2F%EC%83%81%EC%9D%98.jpg?alt=media&token=b3b15345-6da9-48ac-952e-db30638f651c")
                .override(200,200)
                .into(holder.imageviewcodytop);
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
//                .load("https://firebasestorage.googleapis.com/v0/b/test-ae7be.appspot.com/o/Cody%2F%ED%95%98%EC%9D%98.jpg?alt=media&token=5d86800a-96ab-4eca-940f-2d659905c812")
                .override(200,200)
                .into(holder.imageviewcodybot);
        holder.textviewcody.setText(arrayList.get(position).getTitle());
        holder.textviewcody1.setText(arrayList.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageviewcodytop, imageviewcodybot, imageviewcodyhat, imageviewcodyshoe;
        TextView textviewcody, textviewcody1;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageviewcodytop = itemView.findViewById(R.id.imageviewtop);
            this.imageviewcodybot = itemView.findViewById(R.id.imageviewbot);
            this.imageviewcodyhat = itemView.findViewById(R.id.imageviewhat);
            this.imageviewcodyshoe = itemView.findViewById(R.id.imageviewshoe);
            this.textviewcody = itemView.findViewById(R.id.textviewcody);
            this.textviewcody1 = itemView.findViewById(R.id.textviewcodycon);
        }
    }
}
