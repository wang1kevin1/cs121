package com.cmps121.asgn2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cmps121.asgn2.models.Item;
import com.cmps121.asgn2.utils.DatabaseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class DownloadActivity extends AppCompatActivity {

    // database handler
    private DatabaseUtil mDatabaseUtil;

    // EditTexts
    private EditText mEditURL;
    private EditText mEditTitle;

    // Progress Bar
    private ProgressBar mProgressBar;

    // Buttons
    private Button mButtonDownload;

    // Strings
    private String mURL;
    private String mTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mDatabaseUtil = new DatabaseUtil(DownloadActivity.this);

        mEditURL = findViewById(R.id.downloadEditURL);
        mEditTitle = findViewById(R.id.downloadEditTitle);

        mProgressBar = findViewById(R.id.downloadProgressBar);

        mButtonDownload = findViewById(R.id.downloadButtonDownload);

        mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    downloadImage(v);
                } else {
                    Toast.makeText(DownloadActivity.this, "Internet Connectivity Issues", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void downloadImage(View view){
        DownloadTask downloadTask = new DownloadTask();
        mURL = mEditURL.getText().toString().trim();
        mTitle = mEditTitle.getText().toString().trim();

        if(!mURL.trim().equals("")) {
            downloadTask.execute(mURL);
        }
    }

    // https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // https://stackoverflow.com/questions/5776851/load-image-from-url
    // https://stackoverflow.com/questions/29188557/download-file-with-asynctask
    // Asynchronous task to run download an image in background
    private class DownloadTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String...urls){

            Bitmap bitmap;

            try {
                // download the image
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // open input stream
                InputStream inputStream = connection.getInputStream();

                // decode to smaller image
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeStream(inputStream,null, options);

                // close input stream
                inputStream.close();
            }
            catch(IOException e) {
                return null;
            }
            return bitmap;
        }

        protected void onPreExecute(){
            // toast
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(DownloadActivity.this, "Downloading...", Toast.LENGTH_LONG).show();
                }
            });
        }

        protected void onPostExecute(Bitmap result){
            mProgressBar.setVisibility(ProgressBar.VISIBLE);

            if(result != null) {
                // create a new item
                Item item = new Item();
                item.setTitle(mTitle);
                item.setBitmap(result);

                // insert item into db using util
                mDatabaseUtil.addItem(item);

                // toast
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(DownloadActivity.this, "Download Successful", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                // toast
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(DownloadActivity.this, "Download Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
            DownloadActivity.this.finish();

        }

    }

}