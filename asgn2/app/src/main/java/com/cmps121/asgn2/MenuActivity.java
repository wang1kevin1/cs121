package com.cmps121.asgn2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    // Menu buttons
    private Button mButtonDownload;
    private Button mButtonDelete;
    private Button mButtonView;
    private Button mButtonRange;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize menu buttons
        mButtonDownload = findViewById(R.id.menuButtonDownload);
        mButtonDelete = findViewById(R.id.menuButtonDelete);
        mButtonView = findViewById(R.id.menuButtonView);
        mButtonRange = findViewById(R.id.menuButtonRange);

        mButtonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent =
                            new Intent(MenuActivity.this, DownloadActivity.class);
                    startActivity(myIntent);
                }
            });

        mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =
                        new Intent(MenuActivity.this, ViewActivity.class);
                startActivity(myIntent);
            }
        });

        /*
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent =
                            new Intent(MenuActivity.this, DeleteActivity.class);
                    finish();
                    startActivity(myIntent);
                }
            });


        // button to exit
        mButtonRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =
                        new Intent(MenuActivity.this, RangeActivity.class);
                finish();
                startActivity(myIntent);
            }
        });
        */
    }
}
