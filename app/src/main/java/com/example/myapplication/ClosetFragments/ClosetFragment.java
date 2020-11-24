package com.example.myapplication.ClosetFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ClosetFragment extends Fragment {

    MainActivity activity;
    GridView gridView;
    ClothAdapter clothAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public static ClosetFragment newInstance() {
        return new ClosetFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_closet, container, false);

        gridView = (GridView)root.findViewById(R.id.closetGridView);

        clothAdapter = new ClothAdapter();
        clothAdapter.addItem(new ClothItem(R.drawable.bag1));
        clothAdapter.addItem(new ClothItem(R.drawable.accessory1));
        clothAdapter.addItem(new ClothItem(R.drawable.bottom1));
        clothAdapter.addItem(new ClothItem(R.drawable.dress1));
        clothAdapter.addItem(new ClothItem(R.drawable.outer1));
        clothAdapter.addItem(new ClothItem(R.drawable.top1));
        clothAdapter.addItem(new ClothItem(R.drawable.shoes1));
        clothAdapter.addItem(new ClothItem(R.drawable.bag1));
        clothAdapter.addItem(new ClothItem(R.drawable.accessory1));
        clothAdapter.addItem(new ClothItem(R.drawable.bottom1));
        clothAdapter.addItem(new ClothItem(R.drawable.dress1));
        clothAdapter.addItem(new ClothItem(R.drawable.outer1));
        clothAdapter.addItem(new ClothItem(R.drawable.top1));
        clothAdapter.addItem(new ClothItem(R.drawable.shoes1));

        gridView.setAdapter(clothAdapter);
//
//        Button b1 = (Button)root.findViewById(R.id.button);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "옷장입니다.", Toast.LENGTH_LONG).show();
//            }
//        });

        return root;
    }

    class ClothAdapter extends BaseAdapter{
        ArrayList<ClothItem> items = new ArrayList<>();

        ImageView imageView;

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ClothItem clothItem){
            items.add(clothItem);
        }
        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View View, ViewGroup viewGroup) {
            ClothViewer clothViewer = new ClothViewer(activity.getApplicationContext());
            clothViewer.setItem(items.get(i));
            return clothViewer;
        }
    }
}