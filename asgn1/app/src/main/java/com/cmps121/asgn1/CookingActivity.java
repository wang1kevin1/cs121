package com.cmps121.asgn1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CookingActivity extends AppCompatActivity {

    private Button mButtonReturn;
    private Button mButtonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        mButtonReturn = findViewById(R.id.buttonReturn);
        mButtonDelete = findViewById(R.id.buttonDelete);

        mButtonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CookingActivity.this, MenuActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivityIfNeeded(myIntent, 0);
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.deleteCooking(); //set boolean values

                Intent myIntent = new Intent(CookingActivity.this, MenuActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivityIfNeeded(myIntent, 0);
            }
        });
    }
}