package com.application.mybalancediary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import android.widget.ArrayAdapter;
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
    Spinner spinnerWorkoutFreq, spinnerGoals;

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
        ArrayAdapter<CharSequence> adapterWorkoutFreq = ArrayAdapter.createFromResource(this,
                R.array.workfreqarray, R.layout.spinner_item);
        adapterWorkoutFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterGoals = ArrayAdapter.createFromResource(this,
                R.array.goalsarray, R.layout.spinner_item);
        adapterGoals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkoutFreq.setAdapter(adapterWorkoutFreq);
        spinnerGoals.setAdapter(adapterGoals);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                position = genderGroup.indexOfChild(findViewById(checkedId));
                if (position == 0) {
                    Log.d("Gender is ", "Male");
                    genderMF = "Male";
                } else {
                    Log.d("Gender is ", "Female");
                    genderMF = "Female";
                }
            }
        });
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String height = mHeight.getText().toString();
                final String weight = mWeight.getText().toString();
                final String age = mAge.getText().toString();
                final String gender = genderMF;
                String Confirm = etConfirm.getText().toString().trim();
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
                    mAge.setError("Weight must me greater then 25 and lesser then 180");
                    return;
                }
                if (TextUtils.isEmpty(height)) {
                    mHeight.setError("Height is Required.");
                    return;
                }
                if (Integer.parseInt(height) < 100 || Integer.parseInt(height) > 230) {
                    mAge.setError("Height must me greater then 100 and lesser then 230");
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



                progressBar.setVisibility(View.VISIBLE);


                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", fullName);
                            user.put("email", email);
                            user.put("weight", weight);
                            user.put("height", height);
                            user.put("age", age);
                            user.put("gender", gender);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


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
            if (Character.isDigit(ch)) {
                return true;
            }
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

    public boolean isValid(String email)
    {
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
