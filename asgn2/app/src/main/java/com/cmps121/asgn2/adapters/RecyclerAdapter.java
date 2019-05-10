package com.cmps121.asgn2.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmps121.asgn2.R;


import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<Bitmap> recyclerDataset;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public ImageView vImage;

        public RecyclerViewHolder(View v) {
            super(v);
            vTitle = v.findViewById(R.id.vTitle);
            vImage = v.findViewById(R.id.vImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(List<Bitmap> recyclerDataset) {
        this.recyclerDataset = recyclerDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Bitmap bitmap = recyclerDataset.get(position);

        final String title = bitmap

        holder.vTitle.setText();
        //holder image

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (recyclerDataset == null) {
            return 0;
        } else {
            return recyclerDataset.size();
        }
    }
}