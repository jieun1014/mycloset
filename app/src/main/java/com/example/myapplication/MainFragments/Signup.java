package com.example.myapplication.MainFragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Signup extends AppCompatActivity {
    private static final String TAG = "MyTag";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextNickname;
    private Button buttonJoin;

    private String nickname;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextNickname = (EditText) findViewById(R.id.editText_nickname);
        buttonJoin = (Button) findViewById(R.id.btn_join);

        //회원가입 버튼 클릭 시
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
//                    // 이메일과 비밀번호가 공백이 아닌 경우
//                    createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextNickname.getText().toString());
//                } else {
//                    // 이메일과 비밀번호가 공백인 경우
//                    Toast.makeText(Signup.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
//                }
                signup();
            }
        });
    }

    private void signup() {
        nickname = editTextNickname.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(nickname)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)) {
            Toast.makeText(Signup.this, "정보를 바르게 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserDto userDto = new UserDto();

                                userDto.setEmail(email);
                                userDto.setNickname(nickname);
                                userDto.setPassword(password);
                                userDto.setUid(task.getResult().getUser().getUid());

                                firebaseFirestore.collection("UserInfo")
                                        .add(userDto)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID:" + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                                Toast.makeText(Signup.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                if (task.getException() != null) { // 회원가입 실패시
                                    Toast.makeText(Signup.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        } catch (Exception e) {
            String errorMessage = e.toString();
            Log.w(TAG, "Exception 발생 !! : " + errorMessage);
        }

    }
}