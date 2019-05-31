package com.cmps121.asgn3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MyService extends Service {

    private static final String LOG_TAG = "MyService";

    // Motion detector thread and runnable.
    private Thread myThread;
    private MyServiceTask myTask;

    // wakelock for CPU control
    private PowerManager.WakeLock wakeLock;

    // Binder class.
    public class MyBinder extends Binder {
        MyService getService() {
            // Returns the underlying service.
            return MyService.this;
        }
    }

    // Binder given to clients
    private final IBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "Service is being bound");
        // Returns the binder to this service.
        return myBinder;
    }

    public MyService() {
        // empty constructor
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Service is being created");

        // Creates the thread running the service.
        myTask = new MyServiceTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);

        // acquire CPU
        PowerManager powerManager =
                (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock=
                powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"DidItMove::WakeLockService"
                );
        wakeLock.acquire();

        // We start the task thread.
        if (!myThread.isAlive()) {
            myThread.start();
        }
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "Stopping.");
        // Stops the motion detector.
        myTask.stopProcessing();

        // release CPU
        wakeLock.release();
        Log.i(LOG_TAG, "Stopped.");
    }

    public void updateResultCallback(MyServiceTask.ResultCallback resultCallback) {
        myTask.updateResultCallback(resultCallback);
    }

    //calls diditMove
    public boolean didItMove() {
        return myTask.didItMove();
    }

    // reset timer and accel_time
    public void clear() {
        myTask.clear();
    }

    // stopProcessing
    public void terminate() {
        myTask.stopProcessing();
    }
}
