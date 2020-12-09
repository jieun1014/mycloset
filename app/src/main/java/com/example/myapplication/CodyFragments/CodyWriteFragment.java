package com.example.myapplication.CodyFragments;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.myapplication.GalleryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.CodyWriteInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
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

public class CodyWriteFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private DatabaseReference Database;
    private FirebaseAuth auth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private LinearLayout parent;
    private int PathCount, successCount;
    private String Title, Contents, Category;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<String> contentsList = new ArrayList<>();

    SimpleDateFormat nFormat = new SimpleDateFormat("yy.MM.dd HH:mm");
    MainActivity activity;
    EditText TitleEditText, ContentsEditText;
    Button submit, picBtn;

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

    public static CodyWriteFragment newInstance() {
        return new CodyWriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cody_write, container, false);

        parent = root.findViewById(R.id.ic);
        TitleEditText = (EditText) root.findViewById(R.id.et_title);
        ContentsEditText = (EditText) root.findViewById(R.id.et_contents);
        submit = (Button) root.findViewById(R.id.submitbtn);
        Database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        picBtn = root.findViewById(R.id.picBtn);

        picBtn.setOnClickListener(new View.OnClickListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        return root;
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    if (pathList.size() == 6)
                        startToast("더 이상 추가 할 수 없습니다");
                    else {
                        pathList.add(profilePath);
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(layoutParams);
                        Glide.with(activity).load(profilePath).override(500,500).into(imageView);
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
                                return true;
                            }
                        });
                    }
                    break;
                }
        }
    }

    private void upload() {
        Title = TitleEditText.getText().toString();
        Contents = ContentsEditText.getText().toString();

        if (Title.length() == 0 || Contents.length() == 0) {
            startToast("모든 항목을 입력해주세요.");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("게시글 작성");
            builder.setMessage("게시글을 작성하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            StorageReference storageRef = storage.getReference();
                            DocumentReference documentReference = db.collection("Codies").document();

                            for (int i = 0; i < parent.getChildCount(); i++) {
                                contentsList.add(pathList.get(PathCount));
                                final StorageReference mountainImageRef = storageRef.child("CodyImages/" + documentReference.getId() + "/" + PathCount + ".jpg");
                                try {
                                    InputStream stream = new FileInputStream(new File(pathList.get(PathCount)));
                                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + PathCount).build();
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
                                                    successCount++;
                                                    if (pathList.size() == successCount) {
                                                        //완료

                                                        long now = System.currentTimeMillis();
                                                        Date date = new Date(now);

                                                        String time = nFormat.format(date);
                                                        Category = "전체";

                                                        CodyWriteInfo CodyWriteInfo = new CodyWriteInfo(Category ,user.getUid(),Title, Contents, contentsList ,time);
                                                        documentReference.set(CodyWriteInfo);

                                                        CodyFragment codyFragment = new CodyFragment();
                                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                        transaction.replace(R.id.mainLayout, codyFragment);
                                                        transaction.commit();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                PathCount++;
                            }
                            if (PathCount == 0) {
                                startToast("사진을 추가해주세요");
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
}