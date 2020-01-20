package com.example.sensorrecord;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener , ValueEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelerometer, gyro;
    Button start, stop;
    boolean rekam;
    TextView accxValue, accyValue, acczValue;
    TextView gyroxValue, gyroyValue, gyrozValue;
    BufferedWriter out;
    int calibrate_limit=0;
    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference= firebaseDatabase.getReference();
    private DatabaseReference accelerometerXaxis=mRootReference.child("accelerometerXaxis");
    private DatabaseReference accelerometerYaxis=mRootReference.child("accelerometerYaxis");
    private DatabaseReference accelerometerZaxis=mRootReference.child("accelerometerZaxis");
    private DatabaseReference gyroscopeXaxis=mRootReference.child("gyroscopeXaxis");
    private DatabaseReference gyroscopeYaxis=mRootReference.child("gyroscopeYaxis");
    private DatabaseReference gyroscopeZaxis=mRootReference.child("gyroscopeZaxis");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accxValue = (TextView) findViewById(R.id.xValue);
        accyValue = (TextView) findViewById(R.id.yValue);
        acczValue = (TextView) findViewById(R.id.zValue);
        gyroxValue = (TextView) findViewById(R.id.gxValue);
        gyroyValue = (TextView) findViewById(R.id.gyValue);
        gyrozValue = (TextView) findViewById(R.id.gzValue);

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);

        Log.d(TAG, "Initialising...");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(MainActivity.this, accelerometer, sensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(MainActivity.this, gyro, sensorManager.SENSOR_DELAY_FASTEST);

        Log.d(TAG, "Registered");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rekam=true;

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rekam=false;

            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            Log.d(TAG, "X : " + event.values[0] + " Y : " + event.values[1] + " Z : " + event.values[2]);
            accxValue.setText("xValue " + event.values[0]);
            accyValue.setText("yValue " + event.values[1]);
            acczValue.setText("zValue " + event.values[2]);



        }

            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                gyroxValue.setText("xValue " + event.values[0]);
                gyroyValue.setText("yValue " + event.values[1]);
                gyrozValue.setText("zValue " + event.values[2]);
            }



        if(rekam==true) {

            sensorR senr=new sensorR();
            senr.setAx(accxValue.getText().toString());
            senr.setAy(accyValue.getText().toString());
            senr.setAz(acczValue.getText().toString());
            senr.setGx(gyroxValue.getText().toString());
            senr.setGy(gyroyValue.getText().toString());
            senr.setGz(gyrozValue.getText().toString());
            mRootReference.push().setValue(senr);

        }

        calibrate_limit++;

        if(calibrate_limit>200)
        {   sensorManager.unregisterListener(this);
            sensorManager.registerListener(MainActivity.this, accelerometer, sensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(MainActivity.this, gyro, sensorManager.SENSOR_DELAY_FASTEST);
            calibrate_limit=0;

        }


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
