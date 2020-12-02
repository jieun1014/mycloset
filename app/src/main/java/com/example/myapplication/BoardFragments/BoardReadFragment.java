package com.example.myapplication.BoardFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.BoardLoadAdapter;
import com.example.myapplication.info.CommentReadInfo;
import com.example.myapplication.info.CommentWriteInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentLoadAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class BoardReadFragment extends Fragment implements CommentLoadAdapter.OnListItemSelectedInterface{
    private Context context;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private LinearLayout parent;
    private MainActivity activity;
    private TextView Category, Title, Content, Writer, WriteDate;
    private EditText CommentEditText;
    private Button CommentSubmitBtn;
    private ImageView MenuBtn;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private ArrayList<CommentReadInfo> arrayList;
    private ArrayList<String> cidList;
    private String Did, Uid;
    private String[] ImageURL;
    private int Count;

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
        parent = root.findViewById(R.id.ImageContent);

        CommentEditText = (EditText) root.findViewById(R.id.CommentEditText);
        CommentSubmitBtn = (Button) root.findViewById(R.id.CommentSubmitBtn);
        MenuBtn = (ImageView) root.findViewById(R.id.MenuBtn);

        recyclerView2 = root.findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        cidList = new ArrayList<>();

        if (getArguments() != null) {
            Did = getArguments().getString("Did");
            ReadBoard();
        }
        ReadComment();

        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v );
                popupMenu.getMenuInflater().inflate(R.menu.menu_boardread, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (user.getUid().equals(Uid)) {
                            if (item.getTitle().equals("삭제")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("게시글 삭제");
                                builder.setMessage("게시글을 삭제하시겠습니까?");
                                builder.setPositiveButton("예",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.collection("Boards").document(Did).delete();

                                                db.collection("BoardComment").whereEqualTo("did", Did)
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                                                    String cid = ds.get("cid").toString();
                                                                    cidList.add(cid);
                                                                }
                                                            }
                                                        });

                                                for (int i = 0; i < ImageURL.length; i++) {
                                                    StorageReference DidDeleteRef = storage.getReference("BoardImages/" + Did + "/" + i + ".jpg");
                                                    DidDeleteRef.delete();
                                                }

                                                Handler mHandler = new Handler();
                                                mHandler.postDelayed(new Runnable()  {
                                                    public void run() {
                                                        for (int i = 0; i < cidList.size(); i++)
                                                            db.collection("BoardComment").document(cidList.get(i)).delete();
                                                    }
                                                }, 3000);

                                                BoardFragment boardFragment = new BoardFragment();
                                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                transaction.replace(R.id.mainLayout, boardFragment);
                                                transaction.commit();
                                            }
                                        });
                                builder.setNegativeButton("아니오",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            } else {
                                BoardModifyFragment boardModifyFragment = new BoardModifyFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("Did", Did);
                                boardModifyFragment.setArguments(bundle);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.mainLayout, boardModifyFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        } else  {
                            startToast("작성자가 아닙니다.");
                        }
                        return false;
                    }
                });
                popupMenu.show();
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String getURL = (document.getData().get("images").toString());
                        ImageURL = getURL.split(",");
                        ArrayList<String> ImageUrl = new ArrayList<>(Arrays.asList(ImageURL));

                        for (int i = 0; i < ImageURL.length; i++) {
                            String ck = ImageUrl.get(i).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");

                            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ImageView imageView = new ImageView(getContext());
                            imageView.setLayoutParams(layoutParams);
                            Glide.with(getContext()).load(ck).override(1000).into(imageView);
                            parent.addView(imageView);
                        }

                        Category.setText(document.getData().get("category").toString());
                        Title.setText(document.getData().get("title").toString());
                        Content.setText(document.getData().get("contents").toString());
                        Writer.setText(document.getData().get("writer").toString());
                        WriteDate.setText(document.getData().get("writeDate").toString());
                        Uid = document.getData().get("uid").toString();
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
                                            document.getData().get("commentContent").toString(),
                                            document.getData().get("cid").toString(),
                                            document.getData().get("uid").toString()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
        adapter = new CommentLoadAdapter(arrayList, getContext(), this);
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
                            DocumentReference documentReference = db.collection("BoardComment").document();

                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
                            String time = mFormat.format(date);

                            CommentWriteInfo commentWriteInfo = new CommentWriteInfo("익명", Content, Did, time, documentReference.getId(), user.getUid());
                            documentReference.set(commentWriteInfo);
                            CommentEditText.setText(null);

                            arrayList.clear();
                            ReadComment();
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

    @Override
    public void onItemSelected(View v, int position) {
        CommentLoadAdapter.CustomViewHolder viewHolder = (CommentLoadAdapter.CustomViewHolder)recyclerView2.findViewHolderForAdapterPosition(position);
        String Cid = (viewHolder.Cid).getText().toString();
        String Uid = (viewHolder.Uid).getText().toString();

        if (Uid.equals(user.getUid())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("댓글 삭제");
            builder.setMessage("댓글을 삭제하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("BoardComment").document(Cid).delete();

                            arrayList.clear();
                            ReadComment();
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }   else {
            startToast("작성자가 아닙니다.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

}