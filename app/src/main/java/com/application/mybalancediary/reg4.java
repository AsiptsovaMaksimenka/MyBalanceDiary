package com.application.mybalancediary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class reg4 extends AppCompatActivity {

    int position;
    String genderMF,workout,goals;
    Float bmi,bmr,fats,proteins,carbs;
    String weight,height,age;

    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg4);
        final RadioGroup genderGroup = findViewById(R.id.genderGroup);
        final Spinner spinnerWorkoutFreq = findViewById(R.id.WorkoutFreq);
        final Spinner spinnerGoals = findViewById(R.id.spinnerGoals);
        final Button mRegisterBtn = findViewById(R.id.register);
        final CircularProgressIndicator indicator =findViewById(R.id.progress_barCircle);

        FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            weight = String.valueOf(ds.child("weight").getValue());
                            height = String.valueOf(ds.child("height").getValue());
                            age = String.valueOf(ds.child("age").getValue());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        ArrayAdapter<CharSequence> adapterWorkoutFreq = ArrayAdapter.createFromResource(this,
                R.array.workfreqarray, R.layout.spinner_item);
        adapterWorkoutFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterGoals = ArrayAdapter.createFromResource(this,
                R.array.goalsarray, R.layout.spinner_item);
        adapterGoals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkoutFreq.setAdapter(adapterWorkoutFreq);
        spinnerGoals.setAdapter(adapterGoals);
        spinnerWorkoutFreq .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                genderMF="Vertolet";
            } else {
                Log.d("Gender is ", "Female");
                genderMF="Multivarka";
            }
        });
        mRegisterBtn.setOnClickListener(v -> {
            indicator.show();
            indicator.setVisibility(View.VISIBLE);
            if(!weight.equals("") || !height.equals("")) {
                bmi =  ((Float.parseFloat(weight) * 10000) / ((Float.parseFloat(height) * (Float.parseFloat(height)))));
                bmi = Float.valueOf(String.format(Locale.getDefault(), "%.2f", bmi));
            }
            if (genderMF.equals("Vertolet"))
            {
                bmr=(float) 5 + ((float)10 *(Float.parseFloat(weight))) + ((float)6.25  *(Float.parseFloat(height))) - ((float)5 * (Float.parseFloat(age)));
                if (workout.equals("None (little or no exercise)"))
                    bmr=bmr*(float)1.2;
                if (workout.equals("Low (1-3 times/week)"))
                    bmr=bmr*(float)1.375;
                if (workout.equals("Medium (3-5 days/week)"))
                    bmr=bmr*(float)1.55;
                if (workout.equals("High (6-7 days/week)"))
                    bmr=bmr*(float)1.725;
                if (workout.equals("Very High (physical job or 7+ times/week)"))
                    bmr=bmr*(float)1.9;
            }
            else  if (genderMF.equals("Multivarka")) {
                bmr=((float)10*(Float.parseFloat(weight))) + ((float)6.25 *(Float.parseFloat(height)))- ((float)5* (Float.parseFloat(age)))- 161;
                if (workout.equals("None (little or no exercise)"))
                    bmr=bmr*(float)1.2;
                if (workout.equals("Low (1-3 times/week)"))
                    bmr=bmr*(float)1.375;
                if (workout.equals("Medium (3-5 days/week)"))
                    bmr=bmr*(float)1.55;
                if (workout.equals("High (6-7 days/week)"))
                    bmr=bmr*(float)1.725;
                if (workout.equals("Very High (physical job or 7+ times/week)"))
                    bmr=bmr*(float)1.9;
            }
            if (goals.equals("Fat Loss"))
                bmr=bmr-500;
            if (goals.equals("Gain Weight"))
                bmr=bmr+500;
            proteins=((float)0.3*bmr)/(float)4;
            fats= ((float)0.3*bmr)/(float)9;
            carbs=((float)0.4*bmr)/(float)4;
            int target = Integer.parseInt(weight)*35;
            getUsersRef("gender").setValue(genderMF);
            getUsersRef("bmi").setValue(Math.round(bmi * 10.0) / 10.0);
            getUsersRef("bmr").setValue(Math.round(bmr * 10.0) / 10.0);
            getUsersRef("workout").setValue(workout);
            getUsersRef("goals").setValue(goals);
            getUsersRef("TargetWater").setValue(target);
            getUsersRef("proteins").setValue(Math.round(proteins * 10.0) / 10.0);
            getUsersRef("fats").setValue(Math.round(fats * 10.0) / 10.0);
            getUsersRef("carbs").setValue(Math.round(carbs * 10.0) / 10.0);
            Toast.makeText(reg4.this, "User Created.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
        }
    }
