package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.mybalancediary.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    int position;
    String genderMF,workout,goals;
    Float bmi,bmr,fats,proteins,carbs;

    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile2);
        final EditText mFullName = findViewById(R.id.fullName);
        final EditText mHeight = findViewById(R.id.heightInput);
        final EditText mWeight = findViewById(R.id.weightInput);
        final EditText mAge = findViewById(R.id.ageInput);
        final RadioGroup genderGroup = findViewById(R.id.genderGroup);
        final Spinner spinnerWorkoutFreq = findViewById(R.id.WorkoutFreq);
        final Spinner spinnerGoals = findViewById(R.id.spinnerGoals);
        final Button save = findViewById(R.id.register);

        ArrayAdapter<CharSequence> adapterWorkoutFreq = ArrayAdapter.createFromResource(this,
                R.array.workfreqarray, R.layout.spinner_item);
        adapterWorkoutFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterGoals = ArrayAdapter.createFromResource(this,
                R.array.goalsarray, R.layout.spinner_item);
        adapterGoals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkoutFreq.setAdapter(adapterWorkoutFreq);
        spinnerGoals.setAdapter(adapterGoals);
        spinnerWorkoutFreq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                workout = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerGoals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                goals = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            position = genderGroup.indexOfChild(findViewById(checkedId));
            if (position == 1) {
                Log.d("Gender is ", "Male");
                genderMF = "Male";
            } else {
                Log.d("Gender is ", "Female");
                genderMF = "Female";
            }
        });
        save.setOnClickListener(v -> {
            final String fullName = mFullName.getText().toString();
            final String height = mHeight.getText().toString();
            final String weight = mWeight.getText().toString();
            String[] ageRes = String.valueOf(mAge.getText()).split("\\W");
            final String age = String.valueOf(getAge(Integer.valueOf(ageRes[2]), Integer.valueOf(ageRes[1]), Integer.valueOf(ageRes[0])));
            if (TextUtils.isEmpty(age)) {
                mAge.setError("Age is Required.");
                return;
            }
            if (TextUtils.isEmpty(weight)) {
                mWeight.setError("Weight is Required.");
                return;
            }
            if (TextUtils.isEmpty(height)) {
                mHeight.setError("Height is Required.");
                return;
            }

            if (Integer.parseInt(weight) < 25 || Integer.parseInt(weight) > 180) {
                mWeight.setError("Weight must me greater then 25 and lesser then 180");
                return;
            }
            if (Integer.parseInt(height) < 100 || Integer.parseInt(height) > 230) {
                mHeight.setError("Height must me greater then 100 and lesser then 230");
                return;
            }
            if (!weight.equals("") || !height.equals("")) {
                bmi = ((Float.parseFloat(weight) * 10000) / ((Float.parseFloat(height) * (Float.parseFloat(height)))));
                bmi = Float.valueOf(String.format(Locale.getDefault(), "%.2f", bmi));
            }
            if (genderMF.equals("Male")) {
                bmr = (float) 5 + ((float) 10 * (Float.parseFloat(weight))) + ((float) 6.25 * (Float.parseFloat(height))) - ((float) 5 * (Float.parseFloat(age)));
                if (workout.equals("None (little or no exercise)"))
                    bmr = bmr * (float) 1.2;
                if (workout.equals("Low (1-3 times/week)"))
                    bmr = bmr * (float) 1.375;
                if (workout.equals("Medium (3-5 days/week)"))
                    bmr = bmr * (float) 1.55;
                if (workout.equals("High (6-7 days/week)"))
                    bmr = bmr * (float) 1.725;
                if (workout.equals("Very High (physical job or 7+ times/week)"))
                    bmr = bmr * (float) 1.9;
            } else if (genderMF.equals("Female")) {
                bmr = ((float) 10 * (Float.parseFloat(weight))) + ((float) 6.25 * (Float.parseFloat(height))) - ((float) 5 * (Float.parseFloat(age))) - 161;
                if (workout.equals("None (little or no exercise)"))
                    bmr = bmr * (float) 1.2;
                if (workout.equals("Low (1-3 times/week)"))
                    bmr = bmr * (float) 1.375;
                if (workout.equals("Medium (3-5 days/week)"))
                    bmr = bmr * (float) 1.55;
                if (workout.equals("High (6-7 days/week)"))
                    bmr = bmr * (float) 1.725;
                if (workout.equals("Very High (physical job or 7+ times/week)"))
                    bmr = bmr * (float) 1.9;
            }
            if (goals.equals("Fat Loss"))
                bmr = bmr - 500;
            if (goals.equals("Gain Weight"))
                bmr = bmr + 500;
            proteins = ((float) 0.3 * bmr) / (float) 4;
            fats = ((float) 0.3 * bmr) / (float) 9;
            carbs = ((float) 0.4 * bmr) / (float) 4;
            getUsersRef("name").setValue(fullName);
            getUsersRef("height").setValue(height);
            getUsersRef("weight").setValue(weight);
            getUsersRef("age").setValue(age);
            getUsersRef("gender").setValue(genderMF);
            getUsersRef("workout").setValue(workout);
            getUsersRef("goals").setValue(goals);
            getUsersRef("bmi").setValue(Math.round(bmi * 10.0) / 10.0);
            getUsersRef("bmr").setValue(Math.round(bmr * 10.0) / 10.0);
            getUsersRef("proteins").setValue(Math.round(proteins * 10.0) / 10.0);
            getUsersRef("fats").setValue(Math.round(fats * 10.0) / 10.0);
            getUsersRef("carbs").setValue(Math.round(carbs * 10.0) / 10.0);
            Intent intent = new Intent();
            intent.putExtra("goal" , weight);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
    public int getAge(int Year, int Month, int Day) {
        Date now = new Date();
        int nowMonth = now.getMonth()+1;
        int nowYear = now.getYear()+1900;
        int result = nowYear - Year;
        if (Month > nowMonth)
            result--;
        else if (Month == nowMonth)
            if (Day > now.getDate())
                result--;
        return result;
    }
}