package com.application.mybalancediary;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class StepsMain extends AppCompatActivity implements SensorEventListener {
    private Steps steps;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView showSteps;
    private TextView displayGoal;
    private TextView percentageView;
    //  private CircularProgressBar circularProgressBar;
    private int currentGoal = 0;
    int mySteps = 0;
    public static final int MAIN_REQUEST = 2;
    private final ActivityResultLauncher<String> requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            okay -> {
                if (okay) {
                    setupSensor();
                }
                else {
                    Toast.makeText(this, "No sensor found", Toast.LENGTH_SHORT).show();
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        percentageView = findViewById(R.id.percentage);
        //  circularProgressBar = findViewById(R.id.yourCircularProgressbar);
        displayGoal = findViewById(R.id.show_goal);
        showSteps = findViewById(R.id.show_steps);
        if(savedInstanceState == null) {
            steps = new Steps();
        }
        else{

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED){
            setupSensor();
        } else {
            requestPermissionsLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String goalFromSettings = data.getStringExtra("goal");
                displayGoal.setText(goalFromSettings);
                currentGoal = Integer.parseInt(goalFromSettings);
                // circularProgressBar.setProgressMax(currentGoal);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("value", steps.toString());
        outState.putInt("mySteps", mySteps);
        outState.putInt("currentGoal", currentGoal);
    }

    private void setupSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor == null) {
            Toast.makeText(this, "No sensor found", Toast.LENGTH_SHORT).show();
        }
    }
    //@Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(StepsMain.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onDestroy(){
        super.onDestroy();
        ;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mySteps = steps.countSteps((int) sensorEvent.values[0]);
        showSteps.setText(Integer.toString(mySteps));
        int percentage = steps.returnPercentage(currentGoal, mySteps);
        percentageView.setText(percentage + "%");

        // circularProgressBar.setProgressWithAnimation(mySteps, 1000L);
        if (steps.checkGoal(mySteps, currentGoal)){
            Toast.makeText(this, "Congratulations you hit your goal!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void settingsClicked(View view) {
        final Intent intent = new Intent(this, SettingsSteps.class);
        startActivityForResult(intent, MAIN_REQUEST);
    }
}