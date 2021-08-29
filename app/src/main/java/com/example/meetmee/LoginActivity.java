package com.example.meetmee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailBox, passwordBox;
    Button loginBtn,signupBtn;

    ProgressDialog dialog;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();

        emailBox=findViewById(R.id.emailBox);
        passwordBox=findViewById(R.id.passwordBox);

        loginBtn=findViewById(R.id.loginBtn);
        signupBtn=findViewById(R.id.createBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();
                if ((email.trim().length() > 0) && (password.trim().length() > 0)) {
                    dialog.show();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                getMeetingId(auth.getCurrentUser().getUid());
                                //startActivity(new Intent(LoginActivity.this, DashboardScreen.class));

                            } else {

                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
                }else{
                    Toast.makeText(LoginActivity.this,"All fields are required!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SigninActivity.class));
            }
        });
    }

    //getting the meetingId........
    public void getMeetingId(String userId){
        DocumentReference documentreference=database.collection("Users").document(userId);
        documentreference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("meetingID","val=================== "+value.getString("meetingId"));
                Intent intent = new Intent(LoginActivity.this, DashboardScreen.class);
                intent.putExtra("meetingId",value.getString("meetingId"));
                intent.putExtra("loggeduser",value.getString("name"));
                startActivity(intent);

            }
        });
    }
}