package com.example.streamplus;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideoActivity extends AppCompatActivity {

    public static String folder_name;
    FirebaseDatabase database;
    DatabaseReference reference;

    RecyclerView rvVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rvVideo = findViewById(R.id.rvVideo);
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        folder_name = intent.getStringExtra("folder_name");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("storage_mappings").child(folder_name);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Member, videoViewHolder>firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, videoViewHolder>(
                        Member.class,
                        R.layout.video_item,
                        videoViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(videoViewHolder videoViewHolder, Member member, int i) {
                        videoViewHolder.setExoplayer(getApplication(), member.getVideoName(), member.getVideoURL());
                    }
                };

        rvVideo.setAdapter(firebaseRecyclerAdapter);
    }
}