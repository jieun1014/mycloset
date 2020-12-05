package com.example.myapplication.BoardFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

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

public class BoardFragment extends Fragment implements BoardLoadAdapter.OnListItemSelectedInterface {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReadBoardInfo> arrayList;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String[] items = {"전체", "코디 질문", "코디 자랑"};
    private String str;
    private SearchView searchView;
    private FloatingActionButton WriteBtn;
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
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        WriteBtn = (FloatingActionButton) root.findViewById(R.id.WriteBtn);
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
                    arrayList.clear();
                    ReadBoard();
                } else if (str.equals("코디 질문")) {
                    arrayList.clear();
                    ReadBoardCategory("[코디 질문]");
                } else {
                    arrayList.clear();
                    ReadBoardCategory("[코디 자랑]");
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
                    arrayList.clear();
                    SearchBoardAll(query);
                } else if (str.equals("코디 질문")) {
                    arrayList.clear();
                    SearchBoardCategory("[코디 질문]", query);
                } else {
                    arrayList.clear();
                    SearchBoardCategory("[코디 자랑]", query);
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

    private void SearchBoardAll(String query) {
        db.collection("Boards")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("title").toString().contains(query)) {
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
                                if (document.getData().get("category").toString().equals(category)) {
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
                                if (document.getData().get("category").toString().equals(category) && document.getData().get("title").toString().contains(query)) {
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
        bundle.putString("positionCheck", "board");
        boardReadFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, boardReadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}