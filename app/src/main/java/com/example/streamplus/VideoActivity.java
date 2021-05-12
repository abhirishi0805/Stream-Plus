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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements videoAdapter.itemClicked
{

    public static String folder_name;
    FirebaseDatabase database;
    DatabaseReference reference;

    RecyclerView rvVideo;
    RecyclerView.Adapter adapter;

    ArrayList<videoModel> videos;
    String videoName, videoURL, videoDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rvVideo = findViewById(R.id.rvVideo);
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        folder_name = intent.getStringExtra("folder_name");

        videos = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(folder_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setData();
    }

    private void setData()
    {
        videos.clear();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("storage_mappings").child(folder_name);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    videoModel video = dataSnapshot.getValue(videoModel.class);
                    videos.add(video);
                }
                adapter = new videoAdapter(VideoActivity.this, videos);
                rvVideo.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onItemClicked(int index)
    {
        videoName = videos.get(index).getVideoName();
        videoDesc = videos.get(index).getVideoDesc();
        videoURL = videos.get(index).getVideoURL();

        Intent intent2 = new Intent(VideoActivity.this, FullScreenActivity.class);
        intent2.putExtra("videoName", videoName);
        intent2.putExtra("videoURL", videoURL);
        intent2.putExtra("videoDesc", videoDesc);
        startActivity(intent2);
    }
}