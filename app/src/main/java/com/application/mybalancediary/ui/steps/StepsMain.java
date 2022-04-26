package com.application.mybalancediary.ui.steps;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.application.mybalancediary.R;


public class StepsMain extends Fragment implements SensorEventListener {
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
                    Toast.makeText(getContext(), "No sensor found", Toast.LENGTH_SHORT).show();
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.Q)

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_steps, container, false);
        percentageView = root.findViewById(R.id.percentage);
        //  circularProgressBar = findViewById(R.id.yourCircularProgressbar);
        displayGoal = root.findViewById(R.id.show_goal);
        showSteps = root.findViewById(R.id.show_steps);
        final Button btn_click =root.findViewById(R.id.btn_click);
        btn_click.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsSteps.class)));
        if(savedInstanceState == null) {
            steps = new Steps();
        }
        else{

        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED){
            setupSensor();
        } else {
            requestPermissionsLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        }
        return root;
    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                String goalFromSettings = data.getStringExtra("goal");
                displayGoal.setText(goalFromSettings);
                currentGoal = Integer.parseInt(goalFromSettings);
                // circularProgressBar.setProgressMax(currentGoal);
            }
        }
    }

    @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("value", steps.toString());
        outState.putInt("mySteps", mySteps);
        outState.putInt("currentGoal", currentGoal);
    }

    private void setupSensor() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor == null) {
            Toast.makeText(getContext(), "No sensor found", Toast.LENGTH_SHORT).show();
        }
    }
    //@Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(StepsMain.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
   public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onDestroy(){
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
            Toast.makeText(getContext(), "Congratulations you hit your goal!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void settingsClicked(View view) {
        final Intent intent = new Intent(getContext(), SettingsSteps.class);
        startActivityForResult(intent, MAIN_REQUEST);
    }
}