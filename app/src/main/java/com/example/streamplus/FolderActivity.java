package com.example.streamplus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity implements FolderAdapter.itemClicked
{

    RecyclerView rvFolder;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView tvWelcome, tvLoading;
    ProgressBar progressBar2;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;

    ArrayList<String> folders = new ArrayList<>();

    private long backPressedTime = 0;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Stream Plus");

        firebaseAuth = FirebaseAuth.getInstance();

        tvWelcome = findViewById(R.id.tvWelcome);
        tvLoading = findViewById(R.id.tvLoading);
        progressBar2 = findViewById(R.id.progressBar2);

        rvFolder = findViewById(R.id.rvFolder);
        rvFolder.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvFolder.setLayoutManager(layoutManager);

        tvLoading.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        tvWelcome.setVisibility(View.GONE);
        rvFolder.setVisibility(View.GONE);
        setData();

    }

    private void setData()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                tvWelcome.setText(String.format("Welcome back, %s", name));

                for(DataSnapshot snapshot : dataSnapshot.child("mappings").getChildren()) {
                    folders.add(snapshot.getValue(String.class));
                }

                myAdapter = new FolderAdapter(FolderActivity.this, folders);
                rvFolder.setAdapter(myAdapter);

                tvWelcome.setVisibility(View.VISIBLE);
                rvFolder.setVisibility(View.VISIBLE);
                tvLoading.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                tvLoading.setVisibility(View.GONE);
                progressBar2.setVisibility(View.GONE);
                Toast.makeText(FolderActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(FolderActivity.this, VideoActivity.class);
        intent.putExtra("folder_name", folders.get(index));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            finishAffinity();
            finishActivity(1);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sign Out ?");
        builder.setMessage("Please confirm your signing out");
        builder.setPositiveButton("Yes, sign out", (dialog, which) -> {
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
        });
        builder.setNegativeButton("No, cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return super.onOptionsItemSelected(item);
    }
}