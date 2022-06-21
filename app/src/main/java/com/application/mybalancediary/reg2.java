package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class reg2 extends AppCompatActivity {
    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2);
        final EditText mHeight = findViewById(R.id.heightInput);
        final EditText mWeight = findViewById(R.id.weightInput);
        final Button mRegisterBtn = findViewById(R.id.register);
        mRegisterBtn.setOnClickListener(v -> {
            final String height = mHeight.getText().toString();
            final String weight = mWeight.getText().toString();
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
            getUsersRef("height").setValue(height);
            getUsersRef("weight").setValue(weight);
            startActivity(new Intent(getApplicationContext(), reg3.class));
        });
    }
}