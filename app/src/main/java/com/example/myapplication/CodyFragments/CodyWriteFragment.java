package com.example.myapplication.CodyFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myapplication.BoardFragments.BoardFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.CodyInfo;
import com.example.myapplication.info.CodyWriteInfo;
import com.example.myapplication.info.WriteBoardInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class CodyWriteFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private DatabaseReference Database;
    private Uri filePath, filePath1;
    private int i=1;
    MainActivity activity;
    ImageView imageView, imageView1, imageView2, imageView3;
    EditText editText, editText1;
    Button submit;





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
        super.onCreate(savedInstanceState);
        View root =  inflater.inflate(R.layout.fragment_cody_write, container, false);

        imageView = (ImageView)root.findViewById(R.id.imageHat);
        imageView1 = (ImageView)root.findViewById(R.id.imageTop);
//        imageView2 = (ImageView)root.findViewById(R.id.imageBot);
//        imageView3 = (ImageView)root.findViewById(R.id.imageShoe);
        editText = (EditText)root.findViewById(R.id.et_title);
        editText1 = (EditText)root.findViewById(R.id.et_contents);
        submit = (Button)root.findViewById(R.id.submitbtn);

        Database = FirebaseDatabase.getInstance().getReference();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"이미지를 선택하세요"), 0);
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"이미지를 선택하세요"), 1);
            }
        });
//
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 3);
//            }
//        });
//
//        imageView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 4);
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
                String getTitle = editText.getText().toString();
                String getContents = editText1.getText().toString();
//                String getProfile = imageView.getText().toString();

                HashMap result = new HashMap<>();
                result.put("title",getTitle);
                result.put("contents",getContents);
//                result.put("profile",getProfile);

                upload2("1"+i++ , getTitle, getContents);
            }
        });
        return root;
    }

    private void upload2(String userID, String title, String contents) {
        CodyWriteInfo user = new CodyWriteInfo(title, contents);

        Database.child("User").child(title).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
//                        Toast.makeText(MainActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
//                        Toast.makeText(MainActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


//    private void readUser(){
//        Database.child("User").child("1").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                if(dataSnapshot.getValue(User.class) != null){
//                    User post = dataSnapshot.getValue(User.class);
//                    assert post != null;
//                    Log.w("FireBaseData", "getData" + post.toString());
//                } else {
//                    //Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 1 && resultCode == RESULT_OK){
            filePath1 = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath1));
            try {
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath1);
                imageView1.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Check which request we're responding to
//        switch (requestCode) {
//            case (1): {
//                if (requestCode == 1) {
//                    // Make sure the request was successful
//                    Uri image = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
//                        imageView.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            case (2): {
//                if (requestCode == 2) {
//                    // Make sure the request was successful
//                    Uri image = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
//                        imageView1.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            case (3): {
//                if (requestCode == 3) {
//                    // Make sure the request was successful
//                    Uri image = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
//                        imageView2.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            case (4): {
//                if (requestCode == 4) {
//                    // Make sure the request was successful
//                    Uri image = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
//                        imageView3.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }


    private void upload(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("업로드중");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("MMdd_hhmm");
            Date now = new Date();
            String filename = formatter.format(now) + "Hat.jpg";
            StorageReference storageRef =storage.getReferenceFromUrl("gs://test-ae7be.appspot.com/").child("Cody/" + filename);

            storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(activity.getApplicationContext(),"업로드 완료",Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(activity.getApplicationContext(), "업로드 실패",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }

        if(filePath1 != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("업로드중");
            progressDialog.show();

            FirebaseStorage storage1 = FirebaseStorage.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("MMdd_hhmm");
            Date now = new Date();
            String filename = formatter.format(now) + "Top.jpg";
            StorageReference storageRef =storage1.getReferenceFromUrl("gs://test-ae7be.appspot.com/").child("Cody/" + filename);

            storageRef.putFile(filePath1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(activity.getApplicationContext(),"업로드 완료",Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(activity.getApplicationContext(), "업로드 실패",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }

        else {
            Toast.makeText(activity.getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}