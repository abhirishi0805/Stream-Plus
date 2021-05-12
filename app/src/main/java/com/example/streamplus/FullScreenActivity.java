package com.example.streamplus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FullScreenActivity extends AppCompatActivity {

    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    TextView tvFullScreen, tvDescription;
    private String videoURL, videoName, videoDesc;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    boolean fullscreen = false;
    ImageView fullscreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        playerView = findViewById(R.id.exoplayer_fullscreen);
        tvFullScreen = findViewById(R.id.tvFullScreen);
        tvDescription = findViewById(R.id.tvDescription);

        fullscreenButton = playerView.findViewById(R.id.exoplayer_fullscreen_icon);

        Intent intent = getIntent();
        videoURL = intent.getStringExtra("videoURL");
        videoName = intent.getStringExtra("videoName");
        videoDesc = intent.getStringExtra("videoDesc");

        tvFullScreen.setText(videoName);
        tvDescription.setText(videoDesc);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullscreen){
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreenActivity.this, R.drawable.ic_fullscreen_expand));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                    tvFullScreen.setVisibility(View.VISIBLE);
                    tvDescription.setVisibility(View.VISIBLE);
                }
                else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreenActivity.this, R.drawable.ic_fullscreen_shrink));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                    tvFullScreen.setVisibility(View.INVISIBLE);
                    tvDescription.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private MediaSource buildMediaSource(Uri uri)
    {
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(VideoActivity.folder_name);
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void initializePlayer()
    {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(exoPlayer);
        Uri uri = Uri.parse(videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentWindow, playbackPosition);
        exoPlayer.prepare(mediaSource, false, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Util.SDK_INT >= 26) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Util.SDK_INT >= 26 || exoPlayer == null) {
            exoPlayer.setPlayWhenReady(true);
            //initializePlayer(); //added
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Util.SDK_INT > 26)
            exoPlayer.setPlayWhenReady(false);
            //releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(Util.SDK_INT >= 26) {
            exoPlayer.setPlayWhenReady(false);
            //exoPlayer.stop();  //added
            //releasePlayer();
        }
    }

    private void releasePlayer()
    {
        if(exoPlayer != null)
        {
            playWhenReady = exoPlayer.getPlayWhenReady();
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            exoPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //exoPlayer.stop();
        //releasePlayer();
        exoPlayer.setPlayWhenReady(false);
    }
}