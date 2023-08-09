package com.example.notessecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity3 extends AppCompatActivity {

    FloatingActionButton addnote;

    DetailsAdapter detailsAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        addnote = findViewById(R.id.add_note);
        recyclerView = findViewById(R.id.recycle_view);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecyclerView();

    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Details> options = new FirestoreRecyclerOptions.Builder<Details>()
                .setQuery(query,Details.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailsAdapter = new DetailsAdapter(options, this);
        recyclerView.setAdapter(detailsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        detailsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        detailsAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Sign_In.class));
            finish();
        }
        return true;
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

    public void showPopup(View view) {
        startActivity(new Intent(this, Notes.class));
    }


}