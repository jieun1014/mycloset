package com.example.myapplication.ClosetFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myapplication.BoardFragments.BoardFragment;
import com.example.myapplication.BoardFragments.BoardModifyFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class ClothReadFragment extends Fragment {
    private MainActivity activity;
    private LinearLayout parent;
    private TextView clothNameTextView, clothContentsTextView, categoryTextView;
    private ImageView menuBtn;

    private String Did, Uid;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cloth_read, container, false);

        categoryTextView = root.findViewById(R.id.categoryTextView);
        clothNameTextView = root.findViewById(R.id.clothNameTextView);
        clothContentsTextView = root.findViewById(R.id.clothContentsTextView);
        menuBtn = root.findViewById(R.id.MenuBtn);
        parent = root.findViewById(R.id.imageView);

        if (getArguments() != null) {
            Did = getArguments().getString("Did");
            ReadCloth();
        }

        menuBtn.setOnClickListener(new View.OnClickListener() {
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
                                                db.collection("Clothes").document(Did).delete();

                                                StorageReference DidDeleteRef = storage.getReference("ClothImages/" + Did + "/0.jpg");
                                                DidDeleteRef.delete();

                                                ClosetFragment closetFragment = new ClosetFragment();
                                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                transaction.replace(R.id.mainLayout, closetFragment);
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
                                ClothModifyFragment clothModifyFragment = new ClothModifyFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("Did", Did);
                                clothModifyFragment.setArguments(bundle);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.mainLayout, clothModifyFragment);
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

        return root;
    }

    private void ReadCloth() {
        DocumentReference docRef = db.collection("Clothes").document(Did);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        categoryTextView.setText("["+document.getData().get("category").toString()+"]");
                        clothNameTextView.setText(document.getData().get("title").toString());
                        clothContentsTextView.setText(document.getData().get("contents").toString());
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(layoutParams);
                        Glide.with(getContext()).load(document.getData().get("image")).override(1000).into(imageView);
                        parent.addView(imageView);
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
