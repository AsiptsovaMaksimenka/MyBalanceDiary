package com.application.mybalancediary.ui.steps;


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.mybalancediary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepsMain extends Fragment implements SensorEventListener {

    SensorManager sensorManager;
    TextView tv_steps;
    boolean running=false;
    Date date = new Date();
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
    private DatabaseReference getStepsRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Steps").child(today)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_steps, container, false);
        tv_steps=root.findViewById(R.id.tv_stepsToken);

        sensorManager= (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        return root;
    }
    @Override
    public  void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor!=null)
        {
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            Toast.makeText(getActivity(),"Sensor not found",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public  void onPause() {

        super.onPause();
        running=false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       if(running)
       {
        tv_steps.setText(String.valueOf(event.values[0]));
        getStepsRef("steps").setValue(event.values[0]);

       }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public StepsMain() {
    }
}