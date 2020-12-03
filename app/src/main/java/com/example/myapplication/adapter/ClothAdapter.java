package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.ClothReadInfo;

import java.util.ArrayList;

public class ClothAdapter extends RecyclerView.Adapter<ClothAdapter.CustomViewHolder> {
    private ArrayList<ClothReadInfo> arrayList;
    private Context context;

    private OnListItemSelectedInterface nListener;
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    public ClothAdapter(ArrayList<ClothReadInfo> arrayList, Context context, OnListItemSelectedInterface listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.nListener = listener;
    }

    @NonNull
    @Override
    public ClothAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_clothitem, parent, false);
        ClothAdapter.CustomViewHolder holder = new ClothAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClothAdapter.CustomViewHolder holder, final int position) {
        Glide.with(context).load(arrayList.get(position).getImage()).centerCrop().override(500, 500).into(holder.imageView);
        holder.Did.setText(arrayList.get(position).getDid());
        holder.Title.setText(arrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView Did;
        public TextView Title;
        public ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.Did = itemView.findViewById(R.id.didView);
            this.Title = itemView.findViewById(R.id.clothNameText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }

    }
}
