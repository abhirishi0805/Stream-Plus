package com.example.streamplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideoActivity extends AppCompatActivity {

    public static String folder_name;
    FirebaseDatabase database;
    DatabaseReference reference;

    RecyclerView rvVideo;

    String videoName, videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rvVideo = findViewById(R.id.rvVideo);
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        folder_name = intent.getStringExtra("folder_name");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(folder_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("storage_mappings").child(folder_name);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery(reference, Member.class)
                .build();

        FirebaseRecyclerAdapter<Member, videoViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, videoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull videoViewHolder videoViewHolder, int i, @NonNull Member member) {
                        videoViewHolder.setExoplayer(getApplication(), member.getVideoName(), member.getVideoURL());

                        videoViewHolder.setOnClicklistener(new videoViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                videoName = getItem(position).getVideoName();
                                videoURL = getItem(position).getVideoURL();

                                Intent intent = new Intent(VideoActivity.this, FullScreenActivity.class);
                                intent.putExtra("videoName", videoName);
                                intent.putExtra("videoURL", videoURL);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
                        return new videoViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        rvVideo.setAdapter(firebaseRecyclerAdapter);
    }
}