package com.example.myapplication.MyFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class MyInfoFragment extends Fragment {
    MainActivity activity;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText nickname;
    private TextView email;
    private Button editText_nickname_btn, modeify_email_btn, modeify_password_btn;

    private String Nickname = "익명", Email ="이메일", Uid, Did;


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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_information, container, false);

        nickname = (EditText) root.findViewById(R.id.editText_nickname);
        email = (TextView) root.findViewById(R.id.editText_email);

        editText_nickname_btn = (Button) root.findViewById(R.id.editText_nickname_btn);

        modeify_email_btn = (Button) root.findViewById(R.id.modeify_Email_btn);
        modeify_password_btn = (Button) root.findViewById(R.id.create_New_Password_btn);

        getUserInfo();

        editText_nickname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nickname.getText().toString().isEmpty() && nickname.getText().toString().length() <= 6) {
                    DocumentReference editUserNickname = firebaseFirestore.collection("UserInfo").document(Did);
                    editUserNickname
                            .update("nickname", nickname.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Nickname Edit Result", "DocumentSnapshot successfully updated!");
                                    Toast.makeText(getContext(), "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Nickname Edit Result", "Error updating document", e);
                                    Toast.makeText(getContext(), "닉네임 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "닉네임 설정 규칙을 준수하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        modeify_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        modeify_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }

    private void getUserInfo() {
        firebaseFirestore.collection("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("uid").toString().equals(user.getUid())) {
                                    nickname.setText(document.getData().get("nickname").toString());
                                    email.setText(document.getData().get("email").toString());
                                    Uid = user.getUid();
                                    Did = document.getId();
                                }
                            }
                        } else {
                            Nickname = "";
                            Email = "";
                            Uid = user.getUid();
                            Did = "";
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Error !", e);
            }
        });
    }
}
