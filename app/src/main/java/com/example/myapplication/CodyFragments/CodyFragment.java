package com.example.myapplication.CodyFragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.BoardFragments.BoardWriteFragment;
import com.example.myapplication.adapter.CodyAdapter;
import com.example.myapplication.info.CodyInfo;
import com.example.myapplication.info.ReadBoardInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.adapter.BoardLoadAdapter;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class CodyFragment extends Fragment{
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private final FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CodyInfo> arrayList;
    private String Uid;
    private String[] ImageURL;
    private FloatingActionButton WriteBtn;
    private MainActivity activity;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cody, container, false);

        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) root.findViewById(R.id.codyRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();// info 담는 어레이 리스트
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();

        Uid = auth.getUid();
        databaseReference = database.getReference(Uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CodyInfo user = snapshot.getValue(CodyInfo.class);
                    arrayList.add(user);
                }
                adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fragement_cody", String.valueOf(error.toException()));
            }
        });

        adapter = new CodyAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);


        WriteBtn = (FloatingActionButton)root.findViewById(R.id.WriteBtn);
        WriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodyWriteFragment codyWriteFragment = new CodyWriteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainLayout, codyWriteFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }



}