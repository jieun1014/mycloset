package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.MainActivity;
import com.example.myapplication.info.ReadBoardInfo;
import com.example.myapplication.R;

import java.util.ArrayList;

public class BoardLoadAdapter extends RecyclerView.Adapter<BoardLoadAdapter.CustomViewHolder> {

    private ArrayList<ReadBoardInfo> arrayList;
    private Context context;
    private int pos;
    MainActivity activity;

    private OnListItemSelectedInterface mListener;
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    public BoardLoadAdapter(ArrayList<ReadBoardInfo> arrayList, Context context, OnListItemSelectedInterface listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_boarditem, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.Did.setText(arrayList.get(position).getDid());
        holder.Category.setText(arrayList.get(position).getCategory());
        holder.Title.setText(arrayList.get(position).getTitle());
        holder.Writer.setText(arrayList.get(position).getWriter());
        holder.WriteDate.setText(arrayList.get(position).getWriteDate());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, position);

                /*BoardReadFragment boardReadFragment = new BoardReadFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Category", arrayList.get(position).getCategory());
                bundle.putString("Title", arrayList.get(position).getTitle());
                bundle.putString("Contents", arrayList.get(position).getContents());
                bundle.putString("Writer", arrayList.get(position).getWriter());
                bundle.putString("WriteDate", arrayList.get(position).getWriteDate());
                boardReadFragment.setArguments(bundle);
                Log.d(TAG, bundle.getString("Category"));

                //Intent intent = new Intent(context, MainActivity.class);
                //intent.putExtra("Category", arrayList.get(pos).getCategory());
                activity = (MainActivity) context;
                activity.onBoardFragmentChange(2);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView Did;
        public TextView Category;
        public TextView Title;
        public TextView Writer;
        public TextView WriteDate;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.Did =itemView.findViewById(R.id.Did);
            this.Category =itemView.findViewById(R.id.Category);
            this.Title =itemView.findViewById(R.id.Title);
            this.Writer =itemView.findViewById(R.id.Writer);
            this.WriteDate =itemView.findViewById(R.id.WriteDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }

    }

}
