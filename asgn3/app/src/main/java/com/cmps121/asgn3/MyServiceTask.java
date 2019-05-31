package com.cmps121.asgn3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

public class MyServiceTask implements Runnable, SensorEventListener {

    public static final String LOG_TAG = "MyService";
    private boolean running;
    private Context context;

    ResultCallback resultCallback;

    private SensorManager mSensorManager;
    private Sensor accelerometer;

    private float lastX, lastY, lastZ;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    int first_accel_time;
    int T; // time counter

    final Object myLock = new Object();

    public MyServiceTask(Context context) {
        this.context = context;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
    }

    @Override
    public void run() {
        running = true;

        clear(); // time counter

        while (running) {
            // Sleep a tiny bit.
            try {
                Thread.sleep(1000);
                T++;
                Log.i(LOG_TAG, "T = " + T);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            // Check movement
            boolean moved = didItMove();

            // Sends it to the UI thread in MainActivity (if MainActivity
            // is running).
            Log.i(LOG_TAG, "Checking movement");
            notifyResultCallback(moved);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if moved (compensate for gravity)
        if (deltaX > 2 || deltaY > 10 || deltaZ > 2) {
            first_accel_time = T;
            Log.i(LOG_TAG, "Device Moved @ time " + T + " X: " + deltaX + " Y: " + (deltaY - 9.81) + " Z: " + deltaZ);
        }
    }

    public boolean didItMove() {
        boolean moved = false;

        synchronized(myLock) {
            if(first_accel_time != 0 && T - first_accel_time > 30) {
                moved = true;
            }
        }
        return moved;
    }

    public void clear() {
        synchronized(myLock) {
            T = 0;
            first_accel_time = 0;
        }
    }

    public void stopProcessing() {
        // No need to bother with a synchronized statement; booleans are atomically updated.
        running = false;
    }

    /**
     * Call this function to return the boolean moved to the activity.
     *
     * @param moved
     */
    private void notifyResultCallback(boolean moved) {
        ServiceResult result = new ServiceResult();
        // If we got a null result, we have no more space in the buffer,
        // and we simply drop the integer, rather than sending it back.
        if (result != null) {
            result.moved = moved;
            Log.i(LOG_TAG, "calling resultCallback for " + result.moved);
            resultCallback.onResultReady(result);
        }
    }

    public void updateResultCallback(ResultCallback result) {
        Log.i(LOG_TAG, "Adding result callback");
        resultCallback = result;
    }

    public interface ResultCallback {
        void onResultReady(ServiceResult result);
    }

}

