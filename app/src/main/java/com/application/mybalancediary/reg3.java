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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class reg3 extends AppCompatActivity {
    Date now = new Date();
    private Calendar cal = Calendar.getInstance();
    private DatabaseReference getUsersRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        final EditText mDay = findViewById(R.id.ageDay);
        final EditText mMonth = findViewById(R.id.ageMonth);
        final EditText mYear = findViewById(R.id.ageYear);
        final Button mRegisterBtn = findViewById(R.id.register);

        mRegisterBtn.setOnClickListener(v -> {
            final String day = mDay.getText().toString();
            final String month = mMonth.getText().toString();
            final String year = mYear.getText().toString();

            if(TextUtils.isEmpty(day)) {
                mDay.setError("Empty");
                return;
            }
            if(TextUtils.isEmpty(month)) {
                mMonth.setError("Empty");
                return;
            }
            if(TextUtils.isEmpty(year)) {
                mYear.setError("Empty");
                return;
            }
            if (year.length() != 4) {
                mYear.setError("Format");
                return;
            }
            if (validateDate(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year))) {
                getUsersRef("age").setValue(getAge(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)));
                startActivity(new Intent(getApplicationContext(), reg4.class));
            }
        });
    }

    public int getAge(int Year, int Month, int Day) {
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

    private HashMap<Integer, Integer> MaxMonthMap() {
        HashMap<Integer, Integer> maxMonthDay=new HashMap<>();
        maxMonthDay.put(1, 31);
        maxMonthDay.put(2, 28);
        maxMonthDay.put(3, 31);
        maxMonthDay.put(4, 30);
        maxMonthDay.put(5, 31);
        maxMonthDay.put(6, 30);
        maxMonthDay.put(7, 31);
        maxMonthDay.put(8, 30);
        maxMonthDay.put(9, 31);
        maxMonthDay.put(10, 30);
        maxMonthDay.put(11, 31);
        maxMonthDay.put(12, 31);
        return maxMonthDay;
    }

    private boolean validateDate(int day, int month, int year) {
        if(month<=0 || month>=13)
            return false;
        if(year<1922 || year>now.getYear()+1900)
            return false;
        if(year%100 == 0 && year%400==0 && month==2)
            return day>=1  && day<=MaxMonthMap().get(month)+1;
        return day>=1  && day <= MaxMonthMap().get(month);
    }

}