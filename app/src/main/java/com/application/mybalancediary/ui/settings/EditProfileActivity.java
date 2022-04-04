package com.application.mybalancediary.ui.settings;

import androidx.annotation.NonNull;
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
import android.widget.Spinner;

import com.application.mybalancediary.MainActivity;
import com.application.mybalancediary.R;
import com.application.mybalancediary.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class EditProfileActivity extends AppCompatActivity {
    EditText mFullName, mHeight, mWeight;
    Button mSaveBtn;
    Spinner spinnerNewWorkoutFreq, spinnerNewGoal;
    Float bmi;
    Float NewBmr;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String NewWorkout,NewGoals;
    String age,gender;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private DatabaseReference getUsersRef(String ref) {
        FirebaseUser user = fAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        return mDatabase.child("Users").child(userId).child(ref);
    }

    private DatabaseReference getCaloriesRef(String ref) {
        FirebaseUser user = fAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        return mDatabase.child("Calories").child(userId).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mFullName = findViewById(R.id.user_name);
        mHeight = findViewById(R.id.edit_height);
        mWeight = findViewById(R.id.edit_weight);
        mSaveBtn = findViewById(R.id.save);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        spinnerNewWorkoutFreq = findViewById(R.id.NewWorkoutFreq);
        spinnerNewGoal = findViewById(R.id.spinnerNewGoals);
        fAuth = FirebaseAuth.getInstance();
        ArrayAdapter<CharSequence> adapterNewWorkoutFreq = ArrayAdapter.createFromResource(this,
                R.array.workfreqarray, R.layout.spinner_item);
        adapterNewWorkoutFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterNewGoals = ArrayAdapter.createFromResource(this,
                R.array.goalsarray, R.layout.spinner_item);
        adapterNewGoals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNewWorkoutFreq.setAdapter(adapterNewWorkoutFreq);
        spinnerNewGoal.setAdapter(adapterNewGoals);
        spinnerNewWorkoutFreq .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                NewWorkout = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerNewGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                NewGoals = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String NewName = mFullName.getText().toString().trim();
                final String NewHeight = mHeight.getText().toString().trim();
                final String NewWeight = mWeight.getText().toString().trim();
                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();
                FirebaseDatabase  database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("users");
                storageReference = FirebaseStorage.getInstance().getReference();

                Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            age = "" + ds.child("age").getValue();
                            gender=""+ds.child("gender").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (TextUtils.isEmpty(NewWeight)) {
                    mWeight.setError("Weight is Required.");
                    return;
                }
                if (Integer.parseInt(NewWeight) < 25 || Integer.parseInt(NewWeight) > 180) {
                    mWeight.setError("Weight must me greater then 25 and lesser then 180");
                    return;
                }
                if (TextUtils.isEmpty(NewHeight)) {
                    mHeight.setError("Height is Required.");
                    return;
                }
                if (Integer.parseInt(NewHeight) < 100 || Integer.parseInt(NewHeight) > 230) {
                    mHeight.setError("Height must me greater then 100 and lesser then 230");
                    return;
                }
                if (!NewWeight.equals("") || !NewHeight.equals("")) {
                    Log.d("In try Block", "");
                    bmi = ((Float.parseFloat(NewWeight) * 10000) / ((Float.parseFloat(NewHeight) * (Float.parseFloat(NewHeight)))));
                    bmi = Float.parseFloat(String.format("%.2f", bmi));
                    Log.d("BMI is", bmi.toString());
                }
                if (gender.equals("Male")) {
                    NewBmr = (float) 5 + ((float) 10 * (Float.parseFloat(NewWeight))) + ((float) 6.25 * (Float.parseFloat(NewHeight))) - ((float) 5 * (Float.parseFloat(age)));
                    if (NewWorkout.equals("None (little or no exercise)")) {
                        NewBmr = NewBmr * (float) 1.2;
                    }
                    if (NewWorkout.equals("Low (1-3 times/week)")) {
                        NewBmr = NewBmr * (float) 1.375;
                    }
                    if (NewWorkout.equals("Medium (3-5 days/week)")) {
                        NewBmr = NewBmr * (float) 1.55;
                    }
                    if (NewWorkout.equals("High (6-7 days/week)")) {
                        NewBmr = NewBmr * (float) 1.725;
                    }
                    if (NewWorkout.equals("Very High (physical job or 7+ times/week)")) {
                        NewBmr = NewBmr * (float) 1.9;
                    }
                } else if (gender.equals("Female")) {
                    NewBmr = ((float) 10 * (Float.parseFloat(NewWeight))) + ((float) 6.25 * (Float.parseFloat(NewHeight))) - ((float) 5 * (Float.parseFloat(age))) - 161;
                    if (NewWorkout.equals("None (little or no exercise)")) {
                        NewBmr = NewBmr * (float) 1.2;
                    }
                    if (NewWorkout.equals("Low (1-3 times/week)")) {
                        NewBmr = NewBmr * (float) 1.375;
                    }
                    if (NewWorkout.equals("Medium (3-5 days/week)")) {
                        NewBmr = NewBmr * (float) 1.55;
                    }
                    if (NewWorkout.equals("High (6-7 days/week)")) {
                        NewBmr = NewBmr * (float) 1.725;
                    }
                    if (NewWorkout.equals("Very High (physical job or 7+ times/week)")) {
                        NewBmr = NewBmr * (float) 1.9;
                    }
                }
                if (NewGoals.equals("Fat Loss")) {
                    NewBmr = NewBmr - (float) 500;
                }
                if (NewGoals.equals("Gain Weight")) {
                    NewBmr = NewBmr +  (float)500;
                }
                userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                getUsersRef("name").setValue(NewName);
                getUsersRef("height:").setValue(NewHeight);
                getUsersRef("weight").setValue(NewWeight);
                getUsersRef("bmi").setValue(Math.round(bmi * 100.0) / 100.0);
                getUsersRef("bmr").setValue(Math.round(NewBmr * 100.0) / 100.0);
                getUsersRef("workout").setValue(NewWorkout);
                getUsersRef("goals").setValue(NewGoals);
                startActivity(new Intent(getApplicationContext(), ProfileFragment.class));

            }
        });
    }
}