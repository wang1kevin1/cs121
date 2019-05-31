package com.cmps121.asgn3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements com.cmps121.asgn3.MyServiceTask.ResultCallback {

    public static final int DISPLAY_NUMBER = 10;
    private Handler mUiHandler;

    private static final String LOG_TAG = "MainActivity";

    // Service connection variables.
    private boolean serviceBound;
    private MyService myService;

    private TextView mTextMessage;

    private Button mButtonClear;
    private Button mButtonExit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUiHandler = new Handler(getMainLooper(), new UiCallback());
        serviceBound = false;

        mTextMessage = findViewById(R.id.mainTextMessage);
        mTextMessage.setText("Everything was quiet");

        mButtonClear = findViewById(R.id.mainButtonClear);
        mButtonExit = findViewById(R.id.mainButtonExit);

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextMessage.setText("Everything was quiet");
                clear();
            }
        });

        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Starts the service, so that the service will only stop when explicitly stopped.
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindMyService();

        if (serviceBound) {
            didItMove();
        }
    }

    private void bindMyService() {
        // Binds to the service.
        Log.i(LOG_TAG, "Starting the service");
        Intent intent = new Intent(this, MyService.class);
        Log.i("LOG_TAG", "Trying to bind");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    // Service connection code.
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            // We have bound to the camera service.
            MyService.MyBinder binder = (MyService.MyBinder) serviceBinder;
            myService = binder.getService();
            serviceBound = true;
            // Let's connect the callbacks.
            Log.i("MyService", "Bound succeeded, adding the callback");
            myService.updateResultCallback(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };
    /**
     * Here is an example of a method that calls something in the service.
     */
    boolean didItMove() {
        return myService.didItMove();
    }

    void clear() {
        myService.clear();
    }

    @Override
    protected void onPause() {
        if (serviceBound) {
            Log.i("MyService", "Unbinding");
            unbindService(serviceConnection);
            serviceBound = false;
        }
        super.onPause();
    }

    /**
     * This function is called from the service thread.  To process this, we need
     * to create a message for a handler in the UI thread.
     */
    @Override
    public void onResultReady(ServiceResult result) {
        if (result != null) {
            Log.i(LOG_TAG, "Preparing a message for " + result.moved);
        } else {
            Log.e(LOG_TAG, "Received an empty result!");
        }
        mUiHandler.obtainMessage(DISPLAY_NUMBER, result).sendToTarget();
    }

    /**
     * This Handler callback gets the message generated above.
     * It is used to display the integer on the screen.
     */
    private class UiCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == DISPLAY_NUMBER) {
                // Gets the result.
                ServiceResult result = (ServiceResult) message.obj;
                // Displays it.
                if (result != null) {
                    Log.i(LOG_TAG, "Displaying: " + result.moved);
                    if (result.moved) {
                        mTextMessage.setText("The phone moved!");
                    }
                    // Tell the worker that the bitmap is ready to be reused
                } else {
                    Log.e(LOG_TAG, "Error: received empty message!");
                }
            }
            return true;
        }
    }
}
