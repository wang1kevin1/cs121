package com.cmps121.asgn2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmps121.asgn2.adapters.ItemRecyclerAdapter;
import com.cmps121.asgn2.models.Item;
import com.cmps121.asgn2.utils.DatabaseUtil;

import java.util.ArrayList;

public class RangeActivity extends AppCompatActivity {

    // database handler
    private DatabaseUtil mDatabaseUtil;

    // RecyclerView
    private RecyclerView itemRecyclerView;

    // EditTexts
    private EditText mEditStart;
    private EditText mEditEnd;

    // Button
    private Button mButtonDisplay;

    // Adapter + layout manager
    private ItemRecyclerAdapter itemAdapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<Item> itemList;
    private int mStart;
    private int mEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);

        mDatabaseUtil = new DatabaseUtil(RangeActivity.this);

        mEditStart = findViewById(R.id.rangeEditStart);
        mEditEnd = findViewById(R.id.rangeEditEnd);

        itemRecyclerView = findViewById(R.id.viewRecyclerView);

        mButtonDisplay = findViewById(R.id.rangeButtonDisplay);

        mButtonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayItem(v);

                // specify an adapter
                itemAdapter = new ItemRecyclerAdapter(itemList, getApplication());

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(RangeActivity.this,
                        LinearLayoutManager.VERTICAL, false);
                itemRecyclerView.setLayoutManager(layoutManager);

                // set adapter
                itemRecyclerView.setAdapter(itemAdapter);
            }
        });

    }

    public void displayItem(View view){
        // clear items
        itemList = null;

        // get range
        String start = mEditStart.getText().toString().trim();
        String end = mEditEnd.getText().toString().trim();

        // check that range isn't empty
        if(!start.isEmpty() && !end.isEmpty()) {
            mStart = Integer.parseInt(start);
            mEnd = Integer.parseInt(end);

            // invalid range
            if (mStart > mEnd)
                Toast.makeText(RangeActivity.this, "Invalid Range", Toast.LENGTH_LONG).show();
            else {
                // populate items
                itemList = new ArrayList<Item>(mDatabaseUtil.getRangeItems(mStart, mEnd));
            }
        } else { // empty range fields
            Toast.makeText(RangeActivity.this, "Please Enter a Range", Toast.LENGTH_LONG).show();
        }
    }
}
