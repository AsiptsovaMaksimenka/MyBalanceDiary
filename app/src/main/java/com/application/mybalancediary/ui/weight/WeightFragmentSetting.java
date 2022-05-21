package com.application.mybalancediary.ui.weight;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.application.mybalancediary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeightFragmentSetting extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView textGoalWeightBox;
    private TextView textGoalDateBox;
    private Button btnUpdateGoal;
    private Button btnDeleteAll;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings_weight);
        intent=getIntent();

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
                    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateObj = new Date(textGoalDateBox.getText().toString());
                    String formattedDate = simple.format(dateObj);

                }

            }
        });

    }

    private void openSubmitAlertDialog() {
        SubmitAlertDialog submitAlertDialogObj = new SubmitAlertDialog();
        submitAlertDialogObj.show(getSupportFragmentManager(),"submitalerttag2");
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
}
