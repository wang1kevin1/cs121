package com.cmps121.asgn2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmps121.asgn2.R;
import com.cmps121.asgn2.models.Item;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {

    private ArrayList<Item> itemList;

    Context context;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public ImageView vImage;
        public TextView vId;

        public ItemViewHolder(View v) {
            super(v);
            vTitle = v.findViewById(R.id.vTitle);
            vImage = v.findViewById(R.id.vImage);
            vId    = v.findViewById(R.id.vId);
        }
    }

    public ItemRecyclerAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item, parent, false);

        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // get item from list
        Item item = itemList.get(position);

        // get item attributes
        String title = item.getTitle();
        Bitmap bitmap = item.getBitmap();
        String id = Integer.toString(item.getID());

        // populate view holder
        holder.vTitle.setText(title);
        holder.vImage.setImageBitmap(bitmap);
        holder.vId.setText(id);
    }

    // Return the size of itemList (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (itemList == null) {
            return 0;
        } else {
            return itemList.size();
        }
    }
}