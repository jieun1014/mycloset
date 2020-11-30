package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.info.CommentReadInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CommentLoadAdapter extends RecyclerView.Adapter<CommentLoadAdapter.CustomViewHolder> {

    private ArrayList<CommentReadInfo> arrayList;
    private Context context;
    private int pos;
    MainActivity activity;

    private OnListItemSelectedInterface nListener;
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    public CommentLoadAdapter(ArrayList<CommentReadInfo> arrayList, Context context, OnListItemSelectedInterface listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.nListener = listener;
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
        holder.Cid.setText(arrayList.get(position).getCid());
        holder.Uid.setText(arrayList.get(position).getUid());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView CommentWriter;
        public TextView CommentContent;
        public TextView Cid;
        public TextView Uid;
        public TextView DeleteBtn;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.CommentWriter =itemView.findViewById(R.id.CommentWriter);
            this.CommentContent =itemView.findViewById(R.id.CommentContent);
            this.Cid = itemView.findViewById(R.id.CidTextView);
            this.Uid = itemView.findViewById(R.id.UidTextView);
            this.DeleteBtn = itemView.findViewById(R.id.DeleteBtn);

            DeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }

    }

}
