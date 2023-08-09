package com.example.notessecurity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity2 extends AppCompatActivity {

    TextView username, relgn, em;



    Button finlsgn;
    TextInputEditText pinfnl;

    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        pinfnl = findViewById(R.id.pinfinal);
        finlsgn = findViewById(R.id.Final_si);

        progressBar = findViewById(R.id.progressBar);

        username = findViewById(R.id.user);
        em = findViewById(R.id.em);
        relgn = findViewById(R.id.relgn);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    username.setText(documentSnapshot.getString("Name"));
                    em.setText(documentSnapshot.getString("Email"));
                }
            }
        });
        //documentReference.addSnapshotListener(MainActivity2.this, new EventListener<DocumentSnapshot>() {
          //  @Override
         //   public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
          //      username.setText(documentSnapshot.getString("Name"));
           //     em.setText(documentSnapshot.getString("Email"));
          //  }
       // });

        finlsgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String passcode, email;

                email = String.valueOf(em.getText());
                passcode = String.valueOf(pinfnl.getText());

                if (TextUtils.isEmpty(passcode)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity2.this, "Enter PIN", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, passcode)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity2.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(MainActivity2.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }

        });


    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Sign_In.class));
        finish();
    }


}