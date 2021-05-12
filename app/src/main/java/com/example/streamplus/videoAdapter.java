package com.example.streamplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.ViewHolder>
{
    private ArrayList<videoModel> videos;
    videoAdapter.itemClicked activity;

    public videoAdapter(Context context, ArrayList<videoModel> videos) {
        activity = (videoAdapter.itemClicked) context;
        this.videos = videos;
    }

    public interface itemClicked{
        void onItemClicked(int index);
    }

    @NonNull
    @Override
    public videoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new videoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull videoAdapter.ViewHolder holder, int position)
    {
        holder.itemView.setTag(videos.get(position));
        holder.tvVideoName.setText(videos.get(position).getVideoName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvVideoName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvVideoName = itemView.findViewById(R.id.tvVideoName);

            itemView.setOnClickListener(v -> {
                activity.onItemClicked(videos.indexOf(v.getTag()));
            });
        }
    }
}
