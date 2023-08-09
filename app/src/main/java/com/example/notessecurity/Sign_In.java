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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;

    ProgressBar progressBar;
    Button signIn;
    TextView signup;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressBar = findViewById(R.id.progressBar);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.pin);
        signIn = findViewById(R.id.sign_in);
        signup = findViewById(R.id.sign_up);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_In.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, pin;
                email = String.valueOf(editTextEmail.getText());
                pin = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Sign_In.this,"Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pin)){
                    Toast.makeText(Sign_In.this, "Enter PIN", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, pin)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()){
                                    Toast.makeText(Sign_In.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Sign_In.this, MainActivity2.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(Sign_In.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(Sign_In.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(Sign_In.this, "Please Login",Toast.LENGTH_SHORT).show();
        }
    }
}