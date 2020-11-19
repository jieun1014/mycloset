package com.example.myapplication.BoardFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.info.ReadBoardInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.adapter.BoardLoadAdapter;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BoardFragment extends Fragment implements BoardLoadAdapter.OnListItemSelectedInterface{
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReadBoardInfo> arrayList;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FloatingActionButton WriteBtn;

    MainActivity activity;

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
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        ReadBoard();

        WriteBtn = (FloatingActionButton)root.findViewById(R.id.WriteBtn);
        WriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardWriteFragment boardWriteFragment = new BoardWriteFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainLayout, boardWriteFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }

    private void ReadBoard() {
        db.collection("Boards")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                arrayList.add(new ReadBoardInfo(
                                        document.getId(),
                                        document.getData().get("category").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("writer").toString(),
                                        document.getData().get("writeDate").toString()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new BoardLoadAdapter(arrayList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(View v, int position) {
        BoardLoadAdapter.CustomViewHolder viewHolder = (BoardLoadAdapter.CustomViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
        String Did = (viewHolder.Did).getText().toString();
        System.out.println(Did);

        BoardReadFragment boardReadFragment = new BoardReadFragment();
        BoardFragment boardFragment = new BoardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Did", Did);
        boardReadFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, boardReadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}