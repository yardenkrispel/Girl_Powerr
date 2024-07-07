package com.example.Girl_Power.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.Girl_Power.Interfaces.MoveCallBack;

public class SensorsUtil {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long timestamp = 0l;
    MoveCallBack moveCallBack;

    public SensorsUtil(Context context, MoveCallBack moveCallBack) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallBack = moveCallBack;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                makeMove(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    private void makeMove(float x, float y){
        if (System.currentTimeMillis() - timestamp > 500) {
            timestamp = System.currentTimeMillis();

            if (moveCallBack != null){
                if (x > 2.0f) {
                    moveCallBack.tiltLeft();
                }

                else if (x < -2.0f) {
                    moveCallBack.tiltRight();
                }

                if (y < 4.5f ) {
                    moveCallBack.tiltUp();
                }

                else  {
                    moveCallBack.tiltDown();
                }
            }
        }
    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }
    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
