package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class reg1 extends AppCompatActivity {
    private static final String TAG ="TAG";
    ImageView ShowHidePWD,ShowHidePWDConfirm;

    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg1);
        final EditText mFullName = findViewById(R.id.fullName);
        final EditText mEmail = findViewById(R.id.Email);
        final EditText mPassword = findViewById(R.id.password);
        final EditText etConfirm = findViewById(R.id.etConfirmPassword);
        final Button mRegisterBtn = findViewById(R.id.register);
        final TextView mLoginBtn = findViewById(R.id.createText);

        ShowHidePWD = findViewById(R.id.show_hide_pwd);
        ShowHidePWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        ShowHidePWDConfirm = findViewById(R.id.show_hide_pwd_confirm);
        ShowHidePWDConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etConfirm.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                    etConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    etConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
            String Confirm = etConfirm.getText().toString();
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required.");
                return;
            }
            if (!isValid(email)) {
                mEmail.setError("Email isnt real.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required.");
                return;
            }
            if (password.length() >= 8 && upperCase(password) && lowerCase(password) && numberCase(password) && specialCase(password)) {
                System.out.println("Password accepted");
            } else {
                mPassword.setError("Password Must be > 8 characters,have upper,lower letters,number and special character");
                return;
            }
            if (TextUtils.isEmpty(Confirm)) {
                etConfirm.setError("Confirm Password is Required.");
                return;
            }
            if (!mPassword.getText().toString().equals(etConfirm.getText().toString())) {
                etConfirm.setError("Password & Confirm Password must be same");
                return;
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(reg1.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d(TAG, "onFailure: Email not sent " + e.getMessage()));
                    getUsersRef("name").setValue(fullName);
                    getUsersRef("email").setValue(email);
                    startActivity(new Intent(getApplicationContext(), reg2.class));
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
        String emailRegex =  "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}