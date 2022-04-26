package com.application.mybalancediary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsSteps extends AppCompatActivity {
    public static final int SETTINGS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_steps);
    }

    public void saveClicked(View view) {

        EditText goalInput = findViewById(R.id.steps_goal);
        String goal = goalInput.getText().toString();

        if (validateUserInput(goal)) {
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("goal" , goal);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Please check your inputs", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validateUserInput(String... strings) {
        boolean isCorrect = false;
        for (String userInput : strings) {
            isCorrect = !userInput.isEmpty();
        }
        return isCorrect;
    }

    @Override
    public void onBackPressed() {
        saveClicked(null);
    }

    public void homeClicked(View view) {
        saveClicked(null);
    }


}