package com.example.myapplication.CodyFragments;

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

public class CodyReadFragment extends Fragment{
    private Context context;

    private LinearLayout parent;
    private MainActivity activity;
    private TextView Category, Title, Content, WriteDate;
    private ImageView MenuBtn;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private String Did, Uid, positionCheck;
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
        View root =  inflater.inflate(R.layout.fragment_cody_read, container, false);

        Category = (TextView)root.findViewById(R.id.Category);
        Title = (TextView)root.findViewById(R.id.Title);
        Content = (TextView)root.findViewById(R.id.Content);
        WriteDate = (TextView)root.findViewById(R.id.WriteDate);
        parent = root.findViewById(R.id.ImageContent);

        MenuBtn = (ImageView) root.findViewById(R.id.MenuBtn);

        if (getArguments() != null) {
            Did = getArguments().getString("Did");
            positionCheck = getArguments().getString("positionCheck");
            ReadBoard();
        }

        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v );
                popupMenu.getMenuInflater().inflate(R.menu.menu_codyread, popupMenu.getMenu());
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
                                                db.collection("Codies").document(Did).delete();

                                                for (int i = 0; i < ImageURL.length; i++) {
                                                    StorageReference DidDeleteRef = storage.getReference("CodyImages/" + Did + "/" + i + ".jpg");
                                                    DidDeleteRef.delete();
                                                }

                                                CodyFragment codyFragment = new CodyFragment();
                                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                transaction.replace(R.id.mainLayout, codyFragment);
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
                            }
//                            else {
//                                BoardModifyFragment boardModifyFragment = new BoardModifyFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("Did", Did);
//                                bundle.putString("positionCheck", positionCheck);
//                                boardModifyFragment.setArguments(bundle);
//                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                transaction.replace(R.id.mainLayout, boardModifyFragment);
//                                transaction.addToBackStack(null);
//                                transaction.commit();
//                            }
                        } else  {
                            startToast("작성자가 아닙니다.");
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        return root;
    }

    private void ReadBoard() {
        DocumentReference docRef = db.collection("Codies").document(Did);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String getURL = (document.getData().get("profile").toString());
                        ImageURL = getURL.split(",");
                        ArrayList<String> ImageUrl = new ArrayList<>(Arrays.asList(ImageURL));

                        for (int i = 0; i < ImageURL.length; i++) {
                            String ck = ImageUrl.get(i).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");

                            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ImageView imageView = new ImageView(getContext());
                            imageView.setLayoutParams(layoutParams);
                            Glide.with(getContext()).load(ck).override(600,600).into(imageView);
                            parent.addView(imageView);
                        }

                        Category.setText(document.getData().get("category").toString());
                        Title.setText(document.getData().get("title").toString());
                        Content.setText(document.getData().get("contents").toString());
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

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

}