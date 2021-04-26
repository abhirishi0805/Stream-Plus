package com.example.streamplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>
{
    private ArrayList<String> folders;
    itemClicked activity;

    public FolderAdapter(Context context, ArrayList<String> folders) {
        activity = (itemClicked) context;
        this.folders = folders;
    }

    public interface itemClicked{
        void onItemClicked(int index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.itemView.setTag(folders.get(position));
        holder.tvFolderName.setText(folders.get(position));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFolderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFolderName = itemView.findViewById(R.id.tvFolderName);

            itemView.setOnClickListener(v -> {
                activity.onItemClicked(folders.indexOf(v.getTag()));
            });
        }
    }
}
