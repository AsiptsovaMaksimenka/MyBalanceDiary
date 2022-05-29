package com.application.mybalancediary.ui.weight;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.application.mybalancediary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeightFragmentSetting extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView textGoalWeightBox;
    private TextView textGoalDateBox;
    private Button btnUpdateGoal;
    private Button btnDeleteAll;
    private DatabaseReference getWeightRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Weight")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings_weight);

        btnUpdateGoal=findViewById(R.id.buttonUpdateGoal);
        btnUpdateGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goalDateString=textGoalDateBox.getText().toString();
                String goalWeightString=textGoalWeightBox.getText().toString();

                if(goalDateString.length()==0 || goalWeightString.length()==0)
                {
                    openSubmitAlertDialog();
                }
                else
                {
                    float weight=Float.parseFloat(textGoalWeightBox.getText().toString());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                    getWeightRef("Goal_Weight").setValue(String.valueOf(weight));
                    Date dateObj = new Date(textGoalDateBox.getText().toString());
                    String formattedDate = simple.format(dateObj);
                    getWeightRef("Goal_Date").setValue(formattedDate);
                    Intent intent = new Intent();
                    intent.putExtra("goal" , weight);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnDeleteAll = (Button) findViewById(R.id.buttonDeleteAll);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textGoalDateBox = (TextView) findViewById(R.id.goalDateBox);
        textGoalDateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragmentObj = new FutureDatePickerFragment();
                datePickerFragmentObj.show(getSupportFragmentManager(),"date picker frag tag2");
            }
        });

        textGoalWeightBox = (TextView) findViewById(R.id.goalWeightBox);
        textGoalWeightBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);



    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(cal.getTime());
        textGoalDateBox = (TextView)findViewById(R.id.goalDateBox);
        textGoalDateBox.setText(currentDateString);
    }

    private void openSubmitAlertDialog() {
        SubmitAlertDialog submitAlertDialogObj = new SubmitAlertDialog();
        submitAlertDialogObj.show(getSupportFragmentManager(),"submittable2");
    }

}
