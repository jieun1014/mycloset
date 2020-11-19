package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CommentReadInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ReadBoardInfo;

import java.util.ArrayList;

public class CommentLoadAdapter extends RecyclerView.Adapter<CommentLoadAdapter.CustomViewHolder> {

    private ArrayList<CommentReadInfo> arrayList;
    private Context context;
    private int pos;
    MainActivity activity;

    public CommentLoadAdapter(ArrayList<CommentReadInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_commentitem, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.CommentWriter.setText(arrayList.get(position).getCommentWriter());
        holder.CommentContent.setText(arrayList.get(position).getCommentContent());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView CommentWriter;
        public TextView CommentContent;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.CommentWriter =itemView.findViewById(R.id.CommentWriter);
            this.CommentContent =itemView.findViewById(R.id.CommentContent);

        }

    }

}
