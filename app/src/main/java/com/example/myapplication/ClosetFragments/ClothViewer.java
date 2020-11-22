package com.example.myapplication.ClosetFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class ClothViewer extends LinearLayout {
    ImageView imageView;

    public ClothViewer(Context context) {
        super(context);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clothitem,this,true);

        imageView = (ImageView) findViewById(R.id.clothItemImageView);
    }

    public void setItem(ClothItem clothItem){
        imageView.setImageResource(clothItem.getImage());
    }
}
