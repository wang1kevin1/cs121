package com.cmps121.asgn2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cmps121.asgn2.adapters.ItemRecyclerAdapter;
import com.cmps121.asgn2.models.Item;
import com.cmps121.asgn2.utils.DatabaseUtil;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    // database handler
    private DatabaseUtil mDatabaseUtil;

    // RecyclerView
    private RecyclerView itemRecyclerView;

    // Adapter + layout manager
    private ItemRecyclerAdapter itemAdapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mDatabaseUtil = new DatabaseUtil(ViewActivity.this);

        itemRecyclerView = findViewById(R.id.viewRecyclerView);

        // populate items
        itemList = new ArrayList<Item>(mDatabaseUtil.getAllItems());

        // specify an adapter
        itemAdapter = new ItemRecyclerAdapter(itemList, getApplication());

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(ViewActivity.this,
                LinearLayoutManager.VERTICAL, false);
        itemRecyclerView.setLayoutManager(layoutManager);

        // set adapter
        itemRecyclerView.setAdapter(itemAdapter);
    }
}
