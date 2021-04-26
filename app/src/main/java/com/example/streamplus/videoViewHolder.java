package com.example.streamplus;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.streamplus.VideoActivity.folder_name;

public class videoViewHolder extends RecyclerView.ViewHolder
{
    View mView;
    SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    TextView tvVideoName;

    public videoViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setExoplayer(final Application application, String videoName, final String videoURL)
    {
        tvVideoName = mView.findViewById(R.id.tvVideoName);
        playerView = mView.findViewById(R.id.exoplayer_item);

        tvVideoName.setText(videoName);
        try
        {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application);
            Uri video = Uri.parse(videoURL);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(folder_name);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }
        catch (Exception e){
            Log.e("videoViewHolder", "Exoplayer error : " + e.getMessage());
        }
    }
}
