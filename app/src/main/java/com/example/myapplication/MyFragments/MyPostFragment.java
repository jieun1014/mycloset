package com.example.myapplication.MyFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.myapplication.BoardFragments.BoardReadFragment;
import com.example.myapplication.BoardFragments.BoardWriteFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.BoardLoadAdapter;
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

import java.util.ArrayList;

public class MyPostFragment extends Fragment implements BoardLoadAdapter.OnListItemSelectedInterface{
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReadBoardInfo> arrayList;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String[] items = {"전체", "코디 질문", "코디 자랑"};
    private String str;
    private SearchView searchView;
    private Spinner spinner;

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
        View root = inflater.inflate(R.layout.fragment_my_post, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, items
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str = parent.getItemAtPosition(position).toString();
                if (str.equals("전체")) {
                    ReadBoard();
                    arrayList.clear();
                } else if (str.equals("코디 질문")) {
                    ReadBoardCategory("[코디 질문]");
                    arrayList.clear();
                } else {
                    ReadBoardCategory("[코디 자랑]");
                    arrayList.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        searchView = (SearchView) root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (str.equals("전체")) {
                    SearchBoardAll(query);
                    arrayList.clear();
                } else if (str.equals("코디 질문")) {
                    SearchBoardCategory("[코디 질문]", query);
                    arrayList.clear();
                } else {
                    SearchBoardCategory("[코디 자랑]", query);
                    arrayList.clear();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                                if (document.getData().get("uid").toString().equals(user.getUid())) {
                                    arrayList.add(new ReadBoardInfo(
                                            document.getId(),
                                            document.getData().get("category").toString(),
                                            document.getData().get("title").toString(),
                                            document.getData().get("writer").toString(),
                                            document.getData().get("writeDate").toString()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new BoardLoadAdapter(arrayList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void SearchBoardAll(String query) {
        db.collection("Boards")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("title").toString().contains(query) && document.getData().get("uid").toString().equals(user.getUid())) {
                                    arrayList.add(new ReadBoardInfo(
                                            document.getId(),
                                            document.getData().get("category").toString(),
                                            document.getData().get("title").toString(),
                                            document.getData().get("writer").toString(),
                                            document.getData().get("writeDate").toString()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new BoardLoadAdapter(arrayList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void ReadBoardCategory(String category) {
        db.collection("Boards")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("category").toString().equals(category) && document.getData().get("uid").toString().equals(user.getUid())) {
                                    arrayList.add(new ReadBoardInfo(
                                            document.getId(),
                                            document.getData().get("category").toString(),
                                            document.getData().get("title").toString(),
                                            document.getData().get("writer").toString(),
                                            document.getData().get("writeDate").toString()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new BoardLoadAdapter(arrayList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void SearchBoardCategory(String category, String query) {
        db.collection("Boards")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("category").toString().equals(category) && document.getData().get("title").toString().contains(query)
                                        && document.getData().get("uid").toString().equals(user.getUid())) {
                                    arrayList.add(new ReadBoardInfo(
                                            document.getId(),
                                            document.getData().get("category").toString(),
                                            document.getData().get("title").toString(),
                                            document.getData().get("writer").toString(),
                                            document.getData().get("writeDate").toString()));
                                }
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
        BoardLoadAdapter.CustomViewHolder viewHolder = (BoardLoadAdapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        String Did = (viewHolder.Did).getText().toString();
        System.out.println(Did);

        BoardReadFragment boardReadFragment = new BoardReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Did", Did);
        boardReadFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, boardReadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}