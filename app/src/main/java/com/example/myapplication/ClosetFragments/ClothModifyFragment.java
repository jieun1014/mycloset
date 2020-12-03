package com.example.myapplication.ClosetFragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.GalleryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.ClothWriteInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ClothModifyFragment extends Fragment {
    private MainActivity activity;
    private LinearLayout parent;
    private ImageView clothPicImageView;
    private Button btnWriteSubmit, btnBack;
    private Spinner spinnerCloth;
    private EditText clothNameEditText, clothContentsEditText;

    private String clothNameInput, clothContentsInput, clothCategoryInput, profilePath, Did, Uid, time;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<String> contentsList = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

    private int ck;

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
        View root = inflater.inflate(R.layout.fragment_cloth_modify, container, false);

        clothNameEditText = (EditText) root.findViewById(R.id.clothNameEditText);
        spinnerCloth = (Spinner) root.findViewById(R.id.spinnerCloth);
        clothPicImageView = (ImageView) root.findViewById(R.id.clothPicImageView);
        clothContentsEditText = (EditText) root.findViewById(R.id.clothContentsEditText);
        btnWriteSubmit = (Button) root.findViewById(R.id.btnWriteSubmit);
        parent = root.findViewById(R.id.imageLayout);
        parent.removeView(clothPicImageView);

        ck = 1;

        ArrayAdapter clothAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.clothCategory, android.R.layout.simple_spinner_dropdown_item);
        spinnerCloth.setAdapter(clothAdapter);

        if (getArguments() != null) {
            Did = getArguments().getString("Did");
            ReadCloth();
        }

        clothPicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    } else {
                        startToast("권한을 허용해 주세요.");
                    }
                } else {
                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        btnWriteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
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
                        String category = document.getData().get("category").toString();
                        int categoryCheck = 7;
                        if (category.equals("아우터")) {
                            categoryCheck = 0;
                        } else if (category.equals("상의")){
                            categoryCheck = 1;
                        } else if (category.equals("하의")){
                            categoryCheck = 2;
                        } else if (category.equals("원피스")){
                            categoryCheck = 3;
                        } else if (category.equals("신발")){
                            categoryCheck = 4;
                        } else if (category.equals("가방")){
                            categoryCheck = 5;
                        } else if (category.equals("악세사리")){
                            categoryCheck = 6;
                        }  else if (category.equals("기타")){
                            categoryCheck = 7;
                        }
                        spinnerCloth.setSelection(categoryCheck);
                        clothNameEditText.setText(document.getData().get("title").toString());
                        clothContentsEditText.setText(document.getData().get("contents").toString());
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(layoutParams);
                        Glide.with(getContext()).load(document.getData().get("image")).override(1000).into(imageView);
                        parent.addView(imageView);
                        profilePath = document.getData().get("image").toString();
                        ck = 0;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startToast("사진을 길게 누르면 삭제됩니다.");
                            }
                        });

                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                parent.removeView(imageView);
                                parent.addView(clothPicImageView);
                                ck = 1;
                                return true;
                            }
                        });
                        Uid = document.getData().get("uid").toString();
                        time = document.getData().get("uploadTime").toString();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void submit()   {
        //if (clothNameEditText.length() == 0) {
        if (ck == 1 && clothNameEditText.length() != 0) {
            startToast("사진을 첨부해주세요.");
        } else if (clothNameEditText.length() == 0 && ck == 0)  {
            startToast("옷 이름을 입력해주세요.");
        } else if (clothNameEditText.length() == 0 && ck == 1)  {
            startToast("옷 이름과 사진이 없습니다.");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("옷 수정");
            builder.setMessage("옷을 수정하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            StorageReference storageRef = storage.getReference();

                            clothCategoryInput = spinnerCloth.getSelectedItem().toString();
                            clothNameInput = clothNameEditText.getText().toString();
                            clothContentsInput = clothContentsEditText.getText().toString();

                            if (!profilePath.contains("https://")) {
                                final StorageReference mountainImageRef = storageRef.child("ClothImages/" + Did + "/0.jpg");
                                try {
                                    InputStream stream = new FileInputStream(new File(profilePath));
                                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + 0).build();
                                    UploadTask uploadTask = mountainImageRef.putStream(stream, metadata);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                            mountainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    profilePath = uri.toString();

                                                    ClothWriteInfo clothWriteInfo = new ClothWriteInfo(clothCategoryInput, clothNameInput, clothContentsInput, time, user.getUid(), profilePath);
                                                    db.collection("Clothes").document(Did).set(clothWriteInfo);

                                                    ClosetFragment closetFragment = new ClosetFragment();
                                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.mainLayout, closetFragment);
                                                    transaction.commit();
                                                }
                                            });
                                        }
                                    });
                                } catch (FileNotFoundException e) {
                                    startToast("오류");
                                }
                            }   else {
                                ClothWriteInfo clothWriteInfo = new ClothWriteInfo(clothCategoryInput, clothNameInput, clothContentsInput, time, user.getUid(), profilePath);
                                db.collection("Clothes").document(Did).set(clothWriteInfo);

                                ClosetFragment closetFragment = new ClosetFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.mainLayout, closetFragment);
                                transaction.commit();
                            }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                    startActivity(intent);
                } else {
                    startToast("권한을 허용해 주세요.");
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra("profilePath");

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(activity).load(profilePath).override(900).into(imageView);
                    parent.removeView(clothPicImageView);
                    parent.addView(imageView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startToast("사진을 길게 누르면 삭제됩니다.");
                        }
                    });

                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            parent.removeView(imageView);
                            parent.addView(clothPicImageView);
                            ck = 1;
                            return true;
                        }
                    });
                }
                break;
        }
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
    }
}