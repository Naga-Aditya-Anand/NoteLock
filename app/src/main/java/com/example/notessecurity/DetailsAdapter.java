package com.example.notessecurity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

 public class DetailsAdapter extends FirestoreRecyclerAdapter<Details, DetailsAdapter.DetailViewHolder> {
    Context context;


    public DetailsAdapter(@NonNull FirestoreRecyclerOptions<Details> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DetailViewHolder holder, int position, @NonNull Details details) {

        holder.titlemain1.setText(details.title);
        try {
            //holder.titlemain2.setText(details.title);
            //String asd = holder.titlemain2.getText().toString();
            holder.titlemain1.setText(EncryptDecypt.decrypt(holder.titlemain1.getText().toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        holder.timemain1.setText(Utility.timestampToString(details.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context, Notes.class);
                intent.putExtra("Title",details.title);
                intent.putExtra("Account",details.account);
                String docId = DetailsAdapter.this.getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
                intent.putExtra("Password",details.password);
                intent.putExtra("docId",docId);
                context.startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new DetailViewHolder(view);
    }

    class DetailViewHolder extends RecyclerView.ViewHolder{

        TextView titlemain1, timemain1, titlemain2;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            titlemain1 = itemView.findViewById(R.id.titlemain);
            titlemain2 = itemView.findViewById(R.id.titlemain2);
            timemain1 = itemView.findViewById(R.id.timemain);


        }
    }

}
