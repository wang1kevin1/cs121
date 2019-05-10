package com.cmps121.asgn1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private SharedPreferences sSharedPreferences;

    // Booleans to check if deleted/modified
    private static boolean sModified;
    private static boolean sDeleteGaming;
    private static boolean sDeleteCooking;
    private static boolean sDeleteSkiing;

    // Menu buttons
    private Button sButtonGaming;
    private Button sButtonCooking;
    private Button sButtonSkiing;
    private Button sButtonExit;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sSharedPreferences = this.getPreferences(MODE_PRIVATE);

        // if modification has been made, refresh preferences
        if(sModified) {
            sSharedPreferences.edit()
                    .putBoolean(getString(R.string.activity_gaming), sDeleteGaming)
                    .putBoolean(getString(R.string.activity_cooking), sDeleteCooking)
                    .putBoolean(getString(R.string.activity_skiing), sDeleteSkiing)
                    .apply();
            sModified = false;
        }

        // get preferences
        sDeleteGaming =
                sSharedPreferences.getBoolean(getString(R.string.activity_gaming), sDeleteGaming);
        sDeleteCooking =
                sSharedPreferences.getBoolean(getString(R.string.activity_cooking), sDeleteCooking);
        sDeleteSkiing =
                sSharedPreferences.getBoolean(getString(R.string.activity_skiing), sDeleteSkiing);

        // Initialize menu buttons
        sButtonGaming = findViewById(R.id.buttonGaming);
        sButtonCooking = findViewById(R.id.buttonCooking);
        sButtonSkiing = findViewById(R.id.buttonSkiing);
        sButtonExit = findViewById(R.id.buttonExit);

        // hide button if it's been deleted
        if(sDeleteGaming)
            sButtonGaming.setVisibility(View.INVISIBLE);
        else
            sButtonGaming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent =
                            new Intent(MenuActivity.this, GamingActivity.class);
                    finish();
                    startActivity(myIntent);
                }
            });

        // hide button if it's been deleted
        if(sDeleteCooking)
            sButtonCooking.setVisibility(View.INVISIBLE);
        else
            sButtonCooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent =
                            new Intent(MenuActivity.this, CookingActivity.class);
                    finish();
                    startActivity(myIntent);
                }
            });

        // hide button if it's been deleted
        if(sDeleteSkiing)
            sButtonSkiing.setVisibility(View.INVISIBLE);
        else
            sButtonSkiing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent =
                            new Intent(MenuActivity.this, SkiingActivity.class);
                    finish();
                    startActivity(myIntent);
                }
            });

        // button to exit
        sButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sSharedPreferences.edit()
                        .putBoolean(getString(R.string.activity_gaming), sDeleteGaming)
                        .putBoolean(getString(R.string.activity_cooking), sDeleteCooking)
                        .putBoolean(getString(R.string.activity_skiing), sDeleteSkiing)
                        .apply();
                finish();
                System.exit(0);
            }
        });
    }

    // maintain preferences even when out of focus
    @Override
    protected void onPause() {
        super.onPause();
        sSharedPreferences.edit()
                .putBoolean(getString(R.string.activity_gaming), sDeleteGaming)
                .putBoolean(getString(R.string.activity_cooking), sDeleteCooking)
                .putBoolean(getString(R.string.activity_skiing), sDeleteSkiing)
                .apply();
    }

    // sets values on deletion
    public static void deleteGaming() {
        sDeleteGaming = true;
        sModified = true;
    }

    // sets values on deletion
    public static void deleteCooking() {
        sDeleteCooking = true;
        sModified = true;
    }

    // sets values on deletion
    public static void deleteSkiing() {
        sDeleteSkiing = true;
        sModified = true;
    }
}
