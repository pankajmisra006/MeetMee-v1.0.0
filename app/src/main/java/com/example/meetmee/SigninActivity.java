package com.example.meetmee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class SigninActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore database;
    EditText emailBox, passwordBox,nameBox;
    Button loginBtn,signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        emailBox=findViewById(R.id.emailBox);
        nameBox=findViewById(R.id.namebox);
        passwordBox=findViewById(R.id.passwordBox);

        loginBtn=findViewById(R.id.loginBtn);
        signupBtn=findViewById(R.id.createBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninActivity.this,LoginActivity.class));
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email;
                String password;
                String name;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();
                name = nameBox.getText().toString();

                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                user.setName(name);
                user.setMeetingId(getMeetingId());   //this generates the meeting Id

                if ((email.trim().length() > 0) && (password.trim().length() > 0) && (name.trim().length()>0)) {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId;
                                userId =auth.getCurrentUser().getUid();
                                database.collection("Users")
                                        .document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(SigninActivity.this, LoginActivity.class));

                                    }
                                });
                                Toast.makeText(SigninActivity.this, "Account is Created, Please login!", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(SigninActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(SigninActivity.this,"All fields are required!",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public static String getMeetingId() {
        return RandomStringUtils.randomNumeric(8);
    }
}