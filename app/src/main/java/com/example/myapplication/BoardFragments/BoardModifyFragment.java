package com.example.myapplication.BoardFragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.GalleryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MyFragments.MyPostFragment;
import com.example.myapplication.R;
import com.example.myapplication.info.WriteBoardInfo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class BoardModifyFragment extends Fragment {
    private MainActivity activity;
    private LinearLayout parent;
    private Button SubmitBtn, SelectPicBtn;
    private EditText TitleEditText, ContentsEditText;
    private RadioButton QuestionRdb, BoastRdb;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private String Did, Uid, Title, Contents, Category, Writer, WriteDate, Time, positionCheck;
    private int PathCount, successCount;
    private String[] ImageURL;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<String> contentsList = new ArrayList<>();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board_modify, container, false);

        SubmitBtn = (Button) root.findViewById(R.id.SubmitBtn);
        TitleEditText = (EditText) root.findViewById(R.id.TitleEditText);
        ContentsEditText = (EditText) root.findViewById(R.id.ContentsEditText);
        QuestionRdb = (RadioButton) root.findViewById(R.id.QuestionRdb);
        BoastRdb = (RadioButton) root.findViewById(R.id.BoastRdb);
        SelectPicBtn = (Button) root.findViewById(R.id.SelectPicBtn);
        parent = root.findViewById(R.id.ImageContents);

        if (getArguments() != null) {
            Did = getArguments().getString("Did");
            positionCheck = getArguments().getString("positionCheck");
            Log.e("여기여기", positionCheck);
            ReadBoard();
        }

        SelectPicBtn.setOnClickListener(new View.OnClickListener() {
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

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        return root;
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
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);

                    PathCount++;
                    successCount++;

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(activity).load(profilePath).override(1000).into(imageView);
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
                            pathList.remove(profilePath);
                            PathCount--;
                            successCount--;
                            return true;
                        }
                    });
                }
                break;
        }
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
                            if (!ck.equals("")) {
                                pathList.add(ck);
                                contentsList.add(ck);
                                PathCount++;
                                successCount++;
                            }
                            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ImageView imageView = new ImageView(getContext());
                            imageView.setLayoutParams(layoutParams);
                            Glide.with(getContext()).load(ck).override(1000).into(imageView);
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
                                    pathList.remove(ck);
                                    contentsList.remove(ck);
                                    PathCount--;
                                    successCount--;

                                    return true;
                                }
                            });
                        }

                        Category = document.getData().get("category").toString();
                        if (Category.equals("[코디 질문]")) {
                            QuestionRdb.setChecked(true);
                        } else {
                            BoastRdb.setChecked(true);
                        }
                        Writer = document.getData().get("writer").toString();
                        WriteDate = document.getData().get("writeDate").toString();
                        Time = document.getData().get("time").toString();
                        TitleEditText.setText(document.getData().get("title").toString());
                        ContentsEditText.setText(document.getData().get("contents").toString());
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

    private void submit() {
        Title = TitleEditText.getText().toString();
        Contents = ContentsEditText.getText().toString();

        Log.e("path", PathCount + ", " + successCount);

        if (Title.length() == 0 || Contents.length() == 0 || !BoastRdb.isChecked() && !QuestionRdb.isChecked()) {
            startToast("모든 항목을 입력해주세요.");
        } else {
            if (QuestionRdb.isChecked())
                Category = "[코디 질문]";
            else
                Category = "[코디 자랑]";

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("게시글 작성");
            builder.setMessage("게시글을 수정하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            StorageReference storageRef = storage.getReference();
                            for (int i = 0; i < PathCount; i++) {
                                if (!pathList.get(i).contains("https://")) {
                                    contentsList.add(pathList.get(i));
                                    final StorageReference mountainImageRef = storageRef.child("BoardImages/" + Did + "/" + i + ".jpg");
                                    try {
                                        InputStream stream = new FileInputStream(new File(pathList.get(i)));
                                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + i).build();
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
                                                        contentsList.set(index, uri.toString());
                                                        if (pathList.size() == successCount) {

                                                            WriteBoardInfo writeBoardInfo = new WriteBoardInfo(Category, Title, Contents, contentsList, Writer, WriteDate, Time, user.getUid());
                                                            db.collection("Boards").document(Did).set(writeBoardInfo);
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    WriteBoardInfo writeBoardInfo = new WriteBoardInfo(Category, Title, Contents, contentsList, Writer, WriteDate, Time, user.getUid());
                                    db.collection("Boards").document(Did).set(writeBoardInfo);
                                }
                            }

                            if (successCount == 0) {
                                ArrayList<String> sample = new ArrayList();
                                sample.add("");
                                WriteBoardInfo writeBoardInfo = new WriteBoardInfo(Category, Title, Contents, sample, Writer, WriteDate, Time, user.getUid());
                                db.collection("Boards").document(Did).set(writeBoardInfo);
                            }

                            if (positionCheck.equals("board")) {
                                BoardFragment boardFragment = new BoardFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.mainLayout, boardFragment);
                                transaction.commit();
                            }   else {
                                MyPostFragment myPostFragment = new MyPostFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.mainLayout, myPostFragment);
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

    private void startToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}