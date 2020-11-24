package com.example.myapplication.BoardFragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.GalleryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.WriteBoardInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoardWriteFragment extends Fragment {
    MainActivity activity;
    private Context context;
    private ImageView imageView;
    private Button SubmitBtn, SelectPicBtn;
    private EditText TitleEditText, ContentsEditText;
    private RadioButton QuestionRdb, BoastRdb;
    private String Title, Contents, Category;
    private LinearLayout parent;
    private ArrayList<String> pathList = new ArrayList<>();

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
        View root =  inflater.inflate(R.layout.fragment_board_write, container, false);

        SubmitBtn = (Button) root.findViewById(R.id.SubmitBtn);
        TitleEditText = (EditText) root.findViewById(R.id.TitleEditText);
        ContentsEditText = (EditText) root.findViewById(R.id.ContentsEditText);
        QuestionRdb = (RadioButton) root.findViewById(R.id.QuestionRdb);
        BoastRdb = (RadioButton) root.findViewById(R.id.BoastRdb);
        SelectPicBtn = (Button) root.findViewById(R.id.SelectPicBtn);
        imageView = (ImageView) root.findViewById(R.id.imageView);
        parent = root.findViewById(R.id.ImageContents);

        SelectPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)    {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    } else {
                        startToast("권한을 허용해 주세요.");
                    }
                }   else {
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

    public void onRequestPermissionsResults(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                    startActivity(intent);
                }  else {
                    startToast("권한을 허용해 주세요.");
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == Activity.RESULT_OK)   {
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);
                    // DB에 사진 포함 코드 작성 부분

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(activity).load(profilePath).override(800).into(imageView);
                    parent.addView(imageView);
                }
                break;
        }
    }

    private void submit()   {
        Title = TitleEditText.getText().toString();
        Contents = ContentsEditText.getText().toString();

        if (Title.length() == 0 || Contents.length() == 0 || !BoastRdb.isChecked() && !QuestionRdb.isChecked()) {
            startToast("모든 항목을 입력해주세요.");
        }
        else {
            if (QuestionRdb.isChecked())
                Category = "[코디 질문]";
            else
                Category = "[코디 자랑]";

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("게시글 작성");
            builder.setMessage("게시글을 작성하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
                            SimpleDateFormat nFormat = new SimpleDateFormat("yy년 MM월 dd일 HH:mm");
                            String time = nFormat.format(date);
                            String time2 = mFormat.format(date);

                            WriteBoardInfo writeBoardInfo = new WriteBoardInfo(Category, Title, Contents, "Writer", time, time2);
                            db.collection("Boards").document().set(writeBoardInfo);

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
        }
    }

    private void startToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}