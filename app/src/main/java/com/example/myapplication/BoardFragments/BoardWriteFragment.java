package com.example.myapplication.BoardFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.info.WriteBoardInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardWriteFragment extends Fragment {
    MainActivity activity;
    Button SubmitBtn;
    EditText TitleEditText, ContentsEditText;
    RadioButton QuestionRdb, BoastRdb;
    String Title, Contents, Category;

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

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        return root;
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