package com.example.myapplication.ClosetFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.GalleryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.ClothInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;

public class ClothWriteFragment extends Fragment {
    private MainActivity activity;
    private LinearLayout parent;
    private ImageView clothPicImageView;
    private Button btnWriteSubmit, btnBack;
    private Spinner spinnerCloth;
    private EditText clothNameEditText, clothContentsEditText;
    private String clothNameInput, clothContentsInput, clothCategoryInput;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private static final int REQUEST_CODE = 0;
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

    public static ClothWriteFragment newInstance() {
        return new ClothWriteFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cloth_write, container, false);

        clothNameEditText = (EditText) root.findViewById(R.id.clothNameEditText);
        spinnerCloth = (Spinner) root.findViewById(R.id.spinnerCloth);
        clothPicImageView = (ImageView) root.findViewById(R.id.clothItemImageView);
        clothContentsEditText = (EditText) root.findViewById(R.id.clothContentsEditText);
        btnWriteSubmit = (Button) root.findViewById(R.id.btnWriteSubmit);



        //카테고리 스피너 어댑터 연결
        ArrayAdapter clothAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.clothCategory, android.R.layout.simple_spinner_dropdown_item);
        spinnerCloth.setAdapter(clothAdapter);


        //이미지뷰 클릭시 갤러리 사진선택
//        clothPicImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, REQUEST_CODE);
//                if (ContextCompat.checkSelfPermission(getContext(),
//                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    } else {
//                        startToast("권한을 허용해 주세요.");
//                    }
//                } else {
//                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
//                    startActivityForResult(intent, 0);
//                }
//            }
//        });




        btnWriteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clothCategoryInput = spinnerCloth.getSelectedItem().toString();
                clothNameInput = clothNameEditText.getText().toString();
                clothContentsInput = clothContentsEditText.getText().toString();
                Date date = new Date(System.currentTimeMillis());
                String uploadTime = date.toString();
                ClothInfo clothInfo = new ClothInfo(clothCategoryInput, clothNameInput, clothContentsInput, uploadTime );

                db.collection("Clothes").document().set(clothInfo);
            }
        });

        return root;
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
    }
}
