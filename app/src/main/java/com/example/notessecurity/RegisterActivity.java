package com.example.notessecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextName, editTextcnfrm;
    Button signup;

    ProgressBar progressBar;
    TextView signin;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.pin);
        editTextcnfrm = findViewById(R.id.cnfrmpin);
        editTextName = findViewById(R.id.name);
        signin = findViewById(R.id.sign_in);
        signup = findViewById(R.id.sign_up);

        progressBar = findViewById(R.id.progressBar);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, Sign_In.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString().trim();
                String pin = editTextPassword.getText().toString().trim();
                String cnfrm = editTextcnfrm.getText().toString().trim();
                String name = editTextName.getText().toString();

                if (TextUtils.isEmpty(name)){
                    editTextName.setError("Enter Name");
                }

                else if (TextUtils.isEmpty(email)){
                    editTextEmail.setError("Enter Email");
                    return;
                }

                else if (TextUtils.isEmpty(pin)){
                    editTextPassword.setError("Enter PIN");
                    return;
                }

                else if (TextUtils.isEmpty(cnfrm)){
                    editTextcnfrm.setError("Confirm PIN");
                }

                else if (!pin.equals(cnfrm)){
                    editTextcnfrm.setError("PIN doesn't match");
                } else {

                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(email, pin)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();

                                        userID = firebaseAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Name", name);
                                        user.put("Email", email);
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                        Intent intent = new Intent(RegisterActivity.this, Sign_In.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Authentication Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });



    }
}