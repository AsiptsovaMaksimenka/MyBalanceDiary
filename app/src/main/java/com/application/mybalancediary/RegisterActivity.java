package com.application.mybalancediary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.ArrayAdapter;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName, mEmail, mPassword, mHeight, mWeight, mAge,etConfirm;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    RadioGroup genderGroup;
    int position;
    String genderMF;
    String userID;
    Float bmi;
    Float bmr;
    Float fats;
    Float proteins;
    Float carbs;
    String workout,goals;
    Spinner spinnerWorkoutFreq, spinnerGoals;
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
        setContentView(R.layout.activity_register);
        mFullName = findViewById(R.id.fullname);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.register);
        mLoginBtn = findViewById(R.id.createText);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.loading);
        mHeight = findViewById(R.id.heightInput);
        mWeight = findViewById(R.id.weightInput);
        mAge = findViewById(R.id.ageInput);
        etConfirm = findViewById(R.id.etConfirmPassword);
        genderGroup = findViewById(R.id.genderGroup);
        spinnerWorkoutFreq = findViewById(R.id.WorkoutFreq);
        spinnerGoals = findViewById(R.id.spinnerGoals);
        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                genderMF="Male";
            } else {
                Log.d("Gender is ", "Female");
                genderMF="Female";
            }
        });
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mRegisterBtn.setOnClickListener(v -> {
            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString();
            final String fullName = mFullName.getText().toString();
            final String height = mHeight.getText().toString();
            final String weight = mWeight.getText().toString();
            final String age = mAge.getText().toString();
            String Confirm = etConfirm.getText().toString();
            if (TextUtils.isEmpty(age)) {
                mAge.setError("Age is Required.");
                return;
            }
            if (Integer.parseInt(age) < 13 || Integer.parseInt(age) > 115) {
                mAge.setError("Age must me greater then 13 and lesser then 115");
                return;
            }
            if (TextUtils.isEmpty(weight)) {
                mWeight.setError("Weight is Required.");
                return;
            }
            if (Integer.parseInt(weight) < 25 || Integer.parseInt(weight) > 180 ) {
                mWeight.setError("Weight must me greater then 25 and lesser then 180");
                return;
            }
            if (TextUtils.isEmpty(height)) {
                mHeight.setError("Height is Required.");
                return;
            }
            if (Integer.parseInt(height) < 100 || Integer.parseInt(height) > 230) {
                mHeight.setError("Height must me greater then 100 and lesser then 230");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required.");
                return;
            }
            if(!isValid(email))
            {
                mEmail.setError("Email isnt real.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required.");
                return;
            }
            if (password.length() > 8 && upperCase(password) && lowerCase(password) && numberCase(password) && specialCase(password)) {
                System.out.println("Password accepted");
            } else {
                mPassword.setError("Password Must be >= 8 characters,have upper,lower letters,number and special character");
                return;
            }
            if (TextUtils.isEmpty(Confirm)) {
                etConfirm.setError("Confirm Password is Required.");
                return;
            }
            if(mPassword.getText().toString().equals(etConfirm.getText().toString())) {
                etConfirm.setError("Password & Confirm Password must be same");
                return;
            }
            if(!weight.equals("") || !height.equals("")) {
                Log.d("In try Block","");
                bmi =  ((Float.parseFloat(weight) * 10000) / ((Float.parseFloat(height) * (Float.parseFloat(height)))));
                bmi = Float.valueOf(String.format(Locale.getDefault(), "%.2f", bmi));
                Log.d("BMI is", bmi.toString());
            }
            if (genderMF.equals("Male"))
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
            else  if (genderMF.equals("Female")) {
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
            progressBar.setVisibility(View.VISIBLE);
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser fuser = fAuth.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d(TAG, "onFailure: Email not sent " + e.getMessage()));
                    Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    getUsersRef("name").setValue(fullName);
                    getUsersRef("email").setValue(email);
                    getUsersRef("height").setValue(height);
                    getUsersRef("weight").setValue(weight);
                    getUsersRef("age").setValue(age);
                    getUsersRef("gender").setValue(genderMF);
                    getUsersRef("bmi").setValue(Math.round(bmi * 100.0) / 100.0);
                    getUsersRef("bmr").setValue(Math.round(bmr * 100.0) / 100.0);
                    getUsersRef("workout").setValue(workout);
                    getUsersRef("goals").setValue(goals);
                    getCaloriesRef("proteins").setValue(Math.round(proteins * 100.0) / 100.0);
                    getCaloriesRef("fats").setValue(Math.round(fats * 100.0) / 100.0);
                    getCaloriesRef("carbs").setValue(Math.round(carbs * 100.0) / 100.0);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
        mLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

    private boolean upperCase(String str) {
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean lowerCase(String str) {
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isLowerCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean numberCase(String str) {
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch))
                return true;
        }
        return false;
    }

    private boolean specialCase(String str) {
        char ch;
        char ph;
        String specialChars = "/0*!@$^&*()\"{}_[]|\\?<>,.";
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            for (int j = 0; j < specialChars.length(); j++) {
                ph = specialChars.charAt(j);
                if (ch == ph) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}