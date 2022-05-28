package com.application.mybalancediary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    int position;
    String genderMF,userID,workout,goals;
    Float bmi,bmr,fats,proteins,carbs;
    ImageView ShowHidePWD,ShowHidePWDConfirm;
    private DatePickerDialog datePickerDialog;

    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    private DatabaseReference getCaloriesRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Calories")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      final EditText mFullName = findViewById(R.id.fullName);
      final EditText mEmail = findViewById(R.id.Email);
      final EditText mPassword = findViewById(R.id.password);
      final Button mRegisterBtn = findViewById(R.id.register);
      final TextView mLoginBtn = findViewById(R.id.createText);
      final  ProgressBar progressBar = findViewById(R.id.loading);
      final EditText mHeight = findViewById(R.id.heightInput);
      final EditText mWeight = findViewById(R.id.weightInput);
      final EditText mAge = findViewById(R.id.ageInput);
      final  EditText  etConfirm = findViewById(R.id.etConfirmPassword);
      final RadioGroup  genderGroup = findViewById(R.id.genderGroup);
      final Spinner spinnerWorkoutFreq = findViewById(R.id.WorkoutFreq);
      final Spinner spinnerGoals = findViewById(R.id.spinnerGoals);
        ShowHidePWD=findViewById(R.id.show_hide_pwd);
        ShowHidePWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        ShowHidePWDConfirm=findViewById(R.id.show_hide_pwd_confirm);
        ShowHidePWDConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etConfirm .getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                    etConfirm .setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    etConfirm .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
                genderMF="Male";
            } else {
                Log.d("Gender is ", "Female");
                genderMF="Female";
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mRegisterBtn.setOnClickListener(v -> {
            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString();
            final String fullName = mFullName.getText().toString();
            final String height = mHeight.getText().toString();
            final String weight = mWeight.getText().toString();
            String[] ageRes=String.valueOf(mAge.getText()).split("-");
            final String age=String.valueOf(getAge(Integer.valueOf(ageRes[2]),Integer.valueOf(ageRes[1]),Integer.valueOf(ageRes[0])));
            String Confirm = etConfirm.getText().toString();
            if (TextUtils.isEmpty(age)) {
                mAge.setError("Age is Required.");
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
            if(!mPassword.getText().toString().equals(etConfirm.getText().toString())) {
                etConfirm.setError("Password & Confirm Password must be same");
                return;
            }
            if(!weight.equals("") || !height.equals("")) {
                bmi =  ((Float.parseFloat(weight) * 10000) / ((Float.parseFloat(height) * (Float.parseFloat(height)))));
                bmi = Float.valueOf(String.format(Locale.getDefault(), "%.2f", bmi));
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
            int target = Integer.parseInt(weight)*35;
            int glass =target/250 ;
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d(TAG, "onFailure: Email not sent " + e.getMessage()));
                    Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    getUsersRef("name").setValue(fullName);
                    getUsersRef("email").setValue(email);
                    getUsersRef("height").setValue(height);
                    getUsersRef("weight").setValue(weight);
                    getUsersRef("age").setValue(age);
                    getUsersRef("gender").setValue(genderMF);
                    getUsersRef("bmi").setValue(Math.round(bmi * 10.0) / 10.0);
                    getUsersRef("bmr").setValue(Math.round(bmr * 10.0) / 10.0);
                    getUsersRef("workout").setValue(workout);
                    getUsersRef("goals").setValue(goals);
                    getUsersRef("GlassWater").setValue(glass);
                    getUsersRef("TargetWater").setValue(target);
                    getUsersRef("proteins").setValue(Math.round(proteins * 10.0) / 10.0);
                    getUsersRef("fats").setValue(Math.round(fats * 10.0) / 10.0);
                    getUsersRef("carbs").setValue(Math.round(carbs * 10.0) / 10.0);
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