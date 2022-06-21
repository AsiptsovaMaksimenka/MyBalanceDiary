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

import java.util.Date;

public class reg3 extends AppCompatActivity {

    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        final EditText mAge = findViewById(R.id.ageInput);
        final Button mRegisterBtn = findViewById(R.id.register);
        mRegisterBtn.setOnClickListener(v -> {
        String[] ageRes=String.valueOf(mAge.getText()).split("\\W");
        final String age=String.valueOf(getAge(Integer.valueOf(ageRes[2]),Integer.valueOf(ageRes[1]),Integer.valueOf(ageRes[0])));
        if (TextUtils.isEmpty(age))
            {
                mAge.setError("Age is Required.");
                return;
            }
            getUsersRef("age").setValue(age);
            startActivity(new Intent(getApplicationContext(), reg4.class));
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