package com.cmps121.asgn2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmps121.asgn2.models.Item;
import com.cmps121.asgn2.utils.DatabaseUtil;

public class DeleteActivity extends AppCompatActivity {

    // database handler
    private DatabaseUtil mDatabaseUtil;

    // EditTexts
    private EditText mEditID;
    private EditText mEditTitle;

    // Buttons
    private Button mButtonDelete;

    // Strings
    private String mID;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        mEditID = findViewById(R.id.deleteEditID);
        mEditTitle = findViewById(R.id.deleteEditTitle);


        mButtonDelete = findViewById(R.id.deleteButtonDelete);

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(v);
            }
        });
    }

    public void deleteImage(View view) {
        // new item
        Item item = new Item();

        // get input
        String id = mEditID.getText().toString().trim();
        String title = mEditTitle.getText().toString().trim();

        if (!id.equals("")) {
            mID = id;
            item.setID(Integer.parseInt(mID));
        }
        if (!title.equals("")) {
            mTitle = title;
            item.setTitle(mTitle);
        }

        if ((mID == null) && (mTitle == null)) {
            Toast.makeText(DeleteActivity.this, "Please Specify ID and/or Title", Toast.LENGTH_LONG).show();
        } else {
            mDatabaseUtil = new DatabaseUtil(DeleteActivity.this);

            if (mDatabaseUtil.deleteItem(item)) {
                Toast.makeText(DeleteActivity.this, "Image(s) Deleted", Toast.LENGTH_LONG).show();

                DeleteActivity.this.finish();
            } else {
                Toast.makeText(DeleteActivity.this, "No Image(s) Matched ID or Title", Toast.LENGTH_LONG).show();
            }
        }
    }
}
