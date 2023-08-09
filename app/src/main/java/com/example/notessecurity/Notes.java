package com.example.notessecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Notes extends AppCompatActivity {

    Button done;
    TextInputEditText title, password, account;

    TextView pagetitle, delete;
    String titlest, accountst, docId, passwordst;

    ProgressBar progressBar;

    LinearLayout notesact;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    private String stringNote;
    private byte encryptionKey[] = {2, 49, 54, (byte) 257, (byte) 564, (byte) 428, (byte) -49823, (byte) 584, 20, 55, 124, (byte) 258, (byte) 458, 48, (byte) 456, 21};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        BiometricManager biometricManager= BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),"Device doesn't support Biometric",Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),"Biometric Not Working",Toast.LENGTH_SHORT).show();

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(),"Biometric is not enrolled",Toast.LENGTH_SHORT).show();
        }

        Executor executor= ContextCompat.getMainExecutor(this);

        biometricPrompt=new BiometricPrompt(Notes.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Biometric Success",Toast.LENGTH_SHORT).show();
                notesact.setVisibility(View.VISIBLE);


            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("NOTELOCK")
                .setDescription("Use your Fingerprint").setDeviceCredentialAllowed(true).build();

        biometricPrompt.authenticate(promptInfo);

        done = findViewById(R.id.done);
        title = findViewById(R.id.title1);
        password = findViewById(R.id.password1);
        account = findViewById(R.id.account1);
        pagetitle = findViewById(R.id.page_title);
        delete = findViewById(R.id.delete);

        notesact = findViewById(R.id.notesact);

        progressBar = findViewById(R.id.progressBar);



        titlest = getIntent().getStringExtra("Title");
        accountst = getIntent().getStringExtra("Account");
        docId = getIntent().getStringExtra("docId");
        passwordst = getIntent().getStringExtra("Password");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
            done.setText("EDIT");
            pagetitle.setText("Edit your Note");
            delete.setVisibility(View.VISIBLE);
        }

        title.setText(EncryptDecypt.decrypt(titlest));
        account.setText(EncryptDecypt.decrypt(accountst));
        password.setText(EncryptDecypt.decrypt(passwordst));


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                deleteNoteFromFirebase();
            }
        });

        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

    }

    void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);




        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Utility.showToast(Notes.this, "Details Deleted");
                    startActivity(new Intent(Notes.this, MainActivity3.class));
                }else {
                    Utility.showToast(Notes.this, "Failed to Delete Details");
                }
            }
        });
    }

    void saveNote() {

        try {
            String encryptedtitle = EncryptDecypt.encrypt(title.getText().toString());
            String encryptedaccount = EncryptDecypt.encrypt(account.getText().toString());
            String encryptedpassword = EncryptDecypt.encrypt(password.getText().toString());
            Details details;
            if (TextUtils.isEmpty(title.getText().toString())) {
                title.setError("Enter Title");
            } else if (TextUtils.isEmpty(account.getText().toString())) {
                account.setError("Enter Account Address");
            } else if (TextUtils.isEmpty(password.getText().toString())) {
                password.setError("Enter Password");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                details = new Details();
                details.setTitle(encryptedtitle);
                details.setAccount(encryptedaccount);
                details.setPassword(encryptedpassword);
                details.setTimestamp(Timestamp.now());

                saveNoteToFirebase(details);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //String notetitle = EncryptDecrypt.encrypt(.title.getText().toString());
       // String noteaccount = account.getText().toString();
        //String notepassword = password.getText().toString();

        //Details details;
        //if (TextUtils.isEmpty(notetitle)) {
        //    title.setError("Enter Title");
       // } else if (TextUtils.isEmpty(noteaccount)) {
       //     account.setError("Enter Account Address");
        //} else if (TextUtils.isEmpty(notepassword)) {
          //  password.setError("Enter Password");
        //} else {
          //  progressBar.setVisibility(View.VISIBLE);
          //  details = new Details();
         //   details.setTitle();
          //  details.setAccount(noteaccount);
          //  details.setPassword(notepassword);
         //   details.setTimestamp(Timestamp.now());

           // saveNoteToFirebase(details);

        //}



    }


    void saveNoteToFirebase(Details details) {
        DocumentReference documentReference;

        if (isEditMode){
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        }else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                     Utility.showToast(Notes.this, "Details Saved");
                     startActivity(new Intent(Notes.this, MainActivity3.class));
                }else {
                    Utility.showToast(Notes.this, "Failed to save Details");
                }
            }
        });
    }

    public void close(View view) {
        startActivity(new Intent(this, MainActivity3.class));
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(this, MainActivity2.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}