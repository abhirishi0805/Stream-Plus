package com.example.streamplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView tvStream, tvPlus;
    CardView cvLoginCard;
    EditText etEmail, etPassword;
    Button btnSubmit;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private long backPressedTime = 0;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStream = findViewById(R.id.tvStream);
        tvPlus = findViewById(R.id.tvPlus);
        cvLoginCard = findViewById(R.id.cvLoginCard);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);


        tvStream.setTranslationY(-800f);
        tvPlus.setTranslationY(-400f);
        cvLoginCard.setTranslationY(400f);
        tvStream.animate().translationYBy(800f);
        tvPlus.animate().translationYBy(400f);
        cvLoginCard.animate().translationYBy(-400f);


        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(MainActivity.this, "Please fill the fields!", Toast.LENGTH_SHORT).show();
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Successfully Loged In", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, FolderActivity.class));
                                } else {
                                    Toast.makeText(MainActivity.this, "LogIn Failed or User Not Available", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, FolderActivity.class));
        }
    }


    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            finishAffinity();
            finishActivity(1);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}