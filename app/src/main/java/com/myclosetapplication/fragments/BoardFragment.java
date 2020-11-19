package com.myclosetapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.myclosetapplication.R;

public class BoardFragment extends Fragment {

    public static com.myclosetapplication.fragments.ClosetFragment newInstance() {
        return new com.myclosetapplication.fragments.ClosetFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);
        return root;
    }
}