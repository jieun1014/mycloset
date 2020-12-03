package com.example.myapplication.ClosetFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BoardFragments.BoardReadFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.BoardLoadAdapter;
import com.example.myapplication.adapter.ClothAdapter;
import com.example.myapplication.info.ClothReadInfo;
import com.example.myapplication.info.ReadBoardInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ClosetFragment extends Fragment implements ClothAdapter.OnListItemSelectedInterface{

    MainActivity activity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton btnClosetWrite;
    private SearchView searchView;

    private ArrayList<ClothReadInfo> arrayList;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

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

        recyclerView = root.findViewById(R.id.closetView);
        btnClosetWrite = (FloatingActionButton) root.findViewById(R.id.btnClosetWrite);
        arrayList = new ArrayList<>();

        final int numberOfColumns = 3;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        ReadClothes();

        btnClosetWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothWriteFragment clothWriteFragment = new ClothWriteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainLayout, clothWriteFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }

    private void ReadClothes() {
        db.collection("Clothes")
                .orderBy("uploadTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("uid").toString().equals(user.getUid())) {
                                    arrayList.add(new ClothReadInfo(
                                            document.getData().get("image").toString(),
                                            document.getId()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new ClothAdapter(arrayList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(View v, int position) {
        ClothAdapter.CustomViewHolder viewHolder = (ClothAdapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        String Did = (viewHolder.Did).getText().toString();

        ClothReadFragment clothReadFragment = new ClothReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Did", Did);
        clothReadFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, clothReadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}