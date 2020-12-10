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
import com.example.myapplication.info.CodyInfo;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CodyAdapter extends RecyclerView.Adapter<CodyAdapter.CustomViewHolder> {

    private ArrayList<CodyInfo> arrayList;
    private Context context;
    private CodyAdapter.OnListItemSelectedInterface mListener;
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    public CodyAdapter(ArrayList<CodyInfo> arrayList, Context context, OnListItemSelectedInterface listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = listener;

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
        holder.Did.setText(arrayList.get(position).getDid());
        holder.Category.setText(arrayList.get(position).getCategory());
        holder.textviewcody.setText(arrayList.get(position).getTitle());
        holder.textviewcodycon.setText(arrayList.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewcody, textviewcodycon;
        public TextView Did;
        public TextView Category;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.Did =itemView.findViewById(R.id.Did);
            this.Category =itemView.findViewById(R.id.Category);
            this.textviewcody=itemView.findViewById(R.id.textviewcody);
            this.textviewcodycon=itemView.findViewById(R.id.textviewcodycon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });

        }
    }
}
