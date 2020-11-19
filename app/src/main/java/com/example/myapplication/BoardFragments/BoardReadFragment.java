package com.example.myapplication.BoardFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.info.CommentReadInfo;
import com.example.myapplication.info.CommentWriteInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentLoadAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class BoardReadFragment extends Fragment  {
    private Context context;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CommentReadInfo> arrayList;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean flag=false;
    MainActivity activity;
    TextView Category, Title, Content, Writer, WriteDate;
    EditText CommentEditText;
    Button CommentSubmitBtn;
    String Did;

    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_board_read, container, false);

        Category = (TextView)root.findViewById(R.id.Category);
        Title = (TextView)root.findViewById(R.id.Title);
        Content = (TextView)root.findViewById(R.id.Content);
        Writer = (TextView)root.findViewById(R.id.Writer);
        WriteDate = (TextView)root.findViewById(R.id.WriteDate);

        CommentEditText = (EditText) root.findViewById(R.id.CommentEditText);
        CommentSubmitBtn = (Button) root.findViewById(R.id.CommentSubmitBtn);

        if (getArguments() != null) {
            Did = getArguments().getString("Did");
            ReadBoard();
        }

        recyclerView2 = root.findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        ReadComment();

        db.collection("BoardComment").whereEqualTo("did", Did)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    if (flag) {
                                        Log.d(TAG, "New city: " + dc.getDocument().getData());
                                        ReadComment();
                                        arrayList.clear();
                                        break;
                                    }
                                case REMOVED:
                                    if (flag) {
                                        Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                        ReadComment();
                                        arrayList.clear();
                                        break;
                                    }
                            }
                        }
                        flag = true;
                    }
                });

        CommentSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteComment();
            }
        });

        return root;
    }

    private void ReadBoard() {
        DocumentReference docRef = db.collection("Boards").document(Did);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Category.setText(document.getData().get("category").toString());
                        Title.setText(document.getData().get("title").toString());
                        Content.setText(document.getData().get("contents").toString());
                        Writer.setText(document.getData().get("writer").toString());
                        WriteDate.setText(document.getData().get("writeDate").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void ReadComment() {
        db.collection("BoardComment")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("did").toString().equals(Did)) {
                                    arrayList.add(new CommentReadInfo(
                                            document.getData().get("commentWriter").toString(),
                                            document.getData().get("commentContent").toString()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new CommentLoadAdapter(arrayList, getContext());
        recyclerView2.setAdapter(adapter);
    }

    private void WriteComment() {
        String Content = CommentEditText.getText().toString();

        if (CommentEditText.length() == 0) {
            startToast("댓글을 입력해주세요.");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("댓글 작성");
            builder.setMessage("댓글을 작성하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
                            String time = mFormat.format(date);

                            CommentWriteInfo commentWriteInfo = new CommentWriteInfo("최원우", Content, Did, time);
                            db.collection("BoardComment").document().set(commentWriteInfo);
                            CommentEditText.setText(null);
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

}